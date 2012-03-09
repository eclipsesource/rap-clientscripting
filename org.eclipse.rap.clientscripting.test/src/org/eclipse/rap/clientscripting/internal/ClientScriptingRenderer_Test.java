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
import org.eclipse.rap.clientscripting.test.TestUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rap.rwt.testfixture.Message;
import org.eclipse.rap.rwt.testfixture.Message.DestroyOperation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class ClientScriptingRenderer_Test extends TestCase {

  private Shell shell;
  private Display display;
  private Label label;

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
    createWidgets();
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testCreateListenerNotRenderedBeforeBind() {
    new ClientListener( "code" );

    ClientScriptingRenderer.render();

    Message message = Fixture.getProtocolMessage();
    assertEquals( 0, message.getOperationCount() );
  }

  public void testCreateListenerAndBindingRendered() {
    ClientListener listener = new ClientListener( "code" );
    listener.addTo( label, ClientListener.KeyUp );
    ClientListenerBinding binding = TestUtil.findBinding( listener, label, ClientListener.KeyUp );

    ClientScriptingRenderer.render();

    Message message = Fixture.getProtocolMessage();
    assertNotNull( message.findCreateOperation( ClientObjectUtil.getId( listener ) ) );
    assertNotNull( message.findCreateOperation( ClientObjectUtil.getId( binding ) ) );
  }

  public void testCreateRenderedOnlyOnce() {
    ClientListener listener = new ClientListener( "code" );
    listener.addTo( label, ClientListener.KeyUp );

    ClientScriptingRenderer.render();
    Fixture.fakeNewRequest();
    ClientScriptingRenderer.render();

    Message message = Fixture.getProtocolMessage();
    assertEquals( 0, message.getOperationCount() );
  }

  public void testDestroyBindingRendered() {
    ClientListener listener = new ClientListener( "code" );
    listener.addTo( label, ClientListener.KeyUp );
    ClientListenerBinding binding = TestUtil.findBinding( listener, label, ClientListener.KeyUp );
    ClientScriptingRenderer.render();
    Fixture.fakeNewRequest();

    listener.removeFrom( label, ClientListener.KeyUp );
    ClientScriptingRenderer.render();

    Message message = Fixture.getProtocolMessage();
    // TODO [rst] Use Message#findDestroyOperation( String ) as soon as it is available
    DestroyOperation operation = ( DestroyOperation )message.getOperation( 0 );
    assertEquals( ClientObjectUtil.getId( binding ), operation.getTarget() );
  }

  public void testDestroyBindingRenderedOnlyOnce() {
    ClientListener listener = new ClientListener( "code" );
    listener.addTo( label, ClientListener.KeyUp );
    ClientScriptingRenderer.render();
    Fixture.fakeNewRequest();
    listener.removeFrom( label, ClientListener.KeyUp );
    ClientScriptingRenderer.render();
    Fixture.fakeNewRequest();

    listener.removeFrom( label, ClientListener.KeyUp );
    ClientScriptingRenderer.render();

    Message message = Fixture.getProtocolMessage();
    assertEquals( 0, message.getOperationCount() );
  }

  private void createWidgets() {
    display = new Display();
    shell = new Shell( display );
    label = new Label( shell, SWT.NONE );
  }

}
