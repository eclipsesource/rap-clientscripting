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
import org.eclipse.swt.widgets.Label;


public class ClientListener {

  public static final int MouseDown = SWT.MouseDown;
  public static final int MouseUp = SWT.MouseUp;

  public ClientListener( String scriptCode ) {
    if( scriptCode == null ) {
      throw new NullPointerException( "Parameter is null: scriptCode" );
    }
  }

  public void bindTo( Label label, int eventType ) {
  }

}
