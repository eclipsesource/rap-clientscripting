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

import org.eclipse.rwt.RWT;


public class ObjectIdGenerator {

  private static final String ATTR_NEXT_ID = ObjectIdGenerator.class.getName().concat( ".nextId" );

  private ObjectIdGenerator() {
  }

  public static String getNextId() {
    Integer nextId = ( Integer )RWT.getSessionStore().getAttribute( ATTR_NEXT_ID );
    if( nextId == null ) {
      nextId = Integer.valueOf( 1 );
    }
    RWT.getSessionStore().setAttribute( ATTR_NEXT_ID, Integer.valueOf( nextId.intValue() + 1 ) );
    return "cs" + nextId.toString();
  }
}
