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
package org.eclipse.rap.clientscripting.demo;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


@SuppressWarnings( "serial" )
public class Demo implements IEntryPoint {

  public int createUI() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "RAP Client Scripting Examples" );
    createShellContents( shell );
    shell.setBounds( 20, 20, 400, 400 );
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
    return 0;
  }

  private void createShellContents( Composite parent ) {
    parent.setLayout( new GridLayout( 1, false ) );
    addUpperCaseExample( parent );
    addDigitsOnlyExample( parent );
    addDateFieldExample( parent );
    addCounterExample( parent );
  }

  private void addUpperCaseExample( Composite parent ) {
    addHeaderLabel( parent, "Auto upper case text field:" );
    Text text = new Text( parent, SWT.BORDER );
    CustomBehaviors.addUpperCaseBehavior( text );
    text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
  }

  private void addDigitsOnlyExample( Composite parent ) {
    addHeaderLabel( parent, "Digits only, validation on client:" );
    Text text = new Text( parent, SWT.BORDER );
    CustomBehaviors.addDigitsOnlyBehavior( text );
    text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
  }

  private void addDateFieldExample( Composite parent ) {
    addHeaderLabel( parent, "A simple date field, validation on server:" );
    final Text text = new Text( parent, SWT.BORDER );
    CustomBehaviors.addDateFieldBehavior( text );
    addDateValidator( text );
    text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
  }

  private void addCounterExample( Composite parent ) {
    addHeaderLabel( parent, "A button that counts:" );
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Click me!" );
    button.setLayoutData( new GridData( 120, SWT.DEFAULT ) );
    CustomBehaviors.addCounterBehavior( button );
  }

  private static void addHeaderLabel( Composite parent, String text ) {
    Label label = new Label( parent, SWT.NONE );
    label.setText( text );
    GridData layoutData = new GridData();
    layoutData.verticalIndent = 10;
    label.setLayoutData( layoutData );
  }

  private void addDateValidator( final Text text ) {
    text.addFocusListener( new FocusAdapter() {
      Color color = new Color( text.getDisplay(), 255, 128, 128 );
      @Override
      public void focusLost( FocusEvent event ) {
        if( !verifyDate( text.getText() ) ) {
          text.setBackground( color );
        } else {
          text.setBackground( null );
        }
      }
    } );
  }

  private boolean verifyDate( String date ) {
    String[] values = date.split( "\\.", 3 );
    boolean valid = true;
    try {
      if( Integer.parseInt( values[ 0 ] ) > 31 ) {
        valid = false;
      }
      if( Integer.parseInt( values[ 1 ] ) > 12 ) {
        valid = false;
      }
      Integer.parseInt( values[ 2 ].trim() ); // remove trailing " "
    } catch( NumberFormatException ex ) {
      valid = false;
    }
    return valid;
  }

}
