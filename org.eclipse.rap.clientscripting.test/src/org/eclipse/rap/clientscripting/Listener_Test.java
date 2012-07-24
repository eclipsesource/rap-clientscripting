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

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.rap.clientscripting.internal.ClientListenerAdapter;
import org.eclipse.rap.clientscripting.internal.ClientListenerBinding;
import org.eclipse.rap.clientscripting.test.TestUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;


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
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };

    listener.addTo( shell, SWT.MouseDown );

    ClientListener result = listener.getClientListener();
    ClientListenerAdapter adapter = result.getAdapter( ClientListenerAdapter.class );
    assertEquals( 1, adapter.getBindings().size() );
    assertEquals( "function handleEvent(){}", adapter.getScriptCode() );
    listener.removeTo( shell, SWT.MouseDown );
  }

  public void testRemoveListner() {
    Listener listener = new Listener(){
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };
    listener.addTo( shell, SWT.MouseDown );

    listener.removeTo( shell, SWT.MouseDown );

    ClientListener clientListener = listener.getClientListener();
    ClientListenerBinding binding = TestUtil.findBinding( clientListener, shell, SWT.MouseDown );
    assertTrue( binding.isDisposed() );
  }

  public void testDisposeListner() {
    Listener listener = new Listener(){
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };
    listener.addTo( shell, SWT.MouseDown );
    ClientListener clientListener = listener.getClientListener();

    listener.dispose();

    assertTrue( clientListener.isDisposed() );
  }

  public void testListnerWithIntField() {
    Listener listener = new Listener(){
      @SuppressWarnings("unused")
      private int test = 23;
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };

    listener.addTo( shell, SWT.MouseDown );

    Map<String, Object> context = getContext( listener );
    assertEquals( 23, ( ( Integer )context.get( "test" ) ).intValue() );
  }

  public void testListnerWithStringField() {
    Listener listener = new Listener(){
      @SuppressWarnings("unused")
      private String test = "foo";
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };

    listener.addTo( shell, SWT.MouseDown );

    Map<String, Object> context = getContext( listener );
    assertEquals( "foo", ( ( String )context.get( "test" ) ) );
  }

  public void testListnerWithWidgetField() {
    Listener listener = new Listener(){
      @SuppressWarnings("unused")
      private Shell test = shell;
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };

    listener.addTo( shell, SWT.MouseDown );

    Map<String, Object> context = getContext( listener );
    assertEquals( shell, ( ( Widget )context.get( "test" ) ) );
  }
  
  public void testListnerUnspupportedPrimitiveField() {
    Listener listener = new Listener(){
      @SuppressWarnings("unused")
      private float test = -234;
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };
    try {
      listener.addTo( shell, SWT.MouseDown );
      fail();
    } catch( RuntimeException ex ) {
      // expected
    }
  }
  
  public void testListnerUnspupportedObjectField() {
    Listener listener = new Listener(){
      @SuppressWarnings("unused")
      private Date test = new Date();
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };
    try {
      listener.addTo( shell, SWT.MouseDown );
      fail();
    } catch( RuntimeException ex ) {
      // expected
    }
  }

  public void testListnerFieldMustBePrivate() {
    Listener listener = new Listener(){
      @SuppressWarnings("unused")
      int test = 23;
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };

    try {
      listener.addTo( shell, SWT.MouseDown );
      fail();
    } catch( RuntimeException ex ) {
      //expected
    }
  }

  public void testPublicMethodNotAllowed() {
    Listener listener = new Listener(){
      @SuppressWarnings("unused")
      public void test(){}
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };

    try {
      listener.addTo( shell, SWT.MouseDown );
      fail();
    } catch( RuntimeException ex ) {
      //expected
    }
  }

  public void testListnerWithFinalField() {
    Listener listener = new Listener(){
      @SuppressWarnings("unused")
      final private int test = 23;
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };

    try {
      listener.addTo( shell, SWT.MouseDown );
      fail();
    } catch( RuntimeException ex ) {
      //expected
    }
  }

  public void testListnerWithPrivateMethod() {
    final ArrayList<Boolean> log = new ArrayList<Boolean>();
    Listener listener = new Listener(){
      @SuppressWarnings("unused")
      private void someMethod(){
        log.add( Boolean.TRUE );
      }
      public String getClientImpl() {
        return "function handleEvent(){}";
      }
    };

    listener.addTo( shell, SWT.MouseDown );

    Map<String, Object> context = getContext( listener );
    JavaFunction func = ( JavaFunction )context.get( "someMethod" );
    func.execute( null );
    assertEquals( 1, log.size() );
  }

  private Map<String, Object> getContext( Listener listener ) {
    Map<String, Object> context = listener.getClientListener().getContext();
    return context;
  }

  private void createWidgets() {
    display = new Display();
    shell = new Shell( display );
  }

}
