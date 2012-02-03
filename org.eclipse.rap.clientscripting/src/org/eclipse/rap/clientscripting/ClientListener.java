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

import org.eclipse.rap.clientscripting.internal.ClientListenerManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Widget;


public class ClientListener {

  public static final int MouseDown = SWT.MouseDown;
  public static final int MouseUp = SWT.MouseUp;
  private boolean disposed;

  public ClientListener( String scriptCode ) {
    disposed = false;
    if( scriptCode == null ) {
      throw new NullPointerException( "Parameter is null: scriptCode" );
    }
  }

  public void bindTo( Widget widget, int eventType ) {
    if( disposed ) {
      throw new IllegalStateException( "ClientListener is disposed" );
    }
    ClientListenerManager.getInstance().addListener( this );
  }

  public void dispose() {
    disposed = true;
  }

  public boolean isDisposed() {
    return disposed;
  }

}
