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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

public abstract class Listener implements org.eclipse.swt.widgets.Listener {

  private ClientListener clientListener;

  public void addTo( Widget widget, int type ) {
    getClientListener().addTo( widget, type );
  }

  public void removeTo( Widget widget, int type ) {
    getClientListener().removeFrom( widget, type );
  }

  public void handleEvent( Event event ){
    // allow use as client-listener only
  }

  public abstract String getClientImpl();

  ClientListener getClientListener() {
    if( clientListener  == null ) {
      clientListener = new ClientListener( this.getClientImpl(), getContext() );
    }
    return clientListener;
  }

  public void dispose() {
    clientListener.dispose();
  }

  private Map< String,Object > getContext() {
    Map<String,Object> result = new HashMap<String, Object>();
    Field[] fields = this.getClass().getDeclaredFields();
    for( int i = 0; i < fields.length; i++ ) {
      Field field = fields[ i ];
      if( !field.isSynthetic() ) {
        Object value = getValueFromField( field );
        result.put( field.getName(), value );
      }
    }
    Method[] methods = this.getClass().getDeclaredMethods();
    for( int i = 0; i < methods.length; i++ ) {
      Method method = methods[ i ];
      if(    !method.isSynthetic() 
          && method.getName() != "getClientImpl"
          && method.getName() != "handleEvent" )
      {
        if( ( method.getModifiers() & Modifier.PRIVATE ) == 0 ) {
          throw new RuntimeException( "Method " + method.getName() + " must be private" );
        }
      }
    }
    return result;
  }

  private Object getValueFromField( Field field ) {
    field.setAccessible( true );
    if( ( field.getModifiers() & Modifier.PRIVATE ) == 0 ) {
      throw new RuntimeException( "Field " + field.getName() + " must be private" );
    }
    if( ( field.getModifiers() & Modifier.FINAL ) != 0 ) {
      throw new RuntimeException( "Field " + field.getName() + " must not be final" );
    }
    Object result = null;
    try {
      if( field.getType() == Integer.TYPE ) {
        result = new Integer( field.getInt( this ) );
      } else if( !field.getType().isPrimitive() ) {
        Object value = field.get( this );
        if(    value instanceof Widget 
            || value instanceof String ) 
        {
          result = value;
        } else {
          throw new RuntimeException( "Field " + field.getName() + " has unsupported type" );
        }
      } else {
        throw new RuntimeException( "Field " + field.getName() + " has unsupported type" );
      }
    } catch( IllegalArgumentException e ) {
      throw new RuntimeException( "Could not read field " + field.getName(), e );
    } catch( IllegalAccessException e ) {
      throw new RuntimeException( "Could not read field " + field.getName(), e );
    }
    return result;
  }

}
