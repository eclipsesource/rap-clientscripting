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
import java.util.Collection;
import java.util.Collections;

import org.eclipse.rap.clientscripting.internal.ClientListenerAdapter;
import org.eclipse.rap.clientscripting.internal.ClientListenerBinding;
import org.eclipse.rap.clientscripting.internal.ClientListenerManager;
import org.eclipse.rap.clientscripting.internal.IClientObjectAdapter2;
import org.eclipse.rap.clientscripting.internal.ClientObjectAdapterImpl;
import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Widget;


@SuppressWarnings( { "serial" , "restriction" } )
public class ClientListener implements Adaptable {

  public static final int KeyDown = SWT.KeyDown;
  public static final int KeyUp = SWT.KeyUp;
  public static final int MouseDown = SWT.MouseDown;
  public static final int MouseUp = SWT.MouseUp;
  public static final int MouseMove = 5;
  public static final int MouseEnter = 6;
  public static final int MouseExit = 7;
  public static final int MouseDoubleClick = SWT.MouseDoubleClick;
  public static final int FocusIn = SWT.FocusIn;
  public static final int FocusOut = SWT.FocusOut;
  public static final int Modify = SWT.Modify;
  public static final int Verify = SWT.Verify;

  private final String scriptCode;
  private boolean disposed;
  private IClientObjectAdapter2 iClientObjectAdapter2;
  private ClientListenerAdapter clientListenerAdapter;
  protected Collection<ClientListenerBinding> bindings;

  public ClientListener( String scriptCode ) {
    this.scriptCode = scriptCode;
    disposed = false;
    bindings = new ArrayList<ClientListenerBinding>();
    if( scriptCode == null ) {
      throw new NullPointerException( "Parameter is null: scriptCode" );
    }
  }

  public void addTo( Widget widget, int eventType ) {
    if( disposed ) {
      throw new IllegalStateException( "ClientListener is disposed" );
    }
    if( widget == null ) {
      throw new NullPointerException( "widget is null" );
    }
    if( widget.isDisposed() ) {
      throw new IllegalArgumentException( "Widget is disposed" );
    }
    final ClientListenerBinding binding = new ClientListenerBinding( widget, eventType, this );
    addBinding( binding );
    ClientListenerManager.getInstance().addListener( this );
  }

  public void removeFrom( Widget widget, int eventType ) {
    if( disposed ) {
      throw new IllegalStateException( "ClientListener is disposed" );
    }
    if( widget == null ) {
      throw new NullPointerException( "widget is null" );
    }
    ClientListenerBinding binding = findBinding( widget, eventType );
    if( binding != null ) {
      binding.markDisposed();
    }
  }

  public void dispose() {
    disposed = true;
  }

  public boolean isDisposed() {
    return disposed;
  }

  /**
   * Implementation of the <code>Adaptable</code> interface.
   * <p>
   * <strong>IMPORTANT:</strong> This method is <em>not</em> part of the public API. It should never
   * be accessed from application code.
   * </p>
   */
  @SuppressWarnings( "unchecked" )
  public <T> T getAdapter( Class<T> adapter ) {
    T result = null;
    if( adapter == IClientObjectAdapter2.class || adapter == IClientObjectAdapter.class ) {
      if( iClientObjectAdapter2 == null ) {
        iClientObjectAdapter2 = new ClientObjectAdapterImpl();
      }
      result = ( T )iClientObjectAdapter2;
    } else if( adapter == ClientListenerAdapter.class ) {
      if( clientListenerAdapter == null ) {
        clientListenerAdapter = createClientListenerAdapter();
      }
      result = ( T )clientListenerAdapter;
    }
    return result;
  }

  private void addBinding( final ClientListenerBinding binding ) {
    if( !bindings.contains( binding ) ) {
      bindings.add( binding );
      binding.getWidget().addDisposeListener( new DisposeListener() {
        public void widgetDisposed( DisposeEvent event ) {
          binding.markDisposed();
        }
      } );
    }
  }

  private ClientListenerBinding findBinding( Widget widget, int eventType ) {
    for( ClientListenerBinding binding : bindings ) {
      if( binding.getWidget() == widget && binding.getEventType() == eventType ) {
        return binding;
      }
    }
    return null;
  }

  private ClientListenerAdapter createClientListenerAdapter() {
    return new ClientListenerAdapter() {

      public Collection<ClientListenerBinding> getBindings() {
        return Collections.unmodifiableCollection( bindings );
      }

      public String getScriptCode() {
        return scriptCode;
      }

      public void removeDisposedBindings() {
        ArrayList<ClientListenerBinding> toRemove = new ArrayList<ClientListenerBinding>();
        for( ClientListenerBinding binding : bindings ) {
          if( binding.isDisposed() ) {
            toRemove.add( binding );
          }
        }
        for( ClientListenerBinding binding : toRemove ) {
          bindings.remove( binding );
        }
      }

    };
  }
}
