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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Widget;


public class ClientListener {

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

  private boolean disposed;

  public ClientListener( String scriptCode ) {

  }

  public void addTo( Widget widget, int eventType ) {
  }

  public void removeFrom( Widget widget, int eventType ) {
  }

  public void dispose() {
    disposed = true;
  }

  public boolean isDisposed() {
    return disposed;
  }

}
