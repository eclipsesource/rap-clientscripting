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
var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
var ObjectManager = org.eclipse.rwt.protocol.ObjectManager;

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

    testImportedClasses : function() {
      var obj = {};
      var code = "function handleEvent( obj ){ obj.SWT = SWT; }";
      var fun = new Function( code );

      fun.call( obj );

      assertIdentical( SWT, obj.SWT );
    },

    testCreateFunctionWithWidgetsInScope : function() {
      var shell = TestUtil.createShellByProtocol( "w2" );
      var code = "function handleEvent(){ global = myWidget; }";
      var scope = { "myWidget" : { "type" : "widget", "id" : "w2" } };
      var Processor = org.eclipse.rwt.protocol.Processor;
      Processor.processOperation( {
        "target" : "w4",
        "action" : "create",
        "type" : "rwt.clientscripting.Listener",
        "properties" : {
          "code" : code,
          "scope" : scope
        }
      } );
      var result = ObjectManager.getObject( "w4" );

      result.call();

      assertTrue( global instanceof org.eclipse.rap.clientscripting.WidgetProxy );
      assertEquals( "w2", global.getData( org.eclipse.rap.clientscripting.WidgetProxy._ID ) );
      delete global;
      shell.destroy();
    },

    testCreateFunctionWithStringInScope : function() {
      var code = "function handleEvent(){ global = myString; }";
      var scope = { "myString" : "foo" };
      var Processor = org.eclipse.rwt.protocol.Processor;
      Processor.processOperation( {
        "target" : "w4",
        "action" : "create",
        "type" : "rwt.clientscripting.Listener",
        "properties" : {
          "code" : code,
          "scope" : scope
        }
      } );
      var result = ObjectManager.getObject( "w4" );

      result.call();

      assertEquals( "foo", global );
      delete global;
    },

    testCreateFunctionWithIntInScope : function() {
      var code = "function handleEvent(){ global = myInt; }";
      var scope = { "myInt" : 44 };
      var Processor = org.eclipse.rwt.protocol.Processor;
      Processor.processOperation( {
        "target" : "w4",
        "action" : "create",
        "type" : "rwt.clientscripting.Listener",
        "properties" : {
          "code" : code,
          "scope" : scope
        }
      } );
      var result = ObjectManager.getObject( "w4" );

      result.call();

      assertEquals( 44, global );
      delete global;
    },

    testCreateFunctionWithFunctionInScope : function() {
      var shell = TestUtil.createShellByProtocol( "w2" );
      var code = "function handleEvent(){ global = func; }";
      var scope = { "func" : { "type" : "function" } };
      var Processor = org.eclipse.rwt.protocol.Processor;
      Processor.processOperation( {
        "target" : "w4",
        "action" : "create",
        "type" : "rwt.clientscripting.Listener",
        "properties" : {
          "code" : code,
          "scope" : scope
        }
      } );
      var result = ObjectManager.getObject( "w4" );

      result.call();

      assertTrue( typeof global === "function" );
      delete global;
      shell.destroy();
    },

    testCallJavaFunction : function() {
      var shell = TestUtil.createShellByProtocol( "w2" );
      var code = "function handleEvent(){ func(); }";
      var scope = { "func" : { "type" : "function" } };
      TestUtil.initRequestLog();
      var Processor = org.eclipse.rwt.protocol.Processor;
      Processor.processOperation( {
        "target" : "w4",
        "action" : "create",
        "type" : "rwt.clientscripting.Listener",
        "properties" : {
          "code" : code,
          "scope" : scope
        }
      } );
      var result = ObjectManager.getObject( "w4" );

      result.call();

      var log = TestUtil.getRequestLog();
      assertEquals( 1, log.length );
      assertTrue( log[ 0 ].indexOf( "w4.executeFunction=func" ) !== -1 );
      shell.destroy();
    },

    testJavaScriptOverwritesJavaFunction : function() {
      var shell = TestUtil.createShellByProtocol( "w2" );
      var code = "var func = function(){};function handleEvent(){ func(); }";
      var scope = { "func" : { "type" : "function" } };
      TestUtil.initRequestLog();
      var Processor = org.eclipse.rwt.protocol.Processor;
      Processor.processOperation( {
        "target" : "w4",
        "action" : "create",
        "type" : "rwt.clientscripting.Listener",
        "properties" : {
          "code" : code,
          "scope" : scope
        }
      } );
      var result = ObjectManager.getObject( "w4" );

      result.call();

      var log = TestUtil.getRequestLog();
      assertEquals( 0, log.length );
      shell.destroy();
    },

    testCreateTwoJavaFunctions : function() {
      var shell = TestUtil.createShellByProtocol( "w2" );
      var code = "function handleEvent(){ funcA();funcB(); }";
      var scope = { "funcA" : { "type" : "function" }, "funcB" : { "type" : "function" } };
      TestUtil.initRequestLog();
      var Processor = org.eclipse.rwt.protocol.Processor;
      Processor.processOperation( {
        "target" : "w4",
        "action" : "create",
        "type" : "rwt.clientscripting.Listener",
        "properties" : {
          "code" : code,
          "scope" : scope
        }
      } );
      var result = ObjectManager.getObject( "w4" );

      result.call();

      var log = TestUtil.getRequestLog();
      assertEquals( 2, log.length );
      shell.destroy();
    }

  }

} );

}());