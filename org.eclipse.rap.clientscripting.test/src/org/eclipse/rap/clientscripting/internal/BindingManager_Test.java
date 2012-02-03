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

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import junit.framework.TestCase;


public class BindingManager_Test extends TestCase {

  private BindingManager manager;
  private Shell shell;
  private Display display;
  private Label label1;
  private Label label2;
  private ClientListener listener;

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
    createWidgets();
    createListeners();
    manager = BindingManager.getInstance();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testGetInstance() {
    assertNotNull( manager );
  }

  public void testSameInstance() {
    assertSame( manager, BindingManager.getInstance() );
  }

  public void testGetBindingsEmpty() {
    assertEquals( 0, manager.getBindings().size() );
  }

  public void testAddBinding() {
    ClientListenerBinding binding = new ClientListenerBinding( label1, SWT.MouseDown, listener );
    manager.addBinding( binding );

    assertTrue( manager.getBindings().contains( binding ) );
  }

  public void testAddBindingTwice() {
    ClientListenerBinding binding = new ClientListenerBinding( label1, SWT.MouseDown, listener );
    manager.addBinding( binding );
    manager.addBinding( binding );

    assertEquals( 1, manager.getBindings().size() );
    assertTrue( manager.getBindings().contains( binding ) );
  }

  public void testAddDifferentBindings() {
    ClientListenerBinding binding1 = new ClientListenerBinding( label1, SWT.MouseDown, listener );
    ClientListenerBinding binding2 = new ClientListenerBinding( label2, SWT.MouseDown, listener );
    manager.addBinding( binding1 );
    manager.addBinding( binding2 );

    assertTrue( manager.getBindings().contains( binding1 ) );
    assertTrue( manager.getBindings().contains( binding2 ) );
  }

  public void testAddRemoveBinding() {
    ClientListenerBinding binding = new ClientListenerBinding( label1, SWT.MouseDown, listener );
    manager.addBinding( binding );
    manager.removeBinding( binding );

    assertEquals( 0, manager.getBindings().size() );
  }

  public void testRemoveBindingTwice() {
    ClientListenerBinding binding = new ClientListenerBinding( label1, SWT.MouseDown, listener );
    manager.addBinding( binding );
    manager.removeBinding( binding );
    manager.removeBinding( binding );

    assertEquals( 0, manager.getBindings().size() );
  }

  public void testRemoveOneOfTwoBindings() {
    ClientListenerBinding binding1 = new ClientListenerBinding( label1, SWT.MouseDown, listener );
    ClientListenerBinding binding2 = new ClientListenerBinding( label2, SWT.MouseDown, listener );
    manager.addBinding( binding1 );
    manager.addBinding( binding2 );
    manager.removeBinding( binding1 );

    assertFalse( manager.getBindings().contains( binding1 ) );
    assertTrue( manager.getBindings().contains( binding2 ) );
  }

  private void createWidgets() {
    display = new Display();
    shell = new Shell( display );
    label1 = new Label( shell, SWT.NONE );
    label2 = new Label( shell, SWT.NONE );
  }

  private void createListeners() {
    listener = new ClientListener( "code" );
  }

}
