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

import org.eclipse.swt.widgets.Widget;

public abstract class Listener implements org.eclipse.swt.widgets.Listener {

  ClientListener clientListener;

  public Listener() {
    clientListener = new ClientListener( this.getJavaScript() );
  }

  public void add( Widget widget, int type ) {
    clientListener.addTo( widget, type );
  }

  public void remove( Widget widget, int type ) {
    clientListener.removeFrom( widget, type );
  }

  public abstract String getJavaScript();

  public void dispose() {
    clientListener.dispose();
  }


}
