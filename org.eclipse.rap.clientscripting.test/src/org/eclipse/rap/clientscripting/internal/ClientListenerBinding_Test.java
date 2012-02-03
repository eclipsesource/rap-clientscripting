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
  private ClientListenerBinding originalBinding;
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
    assertTrue( originalBinding.equals( equalBinding ) );
    assertFalse( originalBinding.equals( bindingWithDifferentWidget ) );
    assertFalse( originalBinding.equals( bindingWithDifferentEvent ) );
    assertFalse( originalBinding.equals( bindingWithDifferentListener ) );
  }

  public void testHashCode() {
    assertTrue( originalBinding.hashCode() != 0 );
    assertTrue( originalBinding.hashCode() == equalBinding.hashCode() );
    assertFalse( originalBinding.hashCode() == bindingWithDifferentWidget.hashCode() );
    assertFalse( originalBinding.hashCode() == bindingWithDifferentEvent.hashCode() );
    assertFalse( originalBinding.hashCode() == bindingWithDifferentListener.hashCode() );
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
    originalBinding = new ClientListenerBinding( label1, SWT.MouseDown, listener1 );
    equalBinding = new ClientListenerBinding( label1, SWT.MouseDown, listener1 );
    bindingWithDifferentWidget = new ClientListenerBinding( label2, SWT.MouseDown, listener1 );
    bindingWithDifferentEvent = new ClientListenerBinding( label1, SWT.MouseUp, listener1 );
    bindingWithDifferentListener = new ClientListenerBinding( label1, SWT.MouseDown, listener2 );
  }

}
