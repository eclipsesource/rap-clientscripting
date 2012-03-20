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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.examples.ExampleUtil;
import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;


public class ValidationExamplePage implements IExamplePage {
  
  private static final String[] VALID_DOMAINS = new String[] {
    "eclipse.org",
    "eclipsesource.com",
    "googlemail.com",
    "gmail.com",
    "nasa.gov"
  };

  public void createControl( Composite parent ) {
    parent.setLayout( ExampleUtil.createMainLayout( 2 ) );
    createInputForm( parent );
  }

  private void createInputForm( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, true, true ) );
    addDigitsOnlyExample( composite );
    //addUpperCaseExample( composite );
    addEMailExample( composite );
    addDateExample( composite );
  }

  private void addDigitsOnlyExample( Composite parent ) {
    ExampleUtil.createHeading( parent, "Allow only digits", 2 );
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, false, false ) );
    composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    Label labelServer = new Label( composite, SWT.NONE );
    labelServer.setText( "Server-Side validation:" );
    Text serverText = new Text( composite, SWT.BORDER );
    Label labelClient = new Label( composite, SWT.NONE );
    labelClient.setText( "Client-Side validation:" );
    Text clientText = new Text( composite, SWT.BORDER );
    serverText.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    serverText.setText( "23" );
    serverText.setSelection( 2 );
    serverText.setFocus();
    serverText.addListener( SWT.Modify, new Listener() {
      public void handleEvent( Event event ) {
        Text widget = ( Text )event.widget;
        String text = widget.getText();
        String regexp = "^[0-9]*$";
        if( !text.matches( regexp ) ) {
          widget.setBackground( new Color( widget.getDisplay(), 255, 255, 128 ) );
          widget.setToolTipText( "Only digits allowed!" );
        } else {
          widget.setBackground( null );
          widget.setToolTipText( null );
        }
      }
    } );
    clientText.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    clientText.setText( "23" );
    clientText.setSelection( 2 );
    CustomBehaviors.addDigitsOnlyBehavior( clientText );
  }

  private void addUpperCaseExample( Composite parent ) {
    ExampleUtil.createHeading( parent, "Only Upper-Case allowed", 2 );
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( ExampleUtil.createGridLayout( 2, false, false, false ) );
    composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    Label labelServerForced = new Label( composite, SWT.NONE );
    labelServerForced.setText( "Auto Upper-Case (Server):" );
    Text serverForcedText = new Text( composite, SWT.BORDER );
    Label labelClientForced = new Label( composite, SWT.NONE );
    labelClientForced.setText( "Auto Upper-Case (Client):" );
    Text clientForcedText = new Text( composite, SWT.BORDER );
    serverForcedText.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    serverForcedText.addVerifyListener( new VerifyListener() {
      public void verifyText( VerifyEvent event ) {
        // TODO [tb] : Does not work with untyped event?
        event.text = event.text.toUpperCase();
      }
    } );
    clientForcedText.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    CustomBehaviors.addUpperCaseBehavior( clientForcedText );

    Label labelServerDeny = new Label( composite, SWT.NONE );
    labelServerDeny.setText( "Deny Lower-Case (Server):" );
    Text serverDenyText = new Text( composite, SWT.BORDER );
    Label labelClientDeny = new Label( composite, SWT.NONE );
    labelClientDeny.setText( "Deny Lower-Case (Client):" );
    Text clientDenyText = new Text( composite, SWT.BORDER );
    serverDenyText.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    serverDenyText.addVerifyListener( new VerifyListener() {
      public void verifyText( VerifyEvent event ) {
        String regexp = ".*[a-z].*";
        // TODO [tb] : Does not work with untyped event?
        if( event.text.matches( regexp ) ) {
          event.doit = false; 
          Text widget = ( Text )event.widget;
          // TODO [tb] : selection should be reset automatically. 
          widget.setSelection( widget.getText().length() );
        }
      }
    } );
    clientDenyText.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    CustomBehaviors.addLowerCaseBehavior( clientDenyText );
  }

  private void addEMailExample( Composite parent ) {
    ExampleUtil.createHeading( parent, "E-Mail", 2 );
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, false, false ) );
    composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    Label label = new Label( composite, SWT.NONE );
    label.setText( "Basic client-side Modify check,\n server-side validation on focus-out:" );
    final Text text = new Text( composite, SWT.BORDER );
    text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    CustomBehaviors.addEMailBehavior( text );
    text.addFocusListener( new FocusListener() {
      public void focusLost( FocusEvent event ) {
        String mail = text.getText();
        if( mail.matches( "^\\S+@\\S+\\.[a-zA-Z]{2,5}$" ) ) {
          boolean valid = false;
          for( int i = 0; i < VALID_DOMAINS.length; i++ ) {
            String domain = VALID_DOMAINS[ i ];
            if( mail.endsWith( domain ) ) {
              valid = true;
            }
          }
          if( !valid ) {
            String title = "Invalid input";
            String mesg = "This is an unkown domain. Following domains are valid:\n";
            for( int i = 0; i < VALID_DOMAINS.length; i++ ) {
              mesg += VALID_DOMAINS[ i ];
              if( i < VALID_DOMAINS.length -1 ) {
                mesg += ", ";
              }
            }
            MessageDialog.openWarning( text.getShell(), title, mesg );
            text.setFocus();
            text.setBackground( new Color( text.getDisplay(), 255, 255, 128 ) );
          }
        }
      }
      public void focusGained( FocusEvent event ) {
      }
    } );
  }

  private void addDateExample( Composite parent ) {
    ExampleUtil.createHeading( parent, "Date", 2 );
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, false, false ) );
    composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    Label label = new Label( composite, SWT.NONE );
    label.setText( "Basic client-side Verify and Modify check,\n server-side validation on focus-out:" );
    final Text text = new Text( composite, SWT.BORDER );
    final String pattern = "DD.MM.YYYY";
    text.setMessage( pattern );
    text.setTextLimit( pattern.length() );
    text.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    CustomBehaviors.addDateFieldBehavior( text );
    text.addFocusListener( new FocusListener() {
      public void focusLost( FocusEvent event ) {
        String dateText = text.getText();
        if( dateText.length() > 0 ) {
          DateFormat format = DateFormat.getDateInstance( DateFormat.MEDIUM, Locale.GERMANY );
          format.setLenient( false );
          // TODO [tb] : handle dd.MM.yy format to result in 19yy or 20yy
          try {
            Date date = format.parse( dateText );
            text.setBackground( null );
            text.setText( format.format( date ) );
          } catch( ParseException e ) {
            text.setBackground( new Color( text.getDisplay(), 255, 255, 128 ) );
            String title = "Invalid input";
            String mesg = "This is an invalid or incomplete date.";
            MessageDialog.openWarning( text.getShell(), title, mesg );
            text.setFocus();
          }
        }
      }
      public void focusGained( FocusEvent event ) {
      }
    } );
  }


}
