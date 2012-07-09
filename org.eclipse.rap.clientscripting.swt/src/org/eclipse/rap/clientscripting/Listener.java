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

  public void addTo( Widget widget, int type ) {
    widget.addListener( type, this );
  }

  public void removeTo( Widget widget, int type ) {
    widget.removeListener( type, this );
  }

  public abstract String getClientImpl();

  public void dispose() {}

}
