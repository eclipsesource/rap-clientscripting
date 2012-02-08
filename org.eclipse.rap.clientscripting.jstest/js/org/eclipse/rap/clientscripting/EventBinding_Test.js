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
var textEl;

qx.Class.define( "org.eclipse.rap.clientscripting.EventBinding_Test", {

  extend : qx.core.Object,
  
  members : {
    
    testCreateBinding : function() {
      var listener = new Function( "function handleEvent(){}" );
      
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

    testBindKeyUp : function() {
      TestUtil.flush();
      var logger = this._createLogger();

      var binding = new EventBinding( text, SWT.KeyUp, logger );
      TestUtil.keyDown( textEl, "A" );
      assertEquals( 0, logger.log.length );
      TestUtil.keyUp( textEl, "A" );

      assertEquals( 1, logger.log.length );
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
      TestUtil.fakeMouseEventDOM( textEl, "mousedown" );

      assertEquals( 1, logger.log.length );
    },

    testBindMouseUp : function() {
      var logger = this._createLogger(); 

      TestUtil.fakeMouseEventDOM( textEl, "mousedown" );
      var binding = new EventBinding( text, SWT.MouseUp, logger );
      TestUtil.fakeMouseEventDOM( textEl, "mouseup" );

      assertEquals( 1, logger.log.length );
    },

    testBindMouseMove : function() {
      var logger = this._createLogger(); 

      TestUtil.fakeMouseEventDOM( textEl, "mouseover" );
      var binding = new EventBinding( text, SWT.MouseMove, logger );
      TestUtil.fakeMouseEventDOM( textEl, "mousemove" );

      assertEquals( 1, logger.log.length );
    },

    testBindMouseEnter : function() {
      var logger = this._createLogger(); 

      var binding = new EventBinding( text, SWT.MouseEnter, logger );
      TestUtil.fakeMouseEventDOM( textEl, "mouseover" );

      assertEquals( 1, logger.log.length );
    },

    testBindMouseExit : function() {
      var logger = this._createLogger(); 

      TestUtil.fakeMouseEventDOM( textEl, "mouseover" );
      var binding = new EventBinding( text, SWT.MouseExit, logger );
      TestUtil.fakeMouseEventDOM( textEl, "mouseout" );

      assertEquals( 1, logger.log.length );
    },

    testBindVerifyEvent : function() {
      TestUtil.flush();
      var logger = this._createLogger(); 

      var binding = new EventBinding( text, SWT.Verify, logger );
      this._inputText( text, "goo" );

      assertEquals( 1, logger.log.length );
    },

    testDisposeVerifyEventBinding : function() {
      TestUtil.flush();
      var logger = this._createLogger(); 

      var binding = new EventBinding( text, SWT.Verify, logger );
      binding.dispose();
      this._inputText( text, "goo" );

      assertEquals( 0, logger.log.length );
      assertEquals( "goo", text.getValue() );
    },

    testVerifyEventFiredBeforeChange : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var textValue;
      var handler = {
        "call" : function( event ) {
          textValue = event.widget.getText();
        }
      } ;

      var binding = new EventBinding( text, SWT.Verify, handler );
      this._inputText( text, "bar" );

      assertEquals( "bar", text.getValue() );
      assertEquals( "foo", textValue );
    },

    testVerifyEventDoItFalse : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var handler = {
        "call" : function( event ) {
          event.doit = false;
        }
      } ;

      var binding = new EventBinding( text, SWT.Verify, handler );
      this._inputText( text, "bar" );

      assertEquals( "foo", text.getValue() );
      assertEquals( "foo", text.getComputedValue() );
    },

    testVerifyEventDoItFalseSelection : function() {
      TestUtil.flush();
      text.setValue( "fooxxx" );
      var handler = {
        "call" : function( event ) {
          event.doit = false;
        }
      } ;

      var binding = new EventBinding( text, SWT.Verify, handler );
      this._inputText( text, "foobarxxx", 6 );

      assertEquals( 3, text.getSelectionStart() );
    },

    testVerifyBindingProtectAgainstTypeOverwrite : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var handler = {
        "call" : function( event ) {
          event.type = "boom";
        }
      } ;

      var binding = new EventBinding( text, SWT.Verify, handler );
      this._inputText( text, "bar" );

      assertEquals( "bar", text.getValue() );
    },

    testVerifyEventTextOverwrite : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var handler = {
        "call" : function( event ) {
          event.text = "bar";
        }
      } ;

      var binding = new EventBinding( text, SWT.Verify, handler );
      this._inputText( text, "foob" );

      assertEquals( "foobar", text.getValue() );
    },

    testVerifyEventSelectionAferTextOverwrite : function() {
      TestUtil.flush();
      text.setValue( "foo" );
      var handler = {
        "call" : function( event ) {
          event.text = "bar";
        }
      } ;

      var binding = new EventBinding( text, SWT.Verify, handler );
      this._inputText( text, "foxo", 3 );

      assertEquals( "fobaro", text.getValue() );
      assertEquals( 5, text.getSelectionStart() );
      assertEquals( 0, text.getSelectionLength() );
    },

    testBindModifyEvent : function() {
      TestUtil.flush();
      var logger = this._createLogger(); 

      new EventBinding( text, SWT.Modify, logger );
      text.setValue( "foo" );

      assertEquals( 1, logger.log.length );
    },

    testBindVerifyAndModifyEvent : function() {
      TestUtil.flush();
      var logger = this._createLogger(); 

      new EventBinding( text, SWT.Modify, logger );
      new EventBinding( text, SWT.Verify, logger );
      this._inputText( text, "foo" );

      assertEquals( 2, logger.log.length );
      assertEquals( SWT.Verify, logger.log[ 0 ].type )
      assertEquals( SWT.Modify, logger.log[ 1 ].type )
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
      textEl = text.getElement();
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
    },
    
    _inputText : function( textWidget, text, sel ) {
      textWidget._inputElement.value = text;
      if( typeof sel !== "undefined" ) {
        textWidget.setSelectionStart( sel );
      } else {
        textWidget.setSelectionStart( text.length );
      }
      textWidget._oninputDom();      
    }

  }
  
} );

})();