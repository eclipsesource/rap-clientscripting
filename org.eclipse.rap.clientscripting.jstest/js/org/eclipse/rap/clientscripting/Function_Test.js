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
var SWT = org.eclipse.rap.clientscripting.SWT;

qx.Class.define( "org.eclipse.rap.clientscripting.Function_Test", {

  extend : qx.core.Object,
  
  members : {
    
    testCreateFunctionWrongNamed : function() {
      var code = "function foo(){}";
      try {
        new Function( code );
        fail();
      } catch( ex ) {
        // expected
      }
    },

    /*global global:true */ 
    testCreateFunctionWithHelper : function() {
      var code = "var foo = function(){  global = 1;  };var handleEvent = function(){ foo(); };";
      var listener = new Function( code );
      listener.call();
      assertEquals( 1, global );
      delete global;
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

    testCreateFunctionByProtocol : function() {
      var code = "var handleEvent = function(){};";
      var processor = org.eclipse.rwt.protocol.Processor;

      processor.processOperation( {
        "target" : "w3",
        "action" : "create",
        "type" : "rwt.clientscripting.Listener",
        "properties" : {
          "code" : code
        }
      } );

      var ObjectManager = org.eclipse.rwt.protocol.ObjectManager;
      var result = ObjectManager.getObject( "w3" );
      assertTrue( result instanceof Function );
    },

    testCallWithArgument : function() {
      var code = "function handleEvent( e ){ e.x++; }";
      var listener = new Function( code );
      var event = {
        x : 1
      };

      listener.call( event );

      assertEquals( 2, event.x );
    },

    testNoContext : function() {
      var code = "var handleEvent = function(){ this.x++; }";
      var listener = new Function( code );
      listener.x = 1;

      listener.call();

      assertEquals( 1, listener.x );
    },

    testImportedClasses : function() {
      var obj = {};
      var code = "function handleEvent( obj ){ obj.SWT = SWT; }";
      var fun = new Function( code );

      fun.call( obj );

      assertIdentical( SWT, obj.SWT );
    }

  }
  
} );

}());