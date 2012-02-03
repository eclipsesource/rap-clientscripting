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
package org.eclipse.rap.clientscripting.internal;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.rwt.jstest.TestContribution;


public class ClientScriptingTestContribution implements TestContribution {

  private static final String PATH_PREFIX = "/org/eclipse/rap/clientscripting/";

  private static final String[] RESOURCES = new String[] {
    "Function_Test.js",
    "EventBinding_Test.js",
    "EventProxy_Test.js",
    "WidgetProxy_Test.js"
  };

  public String getName() {
    return "clientscripting-tests";
  }

  public String[] getResources() {
    String[] result = new String[ RESOURCES.length ];
    for( int i = 0; i < result.length; i++ ) {
      result[ i ] = PATH_PREFIX + RESOURCES[ i ];
    }
    return result;
  }

  public InputStream getResourceAsStream( String resourceName ) throws IOException {
    return ClientScriptingTestContribution.class.getResourceAsStream( resourceName );
  }

}
