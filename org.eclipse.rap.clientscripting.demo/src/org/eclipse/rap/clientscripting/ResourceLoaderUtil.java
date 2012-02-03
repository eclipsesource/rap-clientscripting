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
package org.eclipse.rap.clientscripting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ResourceLoaderUtil {

  private static final ClassLoader CLASSLOADER = ResourceLoaderUtil.class.getClassLoader();

  private ResourceLoaderUtil() {
    // prevent instantiation
  }

  public static String readContent( String resource, String charset ) throws IOException {
    StringBuilder builder = new StringBuilder();
    InputStream stream = CLASSLOADER.getResourceAsStream( resource );
    if( stream != null ) {
      InputStreamReader inputStreamReader = new InputStreamReader( stream, charset );
      BufferedReader bufferedReader = new BufferedReader( inputStreamReader );
      try {
        String line = bufferedReader.readLine();
        while( line != null ) {
          builder.append( line );
          builder.append( '\n' );
          line = bufferedReader.readLine();
        }
      } finally {
        bufferedReader.close();
      }
    }
    return builder.toString();
  }
}
