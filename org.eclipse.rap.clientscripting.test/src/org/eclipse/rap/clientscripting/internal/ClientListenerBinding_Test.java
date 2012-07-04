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

import java.util.HashMap;
import java.util.Map;

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
  private ClientListenerBinding bindingWithDifferentContext;
  private HashMap<String, Object> context1;
  private HashMap<String, Object> context2;

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
    createWidgets();
    createContexts();
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
    assertFalse( binding.equals( bindingWithDifferentContext ) );
  }

  public void testHashCode() {
    assertTrue( binding.hashCode() != 0 );
    assertTrue( binding.hashCode() == equalBinding.hashCode() );
    assertFalse( binding.hashCode() == bindingWithDifferentWidget.hashCode() );
    assertFalse( binding.hashCode() == bindingWithDifferentEvent.hashCode() );
    assertFalse( binding.hashCode() == bindingWithDifferentListener.hashCode() );
    assertFalse( binding.hashCode() == bindingWithDifferentContext.hashCode() );
  }

  public void testCreate() {
    assertSame( listener1, binding.getListener() );
    assertSame( label1, binding.getWidget() );
    assertEquals( SWT.MouseDown, binding.getEventType() );
    assertEquals( context1, binding.getContext() );
  }

  public void testContextSaveCopyOnCreate() {
    Map<String, Object> actualContext = binding.getContext();

    context1.put( "myField", label1 );

    assertNull( actualContext.get( "myField" ) );
    assertFalse( context1.equals( actualContext ) );
  }

  public void testContextSaveCopyOnGet() {
    Map<String, Object> actualContext = binding.getContext();

    actualContext.put( "myField", label1 );
    Map<String, Object> newActualContext = binding.getContext();

    assertNull( newActualContext.get( "myField" ) );
    assertFalse( newActualContext.equals( actualContext ) );
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

  private void createContexts() {
    context1 = new HashMap< String, Object >();
    context2 = new HashMap< String, Object >();
    context2.put( "myField", label2 );
  }

  private void createBindingss() {
    binding = new ClientListenerBinding( label1, SWT.MouseDown, listener1, context1 );
    equalBinding = new ClientListenerBinding( label1, SWT.MouseDown, listener1, context1 );
    bindingWithDifferentWidget 
      = new ClientListenerBinding( label2, SWT.MouseDown, listener1, context1 );
    bindingWithDifferentEvent 
      = new ClientListenerBinding( label1, SWT.MouseUp, listener1, context1 );
    bindingWithDifferentListener 
      = new ClientListenerBinding( label1, SWT.MouseDown, listener2, context1 );
    bindingWithDifferentContext 
      = new ClientListenerBinding( label1, SWT.MouseDown, listener1, context2 );
  }

}
