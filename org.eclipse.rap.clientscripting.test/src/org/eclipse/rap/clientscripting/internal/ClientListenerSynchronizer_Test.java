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

import junit.framework.TestCase;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rap.rwt.testfixture.Message.CreateOperation;
import org.eclipse.rwt.internal.protocol.ClientObjectFactory;
import org.eclipse.rwt.internal.protocol.IClientObject;


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
  }

}
