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
 
qx.Class.define( "org.eclipse.rap.clientscripting.EventBinding_Test", {

  extend : qx.core.Object,
  
  members : {
    
    testCreateBinding : function() {
      var text = new org.eclipse.rwt.widgets.Text();
      var listener = new org.eclipse.rap.clientscripting.Function( "function(){}" );
      
      var binding = new org.eclipse.rap.clientscripting.EventBinding( text, "KeyDown", listener );
      
      assertTrue( binding instanceof org.eclipse.rap.clientscripting.EventBinding );
    }
    

//    testAddKeyListener : function() {
//      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
//      var text = new org.eclipse.rwt.widgets.Text();
//      text.addToDocument();
//      TestUtil.flush();
//      globalVar = 1;
//      var code = "function(){ globalVar++; }";
//      var listener = new org.eclipse.rap.clientscripting.Function( code );
//
//      this._addListener( text, "KeyDown", listener );
//      TestUtil.press( text, "A" );
//
//      assertEquals( 2, globalVar );
//      delete globalVar;
//      text.destroy();
//    },
//    
//    testRemoveKeyListener : function() {
//      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
//      var text = new org.eclipse.rwt.widgets.Text();
//      text.addToDocument();
//      TestUtil.flush();
//      globalVar = 1;
//      var code = "function(){ globalVar++; }";
//      var listener = new org.eclipse.rap.clientscripting.Function( code );
//
//      this._addListener( text, "KeyDown", listener );
//      this._removeListener( text, "KeyDown", listener );
//      TestUtil.press( text, "A" );
//
//      assertEquals( 1, globalVar );
//      delete globalVar;
//      text.destroy();
//    },
//    
//    /////////
//    // Helper
//    
//    _addListener : function( widget, type, listener ) {
//      org.eclipse.rap.clientscripting.ClientScriptingUtil.addListener( widget, type, listener );
//    },
//
//    _removeListener : function( widget, type, listener ) {
//      org.eclipse.rap.clientscripting.ClientScriptingUtil.removeListener( widget, type, listener );
//    }

  }
  
} );