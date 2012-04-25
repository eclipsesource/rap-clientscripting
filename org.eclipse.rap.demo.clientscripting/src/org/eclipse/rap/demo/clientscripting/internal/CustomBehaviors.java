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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;


public class CustomBehaviors {

  private static final Pattern DATE_PATTERN = Pattern.compile( "^\\s*(\\d+)\\.\\s*(\\d+)\\.\\s*(\\d+)\\s*$" );

  private static final String RESOURCES_PREFIX = "org/eclipse/rap/demo/clientscripting/internal/";

  private static final String[] VALID_DOMAINS = new String[] {
    "eclipse.org",
    "eclipsesource.com",
    "googlemail.com",
    "gmail.com",
    "nasa.gov"
  };

  private CustomBehaviors() {
    // prevent instantiation
  }

  public static void addUpperCaseBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "UpperCase.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.addTo( text, ClientListener.Verify );
  }

  public static void addDigitsOnlyBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DigitsOnly.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.addTo( text, ClientListener.Modify );
  }

  public static void addDigitsOnlyEnforcementBehavior( Text text ) {
    String scriptCode
      = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DigitsOnlyEnforcement.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.addTo( text, ClientListener.Verify );
  }

  public static void addDateFieldBehavior( final Text text ) {
    addClientSideDateFieldBehavior( text );
    addServerSideDateFieldBehavior( text );
  }

  private static void addClientSideDateFieldBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DateField.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.addTo( text, ClientListener.Verify );
    clientListener.addTo( text, ClientListener.Modify );
  }

  private static void addServerSideDateFieldBehavior( final Text text ) {
    final String pattern = "DD.MM.YYYY";
    text.setMessage( pattern );
    text.setTextLimit( pattern.length() );
    text.addFocusListener( new FocusAdapter() {
      @Override
      public void focusLost( FocusEvent event ) {
        String dateText = text.getText();
        if( dateText.length() > 0 ) {
          try {
            String date = evaluateDate( dateText );
            text.setText( date );
            showWarning( text, null );
          } catch( Exception exception ) {
            showWarning( text, exception.getMessage() );
          }
        }
      }
    } );
  }

  public static void addCounterBehavior( Control control ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Counter.js" );
    ClientListener listener = new ClientListener( scriptCode );
    listener.addTo( control, ClientListener.MouseDown );
  }

  public static void addEMailBehavior( Text text ) {
    addClientSideEmailBehavior( text );
    addServerSideEmailBehavior( text );
  }

  private static void addClientSideEmailBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Email.js" );
    ClientListener listener = new ClientListener( scriptCode );
    listener.addTo( text, ClientListener.Modify );
  }

  private static void addServerSideEmailBehavior( final Text text ) {
    text.addFocusListener( new FocusAdapter() {
      @Override
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
            String message = "This is an unkown domain. Following domains are valid:\n";
            for( int i = 0; i < VALID_DOMAINS.length; i++ ) {
              message += VALID_DOMAINS[ i ];
              if( i < VALID_DOMAINS.length -1 ) {
                message += ", ";
              }
            }
            showWarning( text, message );
          }
        }
      }
    } );
  }

  public static void addLoggerBehavior( Widget widget ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Logger.js" );
    ClientListener listener = new ClientListener( scriptCode );
    listener.addTo( widget, ClientListener.KeyDown );
    listener.addTo( widget, ClientListener.KeyUp );
    listener.addTo( widget, ClientListener.FocusIn );
    listener.addTo( widget, ClientListener.FocusOut );
    listener.addTo( widget, ClientListener.MouseDown );
    listener.addTo( widget, ClientListener.MouseUp );
    listener.addTo( widget, ClientListener.MouseEnter );
    listener.addTo( widget, ClientListener.MouseExit );
    listener.addTo( widget, ClientListener.MouseMove );
    listener.addTo( widget, ClientListener.MouseDoubleClick );
  }

  private static void showWarning( Text text, String message ) {
    if( message == null ) {
      text.setBackground( null );
      text.setToolTipText( null );
    } else {
      text.setBackground( new Color( text.getDisplay(), 255, 255, 128 ) );
      text.setToolTipText( message );
    }
  }

  private static String evaluateDate( String string ) {
    Matcher matcher = DATE_PATTERN.matcher( string );
    if( !matcher.matches() ) {
      throw new IllegalArgumentException( "Illegal date format, expected: DD.MM.YYYY" );
    }
    int day = Integer.parseInt( matcher.group( 1 ) );
    int month = Integer.parseInt( matcher.group( 2 ) );
    int year = Integer.parseInt( matcher.group( 3 ) );
    if( day < 0 ) {
      throw new IllegalArgumentException( "Illegal year: " + year );
    } else if( year < 50 ) {
      year += 2000;
    } else if( year < 100 ) {
      year += 1900;
    }
    if( month < 1 || month > 12 ) {
      throw new IllegalArgumentException( "Illegal month: " + month );
    }
    if( day < 1 || day > 31 ) {
      throw new IllegalArgumentException( "Illegal day: " + day );
    }
    return formatDate( day, month, year );
  }

  private static String formatDate( int day, int month, int year ) {
    StringBuilder builder = new StringBuilder();
    builder.append( padZero( day, 2 ) );
    builder.append( "." );
    builder.append( padZero( month, 2 ) );
    builder.append( "." );
    builder.append( year );
    return builder.toString();
  }

  private static String padZero( int number, int length ) {
    String result = Integer.toString( number );
    if( result.length() < length ) {
      StringBuilder builder = new StringBuilder();
      for( int i = result.length(); i < length; i++ ) {
        builder.append( '0' );
      }
      builder.append( result );
      result = builder.toString();
    }
    return result;
  }

}
