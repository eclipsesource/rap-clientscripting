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

import org.eclipse.rap.rwt.testfixture.Fixture;
import junit.framework.TestCase;


public class ClientObjectAdapterImpl_Test extends TestCase {

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testGetId() {
    ClientObjectAdapter adapter = new ClientObjectAdapterImpl();

    assertNotNull( adapter.getId() );
    assertTrue( adapter.getId().length() > 0 );
  }

  public void testDifferentIds() {
    ClientObjectAdapter adapter1 = new ClientObjectAdapterImpl();
    ClientObjectAdapter adapter2 = new ClientObjectAdapterImpl();

    assertFalse( adapter1.getId().equals( adapter2.getId() ) );
  }

  public void testIsCreated() {
    ClientObjectAdapter adapter = new ClientObjectAdapterImpl();

    assertFalse( adapter.isCreated() );
  }

  public void testSetCreated() {
    ClientObjectAdapter adapter = new ClientObjectAdapterImpl();

    adapter.setCreated();

    assertTrue( adapter.isCreated() );
  }

}
