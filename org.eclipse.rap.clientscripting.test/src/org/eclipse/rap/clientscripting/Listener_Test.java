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
import org.eclipse.rap.clientscripting.test.TestUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;


public class Listener_Test extends TestCase {

  private Shell shell;
  private Display display;

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
    createWidgets();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testAddListner() {
    Listener listener = new Listener(){
      public void handleEvent( Event event ) {
      }
      public String getJavaScript() {
        return "function handleEvent(){}";
      }
    };

    listener.add( shell, SWT.MouseDown );

    ClientListener result = listener.clientListener;
    ClientListenerAdapter adapter = result.getAdapter( ClientListenerAdapter.class );
    assertEquals( 1, adapter.getBindings().size() );
    assertEquals( "function handleEvent(){}", adapter.getScriptCode() );
    listener.remove( shell, SWT.MouseDown );
  }

  public void testRemoveListner() {
    Listener listener = new Listener(){
      public void handleEvent( Event event ) {
      }
      public String getJavaScript() {
        return "function handleEvent(){}";
      }
    };
    listener.add( shell, SWT.MouseDown );

    listener.remove( shell, SWT.MouseDown );

    ClientListener clientListener = listener.clientListener;
    ClientListenerBinding binding = TestUtil.findBinding( clientListener, shell, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
  }

  public void testDisposeListner() {
    Listener listener = new Listener(){
      public void handleEvent( Event event ) {
      }
      public String getJavaScript() {
        return "function handleEvent(){}";
      }
    };
    listener.add( shell, SWT.MouseDown );
    ClientListener clientListener = listener.clientListener;

    listener.dispose();

    assertTrue( clientListener.isDisposed() );
  }
  
  private void createWidgets() {
    display = new Display();
    shell = new Shell( display );
  }

}
