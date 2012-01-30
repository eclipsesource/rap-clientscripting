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

var EventBinding = org.eclipse.rap.clientscripting.EventBinding;
var EventProxy = org.eclipse.rap.clientscripting.EventProxy;
var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
var Processor = org.eclipse.rwt.protocol.Processor;
var ObjectManager = org.eclipse.rwt.protocol.ObjectManager;
var Function = org.eclipse.rap.clientscripting.Function;
var SWT = org.eclipse.rap.clientscripting.SWT;
var EventHandlerUtil = org.eclipse.rwt.EventHandlerUtil;

var text;

qx.Class.define( "org.eclipse.rap.clientscripting.EventBinding_Test", {

  extend : qx.core.Object,
  
  members : {
    
    testCreateBinding : function() {
      var listener = new Function( "function(){}" );
      
      var binding = new EventBinding( text, SWT.KeyDown, listener );
      
      assertTrue( binding instanceof EventBinding );
    },

    testBindKeyEvent : function() {
      TestUtil.flush();
      var logger = this._createLogger(); 

      var binding = new EventBinding( text, SWT.KeyDown, logger );
      TestUtil.press( text, "A" );

      assertEquals( 1, logger.log.length );
    },

    testDisposeBindKeyEvent : function() {
      var logger = this._createLogger(); 

      var binding = new EventBinding( text, SWT.KeyDown, logger );
      binding.dispose();
      TestUtil.press( text, "A" );

      assertEquals( 0, logger.log.length );
      assertNull( binding._source );
      assertNull( binding._targetFunction );
    },

    testBindCreatesProxyEvent : function() {
      var logger = this._createLogger(); 

      var binding = new EventBinding( text, SWT.KeyDown, logger );
      TestUtil.press( text, "A" );

      var event = logger.log[ 0 ];
      assertTrue( event instanceof EventProxy );
    },

    testBindDisposesProxyEvent : function() {
      var logger = this._createLogger(); 

      var binding = new EventBinding( text, SWT.KeyDown, logger );
      TestUtil.press( text, "A" );

      var event = logger.log[ 0 ];
      assertTrue( TestUtil.hasNoObjects( event ) );
    },

    testDoItFalse : function() {
      var listener = {
        "call" : function( event ) {
          event.doit = false;
        }
      };

      var binding = new EventBinding( text, SWT.KeyDown, listener );
      var domEvent = TestUtil.createFakeDomKeyEvent( text.getElement(), "keypress", "a" );
      TestUtil.fireFakeDomEvent( domEvent );
      
      assertTrue( EventHandlerUtil.wasStopped( domEvent ) );
    },

    testBindFocusInEvent : function() {
      text.blur();
      var logger = this._createLogger(); 

      var binding = new EventBinding( text, SWT.FocusIn, logger );
      text.focus();

      assertEquals( 1, logger.log.length );
    },

    testBindFocusOutEvent : function() {
      text.focus();
      var logger = this._createLogger(); 

      var binding = new EventBinding( text, SWT.FocusOut, logger );
      text.blur();

      assertEquals( 1, logger.log.length );
    },

    testBindMouseDown : function() {
      var logger = this._createLogger(); 

      var binding = new EventBinding( text, SWT.MouseDown, logger );
      TestUtil.click( text );

      assertEquals( 1, logger.log.length );
    },


    /////////
    // helper
    
    _createLogger : function() {
      var log = [];
      var result = {
        "log" : log,
        "call" : function( arg ) {
          log.push( arg );
        }
      };
      return result;
    },

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

})();