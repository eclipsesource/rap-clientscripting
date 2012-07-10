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

import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rap.rwt.testfixture.Message.CreateOperation;
import org.eclipse.rwt.internal.protocol.ClientObjectFactory;
import org.eclipse.rwt.internal.protocol.IClientObject;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.json.JSONException;
import org.json.JSONObject;


@SuppressWarnings( "restriction" )
public class ClientListenerSynchronizer_Test extends TestCase {

  private ClientListenerSynchronizer synchronizer;

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
    synchronizer = new ClientListenerSynchronizer();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testRenderCreate() {
    ClientListener listener = new ClientListener( "script code" );
    IClientObject clientObject = ClientObjectFactory.getClientObject( listener );

    synchronizer.renderCreate( listener, clientObject );

    Message message = Fixture.getProtocolMessage();
    CreateOperation operation = message.findCreateOperation( ClientObjectUtil.getId( listener ) );
    assertEquals( "rwt.clientscripting.Listener", operation.getType() );
    assertEquals( "script code", operation.getProperty( "code" ) );
    assertEquals( "{}", operation.getProperty( "scope" ).toString() );
  }

  public void testRenderContextInt() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put( "number", new Integer( 3 ) );
    ClientListener listener = new ClientListener( "script code", map );
    IClientObject clientObject = ClientObjectFactory.getClientObject( listener );

    synchronizer.renderCreate( listener, clientObject );

    JSONObject contextResult = getContext( listener );
    int result = -1;
    try {
      result = contextResult.getInt( "number" );
    } catch( JSONException e ) {
      // should not happen
    }
    assertEquals( 3, result );
  }

  private JSONObject getContext( ClientListener listener ) {
    Message message = Fixture.getProtocolMessage();
    CreateOperation operation = message.findCreateOperation( ClientObjectUtil.getId( listener ) );
    JSONObject contextResult = ( JSONObject )operation.getProperty( "scope" );
    return contextResult;
  }

  public void testRenderContextString() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put( "sometext", "foo" );
    ClientListener listener = new ClientListener( "script code", map );
    IClientObject clientObject = ClientObjectFactory.getClientObject( listener );

    synchronizer.renderCreate( listener, clientObject );

    JSONObject contextResult = getContext( listener );
    String result = null;
    try {
      result = contextResult.getString( "sometext" );
    } catch( JSONException e ) {
      // should not happen
    }
    assertEquals( "foo", result );
  }

  public void testRenderContextWidet() {
    Shell shell = new Shell( new Display() );
    HashMap<String, Object> map = new HashMap<String, Object>();
    Widget widget = new Label( shell, SWT.NONE );
    map.put( "widget", widget );
    ClientListener listener = new ClientListener( "script code", map );
    IClientObject clientObject = ClientObjectFactory.getClientObject( listener );

    synchronizer.renderCreate( listener, clientObject );

    JSONObject contextResult = getContext( listener );
    String widgetId = null;
    try {
      widgetId = contextResult.getJSONObject( "widget" ).getString( "id" );
    } catch( JSONException e ) {
      // should not happen
    }
    assertEquals( WidgetUtil.getId( widget ), widgetId );
  }

  public void testRenderUnsupportedType() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put( "sometext", new Date() );
    ClientListener listener = new ClientListener( "script code", map );
    IClientObject clientObject = ClientObjectFactory.getClientObject( listener );

    try {
      synchronizer.renderCreate( listener, clientObject );
      fail();
    }catch( IllegalArgumentException ex ) {
      // expected
    }
  }


}
