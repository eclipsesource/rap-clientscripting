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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.clientscripting.JavaFunction;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rap.rwt.testfixture.Message.CreateOperation;
import org.eclipse.rwt.internal.protocol.ClientObjectFactory;
import org.eclipse.rwt.internal.protocol.IClientObject;
import org.eclipse.rwt.lifecycle.PhaseId;
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

  public void testRenderScopeInt() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put( "number", new Integer( 3 ) );
    ClientListener listener = new ClientListener( "script code", map );
    IClientObject clientObject = ClientObjectFactory.getClientObject( listener );

    synchronizer.renderCreate( listener, clientObject );

    JSONObject scopeResult = getScope( listener );
    int result = -1;
    try {
      result = scopeResult.getInt( "number" );
    } catch( JSONException e ) {
      // should not happen
    }
    assertEquals( 3, result );
  }

  private JSONObject getScope( ClientListener listener ) {
    Message message = Fixture.getProtocolMessage();
    CreateOperation operation = message.findCreateOperation( ClientObjectUtil.getId( listener ) );
    JSONObject scopeResult = ( JSONObject )operation.getProperty( "scope" );
    return scopeResult;
  }

  public void testRenderScopeString() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put( "sometext", "foo" );
    ClientListener listener = new ClientListener( "script code", map );
    IClientObject clientObject = ClientObjectFactory.getClientObject( listener );

    synchronizer.renderCreate( listener, clientObject );

    JSONObject scopeResult = getScope( listener );
    String result = null;
    try {
      result = scopeResult.getString( "sometext" );
    } catch( JSONException e ) {
      // should not happen
    }
    assertEquals( "foo", result );
  }

  public void testRenderScopeWidget() {
    Shell shell = new Shell( new Display() );
    HashMap<String, Object> map = new HashMap<String, Object>();
    Widget widget = new Label( shell, SWT.NONE );
    map.put( "widget", widget );
    ClientListener listener = new ClientListener( "script code", map );
    IClientObject clientObject = ClientObjectFactory.getClientObject( listener );

    synchronizer.renderCreate( listener, clientObject );

    JSONObject scopeResult = getScope( listener );
    String widgetId = null;
    String type = null;
    try {
      type = scopeResult.getJSONObject( "widget" ).getString( "type" );
      widgetId = scopeResult.getJSONObject( "widget" ).getString( "id" );
    } catch( JSONException e ) {
      // should not happen
    }
    assertEquals( "widget", type );
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

  public void testRenderScopeFunction() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put( "func", new JavaFunction(){
      public Object execute( Object[] args ) {
        return null;
      }
    } );
    ClientListener listener = new ClientListener( "script code", map );
    IClientObject clientObject = ClientObjectFactory.getClientObject( listener );

    synchronizer.renderCreate( listener, clientObject );

    JSONObject scopeResult = getScope( listener );
    String type = null;
    try {
      type = scopeResult.getJSONObject( "func" ).getString( "type" );
    } catch( JSONException e ) {
      // should not happen
    }
    assertEquals( "function", type );
  }

  public void testJavaFunctionCall() {
    Display display = new Display();
    Fixture.fakeNewRequest( display );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    HashMap<String, Object> map = new HashMap<String, Object>();
    final ArrayList<Object[]> log = new ArrayList<Object[]>();
    map.put( "func", new JavaFunction(){
      public Object execute( Object[] args ) {
        log.add( args );
        return null;
      }
    } );
    ClientListener listener = new ClientListener( "script code", map );
    String listenerId = ClientObjectUtil.getId( listener );
    String param = listenerId + "." + ClientListenerSynchronizer.PARAM_EXECUTE_FUNCTION;
    Fixture.fakeNewRequest( display );
    Fixture.fakeRequestParam( param, "func" );
    Fixture.fakePhase( PhaseId.READ_DATA );

    synchronizer.readData( listener );
    Fixture.fakePhase( PhaseId.PROCESS_ACTION );
    while( display.readAndDispatch() ) {}

    assertEquals( 1, log.size() );
  }


}
