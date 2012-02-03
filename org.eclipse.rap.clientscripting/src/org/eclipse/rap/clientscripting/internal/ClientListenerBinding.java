package org.eclipse.rap.clientscripting.internal;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.swt.widgets.Widget;


public class ClientListenerBinding {

  private final Widget widget;
  private final int eventType;
  private final ClientListener listener;

  public ClientListenerBinding( Widget widget, int eventType, ClientListener listener ) {
    this.widget = widget;
    this.eventType = eventType;
    this.listener = listener;
  }

  @Override
  public boolean equals( Object obj ) {
    boolean result = false;
    if( this == obj ) {
      result = true;
    } else if( obj != null && getClass() == obj.getClass() ) {
      ClientListenerBinding other = ( ClientListenerBinding )obj;
      if( eventType == other.eventType && widget == other.widget && listener == other.listener ) {
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
    return result;
  }

}
