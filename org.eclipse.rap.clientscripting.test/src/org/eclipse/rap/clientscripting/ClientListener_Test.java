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

import junit.framework.TestCase;

import org.eclipse.rap.clientscripting.internal.ClientListenerAdapter;
import org.eclipse.rap.clientscripting.internal.ClientListenerBinding;
import org.eclipse.rap.clientscripting.internal.ClientListenerManager;
import org.eclipse.rap.clientscripting.internal.IClientObjectAdapter2;
import org.eclipse.rap.clientscripting.test.TestUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class ClientListener_Test extends TestCase {

  private Shell shell;
  private Display display;
  private ClientListener listener;

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
    createWidgets();
    createListener();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testCreateWithNull() {
    try {
      new ClientListener( null );
      fail();
    } catch( NullPointerException expected ) {
    }
  }

  public void testCreateDoesNotRegister() {
    new ClientListener( "code" );

    assertTrue( ClientListenerManager.getInstance().getListeners().isEmpty() );
  }

  public void testIsDisposed() {
    assertFalse( listener.isDisposed() );
  }

  public void testDispose() {
    listener.dispose();

    assertTrue( listener.isDisposed() );
  }

  public void testDisposeTwice() {
    listener.dispose();
    listener.dispose();

    assertTrue( listener.isDisposed() );
  }

  public void testAddToRegistersListener() {
    listener.addTo( shell, ClientListener.MouseDown );

    assertTrue( ClientListenerManager.getInstance().getListeners().contains( listener ) );
  }

  public void testAddToFailsAfterDispose() {
    listener.dispose();

    try {
      listener.addTo( shell, SWT.MouseDown );
      fail();
    } catch( IllegalStateException exception ) {
      assertTrue( exception.getMessage().contains( "disposed" ) );
    }
  }

  public void testAddToFailsWithNullWidget() {
    try {
      listener.addTo( null, SWT.MouseDown );
      fail();
    } catch( NullPointerException exception ) {
      assertEquals( "widget is null", exception.getMessage() );
    }
  }

  public void testAddToFailsWithDisposedWidget() {
    Label label = new Label( shell, SWT.NONE );
    label.dispose();

    try {
      listener.addTo( label, SWT.MouseDown );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertEquals( "Widget is disposed", exception.getMessage() );
    }
  }

  public void testAddToAddsBindingToList() {
    Label label = new Label( shell, SWT.NONE );
    listener.addTo( label, SWT.MouseDown );

    assertNotNull( TestUtil.findBinding( listener, label, SWT.MouseDown ) );
  }

  public void testBindingNotDisposedByDefault() {
    Label label = new Label( shell, SWT.NONE );
    listener.addTo( label, SWT.MouseDown );
    
    ClientListenerBinding binding = TestUtil.findBinding( listener, label, SWT.MouseDown );
    assertFalse( binding.isDisposed() );
  }

  public void testBindingDisposedWhenWidgetDisposed() {
    Label label = new Label( shell, SWT.NONE );
    listener.addTo( label, SWT.MouseDown );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    label.dispose();
    
    ClientListenerBinding binding = TestUtil.findBinding( listener, label, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
  }

  public void testRemoveFromFailsAfterDispose() {
    listener.dispose();
    try {
      listener.removeFrom( shell, SWT.MouseDown );
      fail();
    } catch( IllegalStateException exception ) {
      assertTrue( exception.getMessage().contains( "disposed" ) );
    }
  }

  public void testRemoveFromFailsWithNullWidget() {
    try {
      listener.removeFrom( null, SWT.MouseDown );
      fail();
    } catch( NullPointerException exception ) {
      assertEquals( "widget is null", exception.getMessage() );
    }
  }

  public void testRemoveFromDisposesBinding() {
    Label label = new Label( shell, SWT.NONE );
    listener.addTo( label, SWT.MouseDown );
    listener.removeFrom( label, SWT.MouseDown );
    
    ClientListenerBinding binding = TestUtil.findBinding( listener, label, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
  }

  public void testRemoveFromCalledTwice() {
    Label label = new Label( shell, SWT.NONE );
    listener.addTo( label, SWT.MouseDown );
    listener.removeFrom( label, SWT.MouseDown );
    listener.removeFrom( label, SWT.MouseDown );
    
    ClientListenerBinding binding = TestUtil.findBinding( listener, label, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
  }

  public void testRemoveFromNonExistingBinding() {
    Label label = new Label( shell, SWT.NONE );
    listener.removeFrom( label, SWT.MouseDown );

    assertNull( TestUtil.findBinding( listener, label, SWT.MouseDown ) );
  }
  
  public void testGetClientListenerAdapter() {
    ClientListenerAdapter adapter = listener.getAdapter( ClientListenerAdapter.class );

    assertNotNull( adapter );
  }

  public void testGetClientListenerAdapter_sameInstance() {
    ClientListenerAdapter adapter = listener.getAdapter( ClientListenerAdapter.class );

    assertSame( adapter, listener.getAdapter( ClientListenerAdapter.class ) );
  }

  public void testGetClientListenerAdapter_scriptCode() {
    ClientListenerAdapter adapter = listener.getAdapter( ClientListenerAdapter.class );

    assertEquals( "code", adapter.getScriptCode() );
  }

  public void testGetClientObjectAdapter() {
    IClientObjectAdapter2 adapter = listener.getAdapter( IClientObjectAdapter2.class );

    assertNotNull( adapter );
  }

  public void testGetClientObjectAdapter_sameInstance() {
    IClientObjectAdapter2 adapter = listener.getAdapter( IClientObjectAdapter2.class );

    assertSame( adapter, listener.getAdapter( IClientObjectAdapter2.class ) );
  }

  public void testGetClientObjectAdapter_differentInstances() {
    ClientListener listener1 = new ClientListener( "code" );
    ClientListener listener2 = new ClientListener( "code" );

    IClientObjectAdapter2 adapter1 = listener1.getAdapter( IClientObjectAdapter2.class );
    IClientObjectAdapter2 adapter2 = listener2.getAdapter( IClientObjectAdapter2.class );

    assertNotSame( adapter1, adapter2 );
  }

  public void testAddTo() {
    listener.addTo( shell, SWT.KeyDown );

    ClientListenerAdapter adapter = listener.getAdapter( ClientListenerAdapter.class );
    ClientListenerBinding wanted = new ClientListenerBinding( shell, SWT.KeyDown, listener );
    assertTrue( adapter.getBindings().contains( wanted ) );
  }

  public void testAddToTwice() {
    listener.addTo( shell, SWT.KeyDown );
    listener.addTo( shell, SWT.KeyDown );

    ClientListenerAdapter adapter = listener.getAdapter( ClientListenerAdapter.class );
    assertEquals( 1, adapter.getBindings().size() );
  }

  private void createWidgets() {
    display = new Display();
    shell = new Shell( display );
  }

  private void createListener() {
    listener = new ClientListener( "code" );
  }

}
