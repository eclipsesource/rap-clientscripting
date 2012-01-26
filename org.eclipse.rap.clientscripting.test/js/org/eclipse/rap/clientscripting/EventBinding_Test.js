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
    },

    testBindKeyEvent : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var text = new org.eclipse.rwt.widgets.Text();
      text.addToDocument();
      TestUtil.flush();
      var logger = this._createLogger(); 

      var binding = new org.eclipse.rap.clientscripting.EventBinding( text, "KeyDown", logger );
      TestUtil.press( text, "A" );

      assertEquals( 1, logger.log.length );
      text.destroy();
    },
    
    testDisposeBindKeyEvent : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var text = new org.eclipse.rwt.widgets.Text();
      text.addToDocument();
      TestUtil.flush();
      var logger = this._createLogger(); 

      var binding = new org.eclipse.rap.clientscripting.EventBinding( text, "KeyDown", logger );
      binding.dispose();
      TestUtil.press( text, "A" );

      assertEquals( 0, logger.log.length );
      assertNull( binding._source );
      assertNull( binding._targetFunction );
      text.destroy();
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
    }

  }
  
} );