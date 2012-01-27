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
      assertEquals( "number", typeof eventProxy.keyCode );
      assertEquals( "number", typeof eventProxy.stateMask );
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

    ///////// 
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