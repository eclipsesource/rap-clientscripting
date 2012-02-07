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

import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.protocol.IClientObject;
import org.eclipse.rwt.lifecycle.WidgetUtil;


@SuppressWarnings( "restriction" )
public class ClientListenerBindingSynchronizer implements Synchronizer<ClientListenerBinding> {

  private static final String TYPE = "rwt.clientscripting.EventBinding";

  public void renderCreate( ClientListenerBinding binding, IClientObject clientObject ) {
    clientObject.create( TYPE );
    clientObject.set( "listener", getId( binding.getListener() ) );
    clientObject.set( "targetObject", WidgetUtil.getId( binding.getWidget() ) );
    clientObject.set( "eventType", binding.getEventType() );
  }

  public void renderDestroy( ClientListenerBinding binding, IClientObject clientObject ) {
    clientObject.destroy();
  }

  private static String getId( Adaptable object ) {
    ClientObjectAdapter adapter = object.getAdapter( ClientObjectAdapter.class );
    return adapter.getId();
  }

}
