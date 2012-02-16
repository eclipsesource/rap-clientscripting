/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/
 
(function() {

var EventProxy = org.eclipse.rap.clientscripting.EventProxy;
var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
var Processor = org.eclipse.rwt.protocol.Processor;
var ObjectManager = org.eclipse.rwt.protocol.ObjectManager;
var WidgetProxy = org.eclipse.rap.clientscripting.WidgetProxy;

var text;

qx.Class.define( "org.eclipse.rap.clientscripting.WidgetProxy_Test", {

  extend : qx.core.Object,
  
  members : {

    testCreateTextWidgetProxy : function() {
      var widgetProxy = WidgetProxy.getInstance( text );

      assertTrue( widgetProxy instanceof WidgetProxy );
    },
    
    testCreateTextWidgetProxyTwice : function() {
      var widgetProxy1 = WidgetProxy.getInstance( text );
      var widgetProxy2 = WidgetProxy.getInstance( text );

      assertTrue( widgetProxy1 !== widgetProxy2 ); // protect against abuse
      if( widgetProxy1.__proto__ ) { // __proto__ is not an ECMA standard
        assertTrue( widgetProxy1.__proto__ instanceof WidgetProxy );
        assertTrue( widgetProxy1.__proto__ === widgetProxy1.__proto__ );
      }
    },
    
    testDisposeWidgetProxy : function() {
      var widgetProxy = WidgetProxy.getInstance( text );

      text.destroy();
      TestUtil.flush();
      
      assertTrue( TestUtil.hasNoObjects( widgetProxy ) );
      if( widgetProxy.__proto__ ) { // __proto__ is not an ECMA standard
        var proto = widgetProxy.__proto__;
        assertTrue( TestUtil.hasNoObjects( proto ) );
      }
    },
    
    testDisposeUserData : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      widgetProxy.setData( "key", {} );
      // hacky: get data object
      var data = text.getUserData( org.eclipse.rap.clientscripting.WidgetProxy._USERDATA_KEY );
      assertFalse( TestUtil.hasNoObjects( data ) );
      
      text.destroy();
      TestUtil.flush();
      
      assertTrue( TestUtil.hasNoObjects( data ) );
    },

    testSetter : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      
      widgetProxy.setText( "foo" );

      assertEquals( "foo", text.getValue() );
    },

    testSetGetData : function() {
      var widgetProxy1 = WidgetProxy.getInstance( text );
      var widgetProxy2 = WidgetProxy.getInstance( text );
      
      widgetProxy1.setData( "myKey", 24 );

      assertNull( widgetProxy2.getData( "myWrongKey" ) ); 
      assertEquals( 24, widgetProxy2.getData( "myKey" ) ); 
    },
    
    testSetDataTooManyArguments : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      try {
        widgetProxy.setData( "myKey", 24, "foo" );
        fail();
      } catch( ex ) {
        // expected
      }
    },
    
    testSetDataTooFewArguments : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      try {
        widgetProxy.setData( 24 );
        fail();
      } catch( ex ) {
        // expected
      }
    },
    
    testGetDataTooManyArguments : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      try {
        widgetProxy.getData( "myKey", 24 );
        fail();
      } catch( ex ) {
        // expected
      }
    },
    
    testGetDataTooFewArguments : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      try {
        widgetProxy.getData();
        fail();
      } catch( ex ) {
        // expected
      }
    },

    testSetTextSync : function() {
      TestUtil.initRequestLog();
      var widgetProxy = WidgetProxy.getInstance( text );
      
      widgetProxy.setText( "foo" );
      var req = org.eclipse.swt.Request.getInstance().send();
      var msg = TestUtil.getMessage();
      assertTrue( msg.indexOf( "w3.text=foo" ) != -1 );
    },

    testTextGetText : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      text.setValue( "foo" );
      
      var value = widgetProxy.getText();
      
      assertEquals( "foo", value );
    },

    testTextGetSelection : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      text.setValue( "foo" );
      text.setSelection( [ 1,2 ] );

      var value = widgetProxy.getSelection();

      assertEquals( [ 1, 2 ], value );
    },

    ////////
    // Helper

    _setUp : function() {
      TestUtil.createShellByProtocol( "w2" );
      Processor.processOperation( {
        "target" : "w3",
        "action" : "create",
        "type" : "rwt.widgets.Text",
        "properties" : {
          "style" : [ "SINGLE", "RIGHT" ],
          "parent" : "w2"
        }
      } );
      TestUtil.flush();
      text = ObjectManager.getObject( "w3" );
      text.focus();
    },
    
    _tearDown : function() {
      Processor.processOperation( {
        "target" : "w2",
        "action" : "destroy"
      } );
      Processor.processOperation( {
        "target" : "w3",
        "action" : "destroy"
      } );
      text = null;
    }

  }
    
} );

}());