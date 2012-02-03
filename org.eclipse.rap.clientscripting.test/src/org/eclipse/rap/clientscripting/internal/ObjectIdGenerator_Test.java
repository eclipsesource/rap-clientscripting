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

import junit.framework.TestCase;

import org.eclipse.rap.rwt.testfixture.Fixture;


public class ObjectIdGenerator_Test extends TestCase {

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testGetId() {
    assertEquals( "cs1", ObjectIdGenerator.getNextId() );
  }

  public void testGetIdTwice() {
    assertEquals( "cs1", ObjectIdGenerator.getNextId() );
    assertEquals( "cs2", ObjectIdGenerator.getNextId() );
  }

}
