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
var SWT = org.eclipse.rap.clientscripting.SWT;

var text;
var shell;
 
qx.Class.define( "org.eclipse.rap.clientscripting.EventProxy_Test", {

  extend : qx.core.Object,
  
  members : {

    testCreateEventProxy : function() {
      var eventProxy;
      text.addEventListener( "keypress", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.KeyDown, event );
      } );

      TestUtil.press( text, "a" );

      assertTrue( eventProxy instanceof EventProxy );
    },

    testEventProxyFields : function() {
      var eventProxy;
      text.addEventListener( "keypress", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.KeyDown, event );
      } );

      TestUtil.press( text, "a" );
      
      // Test type (or initial value) of all currently supported fields
      assertTrue( eventProxy.doit );
      assertEquals( "number", typeof eventProxy.type );
      assertEquals( "string", typeof eventProxy.character );
      assertEquals( "string", typeof eventProxy.text );
      assertEquals( "number", typeof eventProxy.keyCode );
      assertEquals( "number", typeof eventProxy.stateMask );
      assertEquals( "number", typeof eventProxy.button );
      assertEquals( "number", typeof eventProxy.x );
      assertEquals( "number", typeof eventProxy.y );
      assertTrue( eventProxy.widget instanceof WidgetProxy );
    },

    testType : function() {
      var eventProxy;
      text.addEventListener( "keypress", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.KeyDown, event );
      } );

      TestUtil.press( text, "a" );
      
      assertEquals( SWT.KeyDown, eventProxy.type );
    },

    testCharacter : function() {
      var eventProxy;
      text.addEventListener( "keypress", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.KeyDown, event );
      } );

      TestUtil.press( text, "a" );
      
      assertEquals( "a", eventProxy.character );
    },

    testKeyCodeCharacterLowerCase : function() {
      var eventProxy;
      text.addEventListener( "keypress", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.KeyDown, event );
      } );

      TestUtil.press( text, "a" );
      
      assertEquals( 97, eventProxy.keyCode );
    },

    testKeyCodeCharacterUpperCase : function() {
      var eventProxy;
      text.addEventListener( "keypress", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.KeyDown, event );
      } );

      TestUtil.press( text, "A" );
      
      assertEquals( 97, eventProxy.keyCode );
    },

    testKeyCodeCharacterNonPrintable : function() {
      var eventProxy;
      text.addEventListener( "keypress", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.KeyDown, event );
      } );

      TestUtil.press( text, "Up" );
      
      assertEquals( '\u0000', eventProxy.character );
      assertEquals( SWT.ARROW_UP, eventProxy.keyCode );
    },

    testKeyCodeModifierKey : function() {
      var eventProxy;
      text.addEventListener( "keypress", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.KeyDown, event );
      } );

      var shift = qx.event.type.DomEvent.SHIFT_MASK;
      TestUtil.keyDown( text.getElement(), "Shift", shift );
      
      assertEquals( '\u0000', eventProxy.character );
      assertEquals( SWT.SHIFT, eventProxy.keyCode );
    },

    testModifierStateMask : function() {
      var eventProxy;
      text.addEventListener( "keypress", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.KeyDown, event );
      } );

      var shift = qx.event.type.DomEvent.SHIFT_MASK;
      var ctrl = qx.event.type.DomEvent.CTRL_MASK;
      TestUtil.keyDown( text.getElement(), "A", shift | ctrl );
      
      assertEquals( SWT.SHIFT | SWT.CTRL, eventProxy.stateMask );
    },

    testMouseEventStateMask : function() {
      var eventProxy;
      text.addEventListener( "mousedown", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.MouseDown, event );
      } );

      TestUtil.shiftClick( text );
      
      assertEquals( SWT.SHIFT, eventProxy.stateMask );
    },

    testMouseEventButtonLeft : function() {
      var eventProxy;
      text.addEventListener( "mousedown", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.MouseDown, event );
      } );

      TestUtil.click( text );
      
      assertEquals( 1, eventProxy.button );
    },

    testMouseEventButtonRight : function() {
      var eventProxy;
      text.addEventListener( "mousedown", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.MouseDown, event );
      } );

      TestUtil.rightClick( text );
      
      assertEquals( 3, eventProxy.button );
    },

    testMouseEventLocation : function() {
      text.setLocation( 10, 20 );
      text.setBorder( null );
      TestUtil.flush();
      var eventProxy;
      text.addEventListener( "mousedown", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.MouseDown, event );
      } );

      TestUtil.click( text, 23, 34 );
      
      assertEquals( 3, eventProxy.x );
      assertEquals( 4, eventProxy.y );
    },

    testMouseEventLocationWithBorder : function() {
      text.setLocation( 10, 20 );
      text.setBorder( new org.eclipse.rwt.Border( 2, "solid", "black" ) );
      TestUtil.flush();
      var eventProxy;
      text.addEventListener( "mousedown", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.MouseDown, event );
      } );

      TestUtil.click( text, 23, 34 );
      
      assertEquals( 1, eventProxy.x );
      assertEquals( 2, eventProxy.y );
    },

    testVerifyEventCharacter : function() {
      var eventProxy;
      text.setValue( "fooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textCharInput( text, "A" );
      
      assertEquals( 65, eventProxy.keyCode );
      assertEquals( 'A', eventProxy.character );
      assertEquals( "A", eventProxy.text );
      assertEquals( 5, eventProxy.start );
      assertEquals( 5, eventProxy.end );
    },

    testVerifyEventCharacterFirstChar : function() {
      var eventProxy;
      text.setValue( "" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textCharInput( text, "A" );
      
      assertEquals( 65, eventProxy.keyCode );
      assertEquals( 'A', eventProxy.character );
      assertEquals( "A", eventProxy.text );
      assertEquals( 0, eventProxy.start );
      assertEquals( 0, eventProxy.end );
    },

    testVerifyEventCharacterLeftCharacterSame : function() {
      var eventProxy;
      text.setValue( "fooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textCharInput( text, "a" );
      
      assertEquals( 65, eventProxy.keyCode );
      assertEquals( 'a', eventProxy.character );
      assertEquals( "a", eventProxy.text );
      assertEquals( 5, eventProxy.start );
      assertEquals( 5, eventProxy.end );
    },

    testVerifyEventPasteCharacter : function() {
      var eventProxy;
      text.setValue( "fooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textPaste( text, "fooAba", 4 );
      
      assertEquals( 0, eventProxy.keyCode );
      assertEquals( '\u0000', eventProxy.character );
      assertEquals( "A", eventProxy.text );
      assertEquals( 3, eventProxy.start );
      assertEquals( 3, eventProxy.end );
    },

    testVerifyEventPasteCharacterBetweenSameCharacters : function() {
      var eventProxy;
      text.setValue( "fooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textPaste( text, "foooba", 3 );
      
      assertEquals( "o", eventProxy.text );
      assertEquals( 2, eventProxy.start );
      assertEquals( 2, eventProxy.end );
    },

    testVerifyEventPasteText : function() {
      var eventProxy;
      text.setValue( "fooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textPaste( text, "fooLALAba", 7 );
      
      assertEquals( "LALA", eventProxy.text );
      assertEquals( 3, eventProxy.start );
      assertEquals( 3, eventProxy.end );
    },

    testVerifyEventDeleteCharacter : function() {
      var eventProxy;
      text.setValue( "fooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textPaste( text, "foob", 4 );
      
      assertEquals( 0, eventProxy.keyCode );
      assertEquals( '\u0000', eventProxy.character );
      assertEquals( "", eventProxy.text );
      assertEquals( 4, eventProxy.start );
      assertEquals( 5, eventProxy.end );
    },

    testVerifyEventDeleteMultiple : function() {
      var eventProxy;
      text.setValue( "fooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textPaste( text, "fba", 1 );
      
      assertEquals( 0, eventProxy.keyCode );
      assertEquals( '\u0000', eventProxy.character );
      assertEquals( "", eventProxy.text );
      assertEquals( 1, eventProxy.start );
      assertEquals( 3, eventProxy.end );
    },

    testVerifyEventDeleteMultipleAmbigous : function() {
      var eventProxy;
      text.setValue( "foooooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textPaste( text, "fooba", 1 );
      
      assertEquals( "", eventProxy.text );
      assertEquals( 1, eventProxy.start );
      assertEquals( 4, eventProxy.end );
    },

    testVerifyEventReplaceLonger : function() {
      var eventProxy;
      text.setValue( "foooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textPaste( text, "fooaaba", 5 );

      assertEquals( "aa", eventProxy.text );
      assertEquals( 3, eventProxy.start );
      assertEquals( 4, eventProxy.end );
    },

    testVerifyEventReplaceLongerSameEnd : function() {
      var eventProxy;
      text.setValue( "foooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textPaste( text, "fooaaba", 6 );

      assertEquals( "aab", eventProxy.text );
      assertEquals( 3, eventProxy.start );
      assertEquals( 5, eventProxy.end );
    },

    testVerifyEventInsertSameStart : function() {
      var eventProxy;
      text.setValue( "foooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textPaste( text, "foooooba", 5 );
      
      // ambigous case, might also be ooo or oooo - impossible to say without previous selection
      assertEquals( "oo", eventProxy.text );
      assertEquals( 3, eventProxy.start );
      assertEquals( 3, eventProxy.end );
    },

    testVerifyEventReplaceShorter : function() {
      var eventProxy;
      text.setValue( "foooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textPaste( text, "faba", 2 );
      
      assertEquals( "a", eventProxy.text );
      assertEquals( 1, eventProxy.start );
      assertEquals( 4, eventProxy.end );
    },

    testVerifyEventReplaceShorterSameEnd : function() {
      var eventProxy;
      text.setValue( "foooba" );
      text.setLiveUpdate( false );
      text.addEventListener( "input", function( event ) {
        eventProxy = new org.eclipse.rap.clientscripting.EventProxy( SWT.Verify, event );
      } );

      this._textPaste( text, "faoba", 3 );

      assertEquals( "ao", eventProxy.text );
      assertEquals( 1, eventProxy.start );
      assertEquals( 4, eventProxy.end );
    },

    ///////// 
    // Helper

    _setUp : function() {
      shell = TestUtil.createShellByProtocol( "w2" );
      shell.setBorder( null );
      shell.setLocation( 10, 10 );
      shell.setDimension( 300, 300 );
      Processor.processOperation( {
        "target" : "w3",
        "action" : "create",
        "type" : "rwt.widgets.Text",
        "properties" : {
          "style" : [ "SINGLE", "RIGHT" ],
          "parent" : "w2",
          "bounds" : [ 0, 0, 10, 10 ]
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
    },
    
    _textCharInput : function( textWidget, character ) {
      TestUtil.keyDown( text, character );
      // we will assume that the carret is at the end
      var newValue = textWidget._inputElement.value + character;
      textWidget._inValueProperty = true;
      textWidget._inputElement.value = newValue;
      textWidget._inValueProperty = false;
      textWidget.setSelectionStart( newValue.length );
      textWidget._oninputDom( { "propertyName" : "value" } );
      TestUtil.keyUp( text, character );
    },

    _textPaste : function( textWidget, value, sel ) {
      textWidget._inValueProperty = true;
      textWidget._inputElement.value = value;
      textWidget._inValueProperty = false;
      if( typeof sel !== "undefined" ) {
        textWidget.setSelectionStart( sel );
      } else {
        textWidget.setSelectionStart( value.length );
      }
      textWidget._oninputDom( { "propertyName" : "value" } );
    }

  }
    
} );

}());