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
 
qx.Class.define( "org.eclipse.rap.clientscripting.ClientEventListener_Test", {

  extend : qx.core.Object,
  
  members : {
    
    testCreateClientEventListener : function() {
      var code = "function(){}";
      
      var listener = new org.eclipse.rap.clientscripting.ClientEventListener( code );
      
      assertTrue( listener instanceof org.eclipse.rap.clientscripting.ClientEventListener );
    },
    
    testCreateClientEventListenerSyntaxError : function() {
      var code = "null.no!;";
      try {
        new org.eclipse.rap.clientscripting.ClientEventListener( code );
        fail();
      } catch( ex ) {
        // expected
      }
    },
    
    testCreateClientEventListenerNoFunction : function() {
      var code = "1";
      try {
        new org.eclipse.rap.clientscripting.ClientEventListener( code );
        fail();
      } catch( ex ) {
        // expected
      }
    },

    testHandleEvent : function() {
      var code = "function( e ){ e.x++; }";
      var listener = new org.eclipse.rap.clientscripting.ClientEventListener( code );
      var event = {
        x : 1
      };
      
      listener.handleEvent( event );
      
      assertEquals( 2, event.x );
    },

    testNoContext : function() {
      var code = "function( ){ this.x++; }";
      var listener = new org.eclipse.rap.clientscripting.ClientEventListener( code );
      listener.x = 1;
      
      listener.handleEvent();
      
      assertEquals( 1, listener.x );
    },
    
    testAddKeyListener : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var text = new org.eclipse.rwt.widgets.Text();
      text.addToDocument();
      TestUtil.flush();
      globalVar = 1;
      var code = "function(){ globalVar++; }";
      var listener = new org.eclipse.rap.clientscripting.ClientEventListener( code );
      
      this._addListener( text, "KeyDown", listener );
      TestUtil.press( text, "A" );
      
      assertEquals( 2, globalVar );
      delete globalVar;
      text.destroy();
    },
    
    testRemoveKeyListener : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var text = new org.eclipse.rwt.widgets.Text();
      text.addToDocument();
      TestUtil.flush();
      globalVar = 1;
      var code = "function(){ globalVar++; }";
      var listener = new org.eclipse.rap.clientscripting.ClientEventListener( code );
      
      this._addListener( text, "KeyDown", listener );
      this._removeListener( text, "KeyDown", listener );
      TestUtil.press( text, "A" );
      
      assertEquals( 1, globalVar );
      delete globalVar;
      text.destroy();
    },
    
    /////////
    // Helper
    
    _addListener : function( widget, type, listener ) {
      org.eclipse.rap.clientscripting.ClientScriptingUtil.addListener( widget, type, listener );
    },

    _removeListener : function( widget, type, listener ) {
      org.eclipse.rap.clientscripting.ClientScriptingUtil.removeListener( widget, type, listener );
    }

  }
  
} );