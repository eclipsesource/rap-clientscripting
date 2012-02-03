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

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.rwt.testfixture.Fixture;


public class ClientListenerManager_Test extends TestCase {

  private ClientListenerManager manager;
  private ClientListener listener1;
  private ClientListener listener2;

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
    createListeners();
    manager = ClientListenerManager.getInstance();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testGetInstance() {
    assertNotNull( manager );
  }

  public void testSameInstance() {
    assertSame( manager, ClientListenerManager.getInstance() );
  }

  public void testGetListenersEmpty() {
    assertEquals( 0, manager.getListeners().size() );
  }

  public void testAddListener() {
    manager.addListener( listener1 );

    assertTrue( manager.getListeners().contains( listener1 ) );
  }

  public void testAddListenerTwice() {
    manager.addListener( listener1 );
    manager.addListener( listener1 );

    assertEquals( 1, manager.getListeners().size() );
  }

  public void testAddDifferentListeners() {
    manager.addListener( listener1 );
    manager.addListener( listener2 );

    assertTrue( manager.getListeners().contains( listener1 ) );
    assertTrue( manager.getListeners().contains( listener2 ) );
  }

  public void testAddRemoveListener() {
    manager.addListener( listener1 );
    manager.addListener( listener2 );

    manager.removeListener( listener2 );

    assertTrue( manager.getListeners().contains( listener1 ) );
    assertFalse( manager.getListeners().contains( listener2 ) );
  }

  public void testRemoveListenerTwice() {
    manager.addListener( listener1 );
    manager.addListener( listener2 );

    manager.removeListener( listener2 );
    manager.removeListener( listener2 );

    assertTrue( manager.getListeners().contains( listener1 ) );
  }

  private void createListeners() {
    listener1 = new ClientListener( "code" );
    listener2 = new ClientListener( "code" );
  }

}
