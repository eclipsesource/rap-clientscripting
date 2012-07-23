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
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.protocol.ClientObjectFactory;
import org.eclipse.rap.rwt.internal.protocol.IClientObject;
import org.eclipse.rap.rwt.lifecycle.PhaseEvent;
import org.eclipse.rap.rwt.lifecycle.PhaseId;
import org.eclipse.rap.rwt.lifecycle.PhaseListener;
import org.eclipse.rap.rwt.service.IApplicationStore;


@SuppressWarnings( { "restriction", "serial" } )
final class ClientScriptingRenderer {

  private static final String ATTR_RENDERING_PHASE_LISTENER
    = ClientScriptingRenderer.class.getName().concat( "#renderingPhaseListener" );
  private static final String ATTR_READ_PHASE_LISTENER
  = ClientScriptingRenderer.class.getName().concat( "#readDataPhaseListener" );
  private final static ClientListenerSynchronizer clientListenerSynchronizer;
  private final static ClientListenerBindingSynchronizer clientListenerBindingSynchronizer;
  private final static Object lock = new Object();

  static {
    clientListenerSynchronizer = new ClientListenerSynchronizer();
    clientListenerBindingSynchronizer = new ClientListenerBindingSynchronizer();
  }

  static void registerPhaseListener() {
    PhaseListener renderPhaseListener;
    PhaseListener readDataPhaseListener;
    IApplicationStore store = RWT.getApplicationStore();
    synchronized( lock ) {
      renderPhaseListener = ( PhaseListener )store.getAttribute( ATTR_RENDERING_PHASE_LISTENER );
      if( renderPhaseListener == null ) {
        renderPhaseListener = new RenderPhaseListener();
        RWT.getLifeCycle().addPhaseListener( renderPhaseListener );
        store.setAttribute( ATTR_RENDERING_PHASE_LISTENER, renderPhaseListener );
      }
      readDataPhaseListener = ( PhaseListener )store.getAttribute( ATTR_READ_PHASE_LISTENER );
      if( readDataPhaseListener == null ) {
        readDataPhaseListener = new ReadDataPhaseListener();
        RWT.getLifeCycle().addPhaseListener( readDataPhaseListener );
        store.setAttribute( ATTR_READ_PHASE_LISTENER, readDataPhaseListener );
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
      listenerAdapter.removeDisposedBindings();
    }
  }

  private static void render( ClientListener listener ) {
    IClientObjectAdapter2 adapter = listener.getAdapter( IClientObjectAdapter2.class );
    if( !adapter.isCreated() ) {
      IClientObject clientObject = ClientObjectFactory.getClientObject( listener );
      clientListenerSynchronizer.renderCreate( listener, clientObject );
      adapter.setCreated();
    }
  }

  static void readData() {
    ClientListenerManager manager = ClientListenerManager.getInstance();
    Collection<ClientListener> listeners = manager.getListeners();
    for( ClientListener listener : listeners ) {
      clientListenerSynchronizer.readData( listener );
    }
  }

  private static void render( ClientListenerBinding binding ) {
    IClientObjectAdapter2 adapter = binding.getAdapter( IClientObjectAdapter2.class );
    if( !adapter.isCreated() ) {
      IClientObject clientObject = ClientObjectFactory.getClientObject( binding );
      clientListenerBindingSynchronizer.renderCreate( binding, clientObject );
      adapter.setCreated();
    }
    if( binding.isDisposed() ) {
      IClientObject clientObject = ClientObjectFactory.getClientObject( binding );
      clientListenerBindingSynchronizer.renderDestroy( binding, clientObject );
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

  private static class ReadDataPhaseListener implements PhaseListener {

    public PhaseId getPhaseId() {
      return PhaseId.READ_DATA;
    }

    public void beforePhase( PhaseEvent event ) {
    }

    public void afterPhase( PhaseEvent event ) {
      readData();
    }
  }

}
