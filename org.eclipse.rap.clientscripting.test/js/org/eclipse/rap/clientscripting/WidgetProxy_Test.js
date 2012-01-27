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
    
    testSetter : function() {
      var widgetProxy = WidgetProxy.getInstance( text );
      
      widgetProxy.setText( "foo" );

      assertEquals( "foo", text.getValue() );
    },
    
    testSetTextSync : function() {
      TestUtil.initRequestLog();
      var widgetProxy = WidgetProxy.getInstance( text );
      
      widgetProxy.setText( "foo" );
      var req = org.eclipse.swt.Request.getInstance().send();
      var msg = TestUtil.getMessage();
      assertTrue( msg.indexOf( "w3.text=foo" ) != -1 );
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
        "action" : "destroy",
      } );
      Processor.processOperation( {
        "target" : "w3",
        "action" : "destroy",
      } );
      text = null
    }

  }
    
} );

} )();