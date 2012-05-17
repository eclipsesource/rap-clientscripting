package org.eclipse.rap.clientscripting.internal;


import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rwt.Adaptable;
import org.eclipse.rwt.internal.protocol.IClientObjectAdapter;
import org.eclipse.swt.widgets.Widget;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings( { "restriction" } )
public class ClientListenerBinding implements Adaptable {

  private final ClientListener listener;
  private final Widget widget;
  private final int eventType;
  private final Map<String, Object> context;
  private IClientObjectAdapter2 iClientObjectAdapter2;
  private boolean disposed;

  public ClientListenerBinding( 
    Widget widget, 
    int eventType, 
    ClientListener listener, 
    Map<String, Object> context ) 
  {
    this.widget = widget;
    this.eventType = eventType;
    this.listener = listener;
    this.context = new HashMap<String, Object>( context );
    disposed = false;
  }

  public ClientListener getListener() {
    return listener;
  }

  public Widget getWidget() {
    return widget;
  }

  public int getEventType() {
    return eventType;
  }

  public Map<String, Object> getContext() {
    return new HashMap<String, Object>( context );
  }

  public boolean isDisposed() {
    return disposed;
  }

  public void markDisposed() {
    disposed = true;
  }

  @Override
  public boolean equals( Object obj ) {
    boolean result = false;
    if( this == obj ) {
      result = true;
    } else if( obj != null && getClass() == obj.getClass() ) {
      ClientListenerBinding other = ( ClientListenerBinding )obj;
      if(    eventType == other.eventType 
          && widget == other.widget 
          && listener == other.listener
          && context.equals(other.context ) )
      {
        result = true;
      }
    }
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + eventType;
    result = prime * result + widget.hashCode();
    result = prime * result + listener.hashCode();
    result = prime * result + context.hashCode();
    return result;
  }

  @SuppressWarnings( "unchecked" )
  public <T> T getAdapter( Class<T> adapter ) {
    T result = null;
    if( adapter == IClientObjectAdapter2.class || adapter == IClientObjectAdapter.class ) {
      if( iClientObjectAdapter2 == null ) {
        iClientObjectAdapter2 = new ClientObjectAdapterImpl();
      }
      result = ( T )iClientObjectAdapter2;
    }
    return result;
  }

}
