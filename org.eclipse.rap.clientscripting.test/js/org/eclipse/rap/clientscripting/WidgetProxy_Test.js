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
 
qx.Class.define( "org.eclipse.rap.clientscripting.WidgetProxy_Test", {

  extend : qx.core.Object,
  
  members : {
    
    testCreateTextWidgetProxy : function() {
      var TestUtil = org.eclipse.rwt.test.fixture.TestUtil;
      var text = new org.eclipse.rwt.widgets.Text(); // TODO [tb] : create by protocol !!!
      text.addToDocument();
      TestUtil.flush();

      var widgetProxy = new org.eclipse.rap.clientscripting.WidgetProxy( text );

      assertTrue( widgetProxy instanceof org.eclipse.rap.clientscripting.WidgetProxy );
      text.destroy();
    }

  }
    
} );