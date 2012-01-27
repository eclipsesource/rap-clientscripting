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

var Function = org.eclipse.rap.clientscripting.Function;

qx.Class.define( "org.eclipse.rap.clientscripting.Function_Test", {

  extend : qx.core.Object,
  
  members : {
    
    testCreateFunction : function() {
      var code = "function(){}";
      
      var listener = new Function( code );
      
      assertTrue( listener instanceof Function );
    },
    
    testCreateFunctionSyntaxError : function() {
      var code = "null.no!;";
      try {
        new Function( code );
        fail();
      } catch( ex ) {
        // expected
      }
    },
    
    testCreateFunctionNoFunction : function() {
      var code = "1";
      try {
        new Function( code );
        fail();
      } catch( ex ) {
        // expected
      }
    },

    testCallWithArgument : function() {
      var code = "function( e ){ e.x++; }";
      var listener = new Function( code );
      var event = {
        x : 1
      };

      listener.call( event );

      assertEquals( 2, event.x );
    },

    testNoContext : function() {
      var code = "function( ){ this.x++; }";
      var listener = new Function( code );
      listener.x = 1;

      listener.call();

      assertEquals( 1, listener.x );
    }

  }
  
} );

} )();