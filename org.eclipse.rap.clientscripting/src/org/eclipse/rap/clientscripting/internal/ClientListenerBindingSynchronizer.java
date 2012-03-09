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
import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.protocol.IClientObject;
import org.eclipse.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rwt.lifecycle.WidgetUtil;


@SuppressWarnings( "restriction" )
public class ClientListenerBindingSynchronizer implements Synchronizer<ClientListenerBinding> {

  private static final String TYPE = "rwt.clientscripting.EventBinding";

  public void renderCreate( ClientListenerBinding binding, IClientObject clientObject ) {
    clientObject.create( TYPE );
    clientObject.set( "listener", getId( binding.getListener() ) );
    clientObject.set( "targetObject", WidgetUtil.getId( binding.getWidget() ) );
    clientObject.set( "eventType", getEventType( binding ) );
  }

  public void renderDestroy( ClientListenerBinding binding, IClientObject clientObject ) {
    clientObject.destroy();
  }

  private static String getId( Adaptable object ) {
    IClientObjectAdapter adapter = object.getAdapter( IClientObjectAdapter.class );
    return adapter.getId();
  }

  private static String getEventType( ClientListenerBinding binding ) {
    String result = null;
    switch( binding.getEventType() ) {
      case ClientListener.KeyUp:
        result = "KeyUp";
      break;
      case ClientListener.KeyDown:
        result = "KeyDown";
      break;
      case ClientListener.FocusIn:
        result = "FocusIn";
      break;
      case ClientListener.FocusOut:
        result = "FocusOut";
      break;
      case ClientListener.MouseDown:
        result = "MouseDown";
        break;
      case ClientListener.MouseUp:
        result = "MouseUp";
        break;
      case ClientListener.MouseEnter:
        result = "MouseEnter";
        break;
      case ClientListener.MouseExit:
        result = "MouseExit";
        break;
      case ClientListener.MouseMove:
        result = "MouseMove";
        break;
      case ClientListener.MouseDoubleClick:
        result = "MouseDoubleClick";
      break;
      case ClientListener.Modify:
        result = "Modify";
        break;
      case ClientListener.Verify:
        result = "Verify";
      break;
    }
    if( result == null ) {
      throw new IllegalArgumentException( "Unknown Event Type " + binding.getEventType() );
    }
    return result;
  }

}
