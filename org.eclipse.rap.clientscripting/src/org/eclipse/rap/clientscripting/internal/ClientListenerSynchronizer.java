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

import java.util.Map;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rwt.internal.protocol.IClientObject;
import org.eclipse.rwt.internal.theme.JsonObject;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Widget;


@SuppressWarnings( "restriction" )
public class ClientListenerSynchronizer implements Synchronizer<ClientListener> {

  private static final String TYPE = "rwt.clientscripting.Listener";

  public void renderCreate( ClientListener listener, IClientObject clientObject ) {
    clientObject.create( TYPE );
    ClientListenerAdapter adapter = listener.getAdapter( ClientListenerAdapter.class );
    clientObject.set( "code", adapter.getScriptCode() );
    clientObject.set( "scope", getContext( listener ) );
  }

  private JsonObject getContext( ClientListener listener ) {
    JsonObject result = new JsonObject();
    Map<String, Object> context = listener.getContext();
    Object[] keys = context.keySet().toArray();
    for( int i = 0; i < keys.length; i++ ) {
      String key = ( String )keys[ i ];
      Object object = context.get( key );
      if( object instanceof Widget ) {
        JsonObject value = new JsonObject();
        value.append( "id", WidgetUtil.getId( ( Widget )object ) );
        result.append( key, value );
      } else if( object instanceof Integer ) {
        result.append( key, ( ( Integer )object ) );
      } else if( object instanceof String ) {
        result.append( key, ( String )object );
      } else {
        throw new IllegalArgumentException( "Context does not support " + object.getClass() );
      }
    }
    return result;
  }

}
