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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rap.rwt.testfixture.Message.CreateOperation;
import org.eclipse.rwt.internal.protocol.ClientObjectFactory;
import org.eclipse.rwt.internal.protocol.IClientObject;
import org.eclipse.rwt.internal.theme.JsonObject;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.json.JSONException;
import org.json.JSONObject;


@SuppressWarnings( "restriction" )
public class ClientListenerBindingSynchronizer_Test extends TestCase {

  private ClientListenerBindingSynchronizer synchronizer;

  private Shell shell;
  private Display display;
  private Label label;
  private ClientListener listener;
  private ClientListenerBinding binding;

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
    createWidgets();
    createListeners();
    synchronizer = new ClientListenerBindingSynchronizer();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testRenderCreate() {
    createBindings( null );
    IClientObject bindingClientObject = ClientObjectFactory.getClientObject( binding );

    synchronizer.renderCreate( binding, bindingClientObject );

    Message message = Fixture.getProtocolMessage();
    CreateOperation operation = message.findCreateOperation( ClientObjectUtil.getId( binding ) );
    assertEquals( "rwt.clientscripting.EventBinding", operation.getType() );
    assertEquals( ClientObjectUtil.getId( listener ), operation.getProperty( "listener" ) );
    assertEquals( WidgetUtil.getId( label ), operation.getProperty( "targetObject" ) );
    assertEquals( "KeyUp", operation.getProperty( "eventType" ) );
    assertEquals( "{}", operation.getProperty( "context" ).toString() );
  }
  
  public void testRenderContextWidet() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    Widget widget = new Label( shell, SWT.NONE );
    map.put( "widget", widget );
    createBindings( map );
    IClientObject bindingClientObject = ClientObjectFactory.getClientObject( binding );
    
    synchronizer.renderCreate( binding, bindingClientObject );
    
    Message message = Fixture.getProtocolMessage();
    CreateOperation operation = message.findCreateOperation( ClientObjectUtil.getId( binding ) );
    assertEquals( "rwt.clientscripting.EventBinding", operation.getType() );
    JSONObject contextResult = ( JSONObject )operation.getProperty( "context" );
    String widgetId = null;
    try {
      widgetId = contextResult.getJSONObject( "widget" ).getString( "id" );
    } catch( JSONException e ) {
      // should not happen
    }
    assertEquals( WidgetUtil.getId( widget ), widgetId );
  }

  private void createWidgets() {
    display = new Display();
    shell = new Shell( display );
    label = new Label( shell, SWT.NONE );
  }

  private void createListeners() {
    listener = new ClientListener( "code" );
  }

  private void createBindings( Map<String, Object> context ) {
    Map<String, Object> useContext = context != null ? context : new HashMap<String, Object>();
    binding = new ClientListenerBinding( label, SWT.KeyUp, listener, useContext );
  }

}
