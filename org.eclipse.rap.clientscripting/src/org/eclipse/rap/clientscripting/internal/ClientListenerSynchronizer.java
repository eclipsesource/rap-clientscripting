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

import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.clientscripting.JavaFunction;
import org.eclipse.rap.rwt.internal.protocol.IClientObject;
import org.eclipse.rap.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.rap.rwt.internal.service.ContextProvider;
import org.eclipse.rap.rwt.internal.theme.JsonObject;
import org.eclipse.rap.rwt.lifecycle.ProcessActionRunner;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.widgets.Widget;


@SuppressWarnings( "restriction" )
public class ClientListenerSynchronizer implements Synchronizer<ClientListener> {

  private static final String TYPE = "rwt.clientscripting.Listener";
  static final String PARAM_EXECUTE_FUNCTION = "executeFunction";

  public void renderCreate( ClientListener listener, IClientObject clientObject ) {
    clientObject.create( TYPE );
    ClientListenerAdapter adapter = listener.getAdapter( ClientListenerAdapter.class );
    clientObject.set( "code", adapter.getScriptCode() );
    clientObject.set( "scope", getContext( listener ) );
  }

  public void readData( ClientListener listener ) {
    executeFunction( listener );
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
        value.append( "type", "widget" );
        value.append( "id", WidgetUtil.getId( ( Widget )object ) );
        result.append( key, value );
      } else if( object instanceof JavaFunction ) {
        JsonObject value = new JsonObject();
        value.append( "type", "function" );
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
  private static void executeFunction( final ClientListener listener ) {
    String functionName = readPropertyValue( listener, PARAM_EXECUTE_FUNCTION );
    if( functionName != null ) {
      final JavaFunction function = ( JavaFunction )listener.getContext().get( functionName );
      ProcessActionRunner.add( new Runnable() {
        public void run() {
          function.execute( null );
        }
      } );
    }
  }

  private static String readPropertyValue( ClientListener listener, String propertyName ) {
    String id = listener.getAdapter( IClientObjectAdapter.class ).getId();
    HttpServletRequest request = ContextProvider.getRequest();
    StringBuilder key = new StringBuilder();
    key.append( id );
    key.append( "." );
    key.append( propertyName );
    return request.getParameter( key.toString() );
  }


}
