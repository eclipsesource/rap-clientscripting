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

  public void testNotRenderedBeforeBind() {
    new ClientListener( "code" );

    ClientScriptingRenderer.render();

    Message message = Fixture.getProtocolMessage();
    assertEquals( 0, message.getOperationCount() );
  }

  public void testRendered() {
    ClientListener listener = new ClientListener( "code" );
    listener.addTo( label, ClientListener.MouseDown );

    ClientScriptingRenderer.render();

    Message message = Fixture.getProtocolMessage();
    String listenerId = listener.getAdapter( ClientObjectAdapter.class ).getId();
    assertNotNull( message.findCreateOperation( listenerId ) );
  }

  public void testNotRenderedOnlyOnce() {
    ClientListener listener = new ClientListener( "code" );
    listener.addTo( label, ClientListener.MouseDown );

    ClientScriptingRenderer.render();
    Fixture.fakeNewRequest();
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
