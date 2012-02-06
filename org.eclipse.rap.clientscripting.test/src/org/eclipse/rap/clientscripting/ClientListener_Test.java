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

import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.rap.clientscripting.internal.ClientListenerAdapter;
import org.eclipse.rap.clientscripting.internal.ClientListenerBinding;
import org.eclipse.rap.clientscripting.internal.ClientListenerManager;
import org.eclipse.rap.clientscripting.internal.ClientObjectAdapter;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;


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

  public void testBindToRegistersListener() {
    listener.bindTo( shell, ClientListener.MouseDown );

    assertTrue( ClientListenerManager.getInstance().getListeners().contains( listener ) );
  }

  public void testBindToFailsAfterDispose() {
    listener.dispose();

    try {
      listener.bindTo( shell, SWT.MouseDown );
      fail();
    } catch( IllegalStateException exception ) {
      // expected
    }
  }

  public void testBindToFailsWithNullWidget() {
    try {
      listener.bindTo( null, SWT.MouseDown );
      fail();
    } catch( NullPointerException exception ) {
      assertEquals( "widget is null", exception.getMessage() );
    }
  }

  public void testBindToFailsWithDisposedWidget() {
    Label label = new Label( shell, SWT.NONE );
    label.dispose();

    try {
      listener.bindTo( label, SWT.MouseDown );
      fail();
    } catch( IllegalArgumentException exception ) {
      assertEquals( "Widget is disposed", exception.getMessage() );
    }
  }

  public void testBindToAddsBindingToList() {
    Label label = new Label( shell, SWT.NONE );
    listener.bindTo( label, SWT.MouseDown );

    assertNotNull( findBinding( listener, label, SWT.MouseDown ) );
  }

  public void testBindingNotDisposedByDefault() {
    Label label = new Label( shell, SWT.NONE );
    listener.bindTo( label, SWT.MouseDown );
    
    ClientListenerBinding binding = findBinding( listener, label, SWT.MouseDown );
    assertFalse( binding.isDisposed() );
  }

  public void testBindingDisposedWhenWidgetDisposed() {
    Label label = new Label( shell, SWT.NONE );
    listener.bindTo( label, SWT.MouseDown );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    label.dispose();
    
    ClientListenerBinding binding = findBinding( listener, label, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
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
    ClientObjectAdapter adapter = listener.getAdapter( ClientObjectAdapter.class );

    assertNotNull( adapter );
  }

  public void testGetClientObjectAdapter_sameInstance() {
    ClientObjectAdapter adapter = listener.getAdapter( ClientObjectAdapter.class );

    assertSame( adapter, listener.getAdapter( ClientObjectAdapter.class ) );
  }

  public void testGetClientObjectAdapter_differentInstances() {
    ClientListener listener1 = new ClientListener( "code" );
    ClientListener listener2 = new ClientListener( "code" );

    ClientObjectAdapter adapter1 = listener1.getAdapter( ClientObjectAdapter.class );
    ClientObjectAdapter adapter2 = listener2.getAdapter( ClientObjectAdapter.class );

    assertNotSame( adapter1, adapter2 );
  }

  public void testBindTo() {
    listener.bindTo( shell, SWT.KeyDown );

    ClientListenerAdapter adapter = listener.getAdapter( ClientListenerAdapter.class );
    ClientListenerBinding wanted = new ClientListenerBinding( shell, SWT.KeyDown, listener );
    assertTrue( adapter.getBindings().contains( wanted ) );
  }

  public void testBindToTwice() {
    listener.bindTo( shell, SWT.KeyDown );
    listener.bindTo( shell, SWT.KeyDown );

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
  
  private static ClientListenerBinding findBinding( ClientListener listener,
                                                    Widget widget,
                                                    int eventType )
  {
    ClientListenerAdapter adapter = listener.getAdapter( ClientListenerAdapter.class );
    Collection<ClientListenerBinding> bindings = adapter.getBindings();
    for( ClientListenerBinding binding : bindings ) {
      if( binding.getWidget() == widget && binding.getEventType() == eventType  ) {
        return binding;
      }
    }
    return null;
  }

}
