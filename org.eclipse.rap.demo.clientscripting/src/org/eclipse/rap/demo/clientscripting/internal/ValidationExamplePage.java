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
package org.eclipse.rap.demo.clientscripting.internal;

import org.eclipse.rap.examples.ExampleUtil;
import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class ValidationExamplePage implements IExamplePage {

  public void createControl( Composite parent ) {
    parent.setLayout( ExampleUtil.createMainLayout( 2 ) );
    createInputForm( parent );
  }

  private void createInputForm( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, true, true ) );
    addDigitsOnlyExample( composite );
    addUpperCaseExample( composite );
    addDateFieldExample( composite );
  }

  private void addDigitsOnlyExample( Composite parent ) {
    addHeaderLabel( parent, "Only digits allowed, validated directly on the client:" );
    Text text = new Text( parent, SWT.BORDER );
    CustomBehaviors.addDigitsOnlyBehavior( text );
    text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    text.setText( "23" );
    text.setSelection( 2 );
    text.setFocus();
  }

  private void addUpperCaseExample( Composite parent ) {
    addHeaderLabel( parent, "Only upper-case characters, auto-changed on the client:" );
    Text text = new Text( parent, SWT.BORDER );
    CustomBehaviors.addUpperCaseBehavior( text );
    text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
  }

  private void addDateFieldExample( Composite parent ) {
    addHeaderLabel( parent, "Simple date field, syntax validation on client,"
                            + " thorough validation on server on focus-out:" );
    Text text = new Text( parent, SWT.BORDER );
    CustomBehaviors.addDateFieldBehavior( text );
    addDateValidator( text );
    text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
  }

  private static void addHeaderLabel( Composite parent, String text ) {
    Label label = new Label( parent, SWT.WRAP );
    label.setText( text );
    GridData layoutData = ExampleUtil.createHorzFillData();
    layoutData.verticalIndent = 10;
    label.setLayoutData( layoutData );
  }

  private static void addDateValidator( final Text text ) {
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

  private static boolean verifyDate( String date ) {
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
