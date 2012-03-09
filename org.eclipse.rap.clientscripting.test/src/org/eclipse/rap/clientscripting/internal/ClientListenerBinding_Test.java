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


public class ClientListenerBinding_Test extends TestCase {

  private Shell shell;
  private Display display;
  private Label label1;
  private Label label2;
  private ClientListener listener1;
  private ClientListener listener2;
  private ClientListenerBinding binding;
  private ClientListenerBinding equalBinding;
  private ClientListenerBinding bindingWithDifferentWidget;
  private ClientListenerBinding bindingWithDifferentEvent;
  private ClientListenerBinding bindingWithDifferentListener;

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
    createWidgets();
    createListeners();
    createBindingss();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testEquals() {
    assertTrue( binding.equals( equalBinding ) );
    assertFalse( binding.equals( bindingWithDifferentWidget ) );
    assertFalse( binding.equals( bindingWithDifferentEvent ) );
    assertFalse( binding.equals( bindingWithDifferentListener ) );
  }

  public void testHashCode() {
    assertTrue( binding.hashCode() != 0 );
    assertTrue( binding.hashCode() == equalBinding.hashCode() );
    assertFalse( binding.hashCode() == bindingWithDifferentWidget.hashCode() );
    assertFalse( binding.hashCode() == bindingWithDifferentEvent.hashCode() );
    assertFalse( binding.hashCode() == bindingWithDifferentListener.hashCode() );
  }

  public void testCreate() {
    assertSame( listener1, binding.getListener() );
    assertSame( label1, binding.getWidget() );
    assertEquals( SWT.MouseDown, binding.getEventType() );
  }

  public void testIsDisposed() {
    assertFalse( binding.isDisposed() );
  }

  public void testMarkDisposed() {
    binding.markDisposed();

    assertTrue( binding.isDisposed() );
  }

  public void testGetClientObjectAdapter() {
    IClientObjectAdapter2 adapter = binding.getAdapter( IClientObjectAdapter2.class );

    assertNotNull( adapter );
  }

  public void testGetClientObjectAdapter_sameInstance() {
    IClientObjectAdapter2 adapter = binding.getAdapter( IClientObjectAdapter2.class );

    assertSame( adapter, binding.getAdapter( IClientObjectAdapter2.class ) );
  }

  public void testGetClientObjectAdapter_differentInstances() {
    IClientObjectAdapter2 adapter1 = binding.getAdapter( IClientObjectAdapter2.class );
    IClientObjectAdapter2 adapter2 = bindingWithDifferentEvent.getAdapter( IClientObjectAdapter2.class );

    assertNotSame( adapter1, adapter2 );
  }

  private void createWidgets() {
    display = new Display();
    shell = new Shell( display );
    label1 = new Label( shell, SWT.NONE );
    label2 = new Label( shell, SWT.NONE );
  }

  private void createListeners() {
    listener1 = new ClientListener( "code" );
    listener2 = new ClientListener( "code" );
  }

  private void createBindingss() {
    binding = new ClientListenerBinding( label1, SWT.MouseDown, listener1 );
    equalBinding = new ClientListenerBinding( label1, SWT.MouseDown, listener1 );
    bindingWithDifferentWidget = new ClientListenerBinding( label2, SWT.MouseDown, listener1 );
    bindingWithDifferentEvent = new ClientListenerBinding( label1, SWT.MouseUp, listener1 );
    bindingWithDifferentListener = new ClientListenerBinding( label1, SWT.MouseDown, listener2 );
  }

}
