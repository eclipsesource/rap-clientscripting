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
package org.eclipse.rap.clientscripting.internal;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rwt.internal.protocol.IClientObject;


@SuppressWarnings( "restriction" )
public class ClientListenerSynchronizer implements Synchronizer<ClientListener> {

  private static final String TYPE = "rwt.clientscripting.Listener";

  public void renderCreate( ClientListener listener, IClientObject clientObject ) {
    clientObject.create( TYPE );
    ClientListenerAdapter adapter = listener.getAdapter( ClientListenerAdapter.class );
    clientObject.set( "code", adapter.getScriptCode() );
  }
}
