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
import org.eclipse.rwt.Adaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Widget;


public class ClientListener implements Adaptable {

  public static final int MouseDown = SWT.MouseDown;
  public static final int MouseUp = SWT.MouseUp;
  private boolean disposed;
  private ClientListenerAdapter clientListenerAdapter;
  protected Collection<ClientListenerBinding> bindings;

  public ClientListener( String scriptCode ) {
    disposed = false;
    bindings = new ArrayList<ClientListenerBinding>();
    if( scriptCode == null ) {
      throw new NullPointerException( "Parameter is null: scriptCode" );
    }
  }

  public void bindTo( Widget widget, int eventType ) {
    if( disposed ) {
      throw new IllegalStateException( "ClientListener is disposed" );
    }
    ClientListenerBinding binding = new ClientListenerBinding( widget, eventType, this );
    if( !bindings.contains( binding ) ) {
      bindings.add( binding );
    }
    ClientListenerManager.getInstance().addListener( this );
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
    if( adapter == ClientListenerAdapter.class ) {
      if( clientListenerAdapter == null ) {
        clientListenerAdapter = createClientListenerAdapter();
      }
      return ( T )clientListenerAdapter;
    }
    return null;
  }

  private ClientListenerAdapter createClientListenerAdapter() {
    return new ClientListenerAdapter() {

      public Collection<ClientListenerBinding> getBindings() {
        return Collections.unmodifiableCollection( bindings );
      }

    };
  }
}
