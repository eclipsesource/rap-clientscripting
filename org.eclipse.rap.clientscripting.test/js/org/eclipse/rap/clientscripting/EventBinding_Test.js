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
      this._setUp();
      var listener = new org.eclipse.rap.clientscripting.Function( "function(){}" );
      
      var binding 
        = new org.eclipse.rap.clientscripting.EventBinding( this._text, "KeyDown", listener );
      
      assertTrue( binding instanceof org.eclipse.rap.clientscripting.EventBinding );
      this._tearDown();
    },

    testBindKeyEvent : function() {
      this._setUp();
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      TestUtil.flush();
      var logger = this._createLogger(); 

      var binding = new org.eclipse.rap.clientscripting.EventBinding( this._text, "KeyDown", logger );
      TestUtil.press( this._text, "A" );

      assertEquals( 1, logger.log.length );
      this._tearDown();
    },

    testDisposeBindKeyEvent : function() {
      this._setUp();
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var logger = this._createLogger(); 

      var binding = new org.eclipse.rap.clientscripting.EventBinding( this._text, "KeyDown", logger );
      binding.dispose();
      TestUtil.press( this._text, "A" );

      assertEquals( 0, logger.log.length );
      assertNull( binding._source );
      assertNull( binding._targetFunction );
      this._tearDown();
    },

    testBindCreatesProxyEvent : function() {
      this._setUp();
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var logger = this._createLogger(); 

      var binding = new org.eclipse.rap.clientscripting.EventBinding( this._text, "KeyDown", logger );
      TestUtil.press( this._text, "A" );

      var event = logger.log[ 0 ];
      assertTrue( event instanceof org.eclipse.rap.clientscripting.EventProxy );
      this._tearDown();
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
      var processor = org.eclipse.rwt.protocol.Processor;
      processor.processOperation( {
        "target" : "w3",
        "action" : "create",
        "type" : "rwt.widgets.Text",
        "properties" : {
          "style" : [ "SINGLE", "RIGHT" ],
          "parent" : "w2"
        }
      } );
      TestUtil.flush();
      var ObjectManager = org.eclipse.rwt.protocol.ObjectManager;
      this._text = ObjectManager.getObject( "w3" );
      this._text.focus();
    },
    
    _tearDown : function() {
      org.eclipse.rwt.protocol.Processor.processOperation( {
        "target" : "w2",
        "action" : "destroy",
      } );
      org.eclipse.rwt.protocol.Processor.processOperation( {
        "target" : "w3",
        "action" : "destroy",
      } );
      this._text = null
    }

  }
  
} );