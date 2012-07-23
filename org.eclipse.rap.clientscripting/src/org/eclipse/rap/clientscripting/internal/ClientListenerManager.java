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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.rwt.SessionSingletonBase;


public class ClientListenerManager {

  private final ArrayList<ClientListener> listeners;

  private ClientListenerManager() {
    listeners = new ArrayList<ClientListener>();
    // TODO [rst] This is provisional, think about proper initialization hook for renderer
    ClientScriptingRenderer.registerPhaseListener();
  }

  public static ClientListenerManager getInstance() {
    return SessionSingletonBase.getInstance( ClientListenerManager.class );
  }

  public void addListener( ClientListener listener ) {
    if( !listeners.contains( listener ) ) {
      listeners.add( listener );
    }
  }

  public void removeListener( ClientListener listener ) {
    listeners.remove( listener );
  }

  public Collection<ClientListener> getListeners() {
    return Collections.unmodifiableCollection( listeners );
  }
}
