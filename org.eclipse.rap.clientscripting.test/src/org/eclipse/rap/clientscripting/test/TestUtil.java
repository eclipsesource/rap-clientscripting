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
package org.eclipse.rap.clientscripting.test;

import java.util.Collection;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.clientscripting.internal.ClientListenerAdapter;
import org.eclipse.rap.clientscripting.internal.ClientListenerBinding;
import org.eclipse.swt.widgets.Widget;


public class TestUtil {

  public static ClientListenerBinding findBinding( ClientListener listener,
                                                   Widget widget,
                                                   int eventType )
  {
    ClientListenerAdapter adapter = listener.getAdapter( ClientListenerAdapter.class );
    Collection<ClientListenerBinding> bindings = adapter.getBindings();
    for( ClientListenerBinding binding : bindings ) {
      if( binding.getWidget() == widget && binding.getEventType() == eventType  ) {
        return binding;
      }
    }
    return null;
  }

}
