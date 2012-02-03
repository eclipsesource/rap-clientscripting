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

import java.util.Collection;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.protocol.ClientObject;
import org.eclipse.rwt.internal.protocol.IClientObject;
import org.eclipse.rwt.lifecycle.PhaseEvent;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.rwt.lifecycle.PhaseListener;
import org.eclipse.rwt.service.IApplicationStore;


@SuppressWarnings( { "restriction", "serial" } )
final class ClientScriptingRenderer {

  private static final String ATTR_RENDERING_PHASE_LISTENER
    = ClientScriptingRenderer.class.getName().concat( "#renderingPhaseListener" );
  private final static ClientListenerSynchronizer clientListenerSynchronizer;
  private final static ClientListenerBindingSynchronizer clientListenerBindingSynchronizer;
  private final static Object lock = new Object();

  static {
    clientListenerSynchronizer = new ClientListenerSynchronizer();
    clientListenerBindingSynchronizer = new ClientListenerBindingSynchronizer();
  }

  static void registerPhaseListener() {
    PhaseListener phaseListener;
    IApplicationStore store = RWT.getApplicationStore();
    synchronized( lock ) {
      phaseListener = ( PhaseListener )store.getAttribute( ATTR_RENDERING_PHASE_LISTENER );
      if( phaseListener == null ) {
        phaseListener = new RenderPhaseListener();
        RWT.getLifeCycle().addPhaseListener( phaseListener );
        store.setAttribute( ATTR_RENDERING_PHASE_LISTENER, phaseListener );
      }
    }
  }

  static void render() {
    ClientListenerManager manager = ClientListenerManager.getInstance();
    Collection<ClientListener> listeners = manager.getListeners();
    for( ClientListener listener : listeners ) {
      render( listener );
      ClientListenerAdapter listenerAdapter = listener.getAdapter( ClientListenerAdapter.class );
      Collection<ClientListenerBinding> bindings = listenerAdapter.getBindings();
      for( ClientListenerBinding binding : bindings ) {
        render( binding );
      }
    }
  }

  private static void render( ClientListener listener ) {
    ClientObjectAdapter adapter = listener.getAdapter( ClientObjectAdapter.class );
    if( !adapter.isCreated() ) {
      IClientObject clientObject = new ClientObject( adapter.getId() );
      clientListenerSynchronizer.renderCreate( listener, clientObject );
      adapter.setCreated();
    }
  }

  private static void render( ClientListenerBinding binding ) {
    ClientObjectAdapter adapter = binding.getAdapter( ClientObjectAdapter.class );
    if( !adapter.isCreated() ) {
      IClientObject clientObject = new ClientObject( adapter.getId() );
      clientListenerBindingSynchronizer.renderCreate( binding, clientObject );
      adapter.setCreated();
    }
  }

  private static class RenderPhaseListener implements PhaseListener {

    public PhaseId getPhaseId() {
      return PhaseId.RENDER;
    }

    public void beforePhase( PhaseEvent event ) {
    }

    public void afterPhase( PhaseEvent event ) {
      render();
    }
  }
}
