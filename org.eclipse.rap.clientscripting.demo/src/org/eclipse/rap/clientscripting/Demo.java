package org.eclipse.rap.clientscripting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

@SuppressWarnings( { "restriction", "serial" } )
public class Demo implements IEntryPoint {

  public int createUI() {
    Display display = new Display();
    Shell shell = new Shell( display );
    createShellContents( shell );
    shell.setBounds( 20, 20, 400, 200 );
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
    parent.setLayout( new GridLayout( 2, false ) );
    Text dateField = createDateField( parent );
    dateField.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    createVerifyButton( parent, dateField );
    Label counterLabel = createCounterLabel( parent );
    counterLabel.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    createLoggerButton( parent );
  }

  private Text createDateField( Composite parent ) {
    Text date = new Text( parent, SWT.SINGLE | SWT.BORDER );
    date.setText( "__.__.____" );
    addDateMaskBehavior( date );
    return date;
  }

  private void createVerifyButton( Composite parent, final Text dateField ) {
    Button verify = new Button( parent, SWT.PUSH );
    verify.setText( "Verify" );
    verify.addSelectionListener( new SelectionAdapter() {
      @Override
      public void widgetSelected( SelectionEvent e ) {
        verifyDate( dateField );
      }
    } );
  }

  private Label createCounterLabel( final Composite parent ) {
    Label label = new Label( parent, SWT.PUSH );
    label.setText( "Click!" );
    addCounterBehavior( label );
    return label;
  }

  private void createLoggerButton( final Composite parent ) {
    Button logger = new Button( parent, SWT.PUSH );
    logger.setText( "This logs all events" );
    addLoggerBehavior( logger );
  }

  private void addDateMaskBehavior( Text text ) {
    String code = "var f = new org.eclipse.rap.clientscripting.Function( \"";
    code += getJSHandler( "DateMask.js" );
    code += "\" );";
    code += "new org.eclipse.rap.clientscripting.EventBinding( ";
    code += getWidgetRef( text );
    code += ", org.eclipse.rap.clientscripting.SWT.KeyDown, f );";
    code += "new org.eclipse.rap.clientscripting.EventBinding( ";
    code += getWidgetRef( text );
    code += ", org.eclipse.rap.clientscripting.SWT.MouseDown, f );";
    JSExecutor.executeJS( code );
  }

  private void verifyDate( Text dateField ) {
    String text = dateField.getText();
    String[] values = text.split( "\\.", 3 );
    String message = "Date OK";
    int kind = MessageDialog.CONFIRM;
    try {
      if( Integer.parseInt( values[ 0 ] ) > 31 ) {
        kind = MessageDialog.ERROR;
        message = "Incorrect day " + values[ 0 ];
      }
      if( Integer.parseInt( values[ 1 ] ) > 12 ) {
        kind = MessageDialog.ERROR;
        message = "Incorrect month " + values[ 1 ];
      }
      Integer.parseInt( values[ 2 ].trim() ); // remove trailing " "
    } catch( NumberFormatException ex ) {
      kind = MessageDialog.ERROR;
      message = "Incomplete";
    }
    MessageDialog.open( kind, dateField.getShell(), "Verify", message, SWT.NONE );
  }

  private void addCounterBehavior( Label count ) {
    String code = "var f = new org.eclipse.rap.clientscripting.Function( \"";
    code += getJSHandler( "Counter.js" );
    code += "\" );";
    code += "new org.eclipse.rap.clientscripting.EventBinding( ";
    code += getWidgetRef( count );
    code += ", org.eclipse.rap.clientscripting.SWT.MouseDown, f );";
    JSExecutor.executeJS( code );
  }

  private void addLoggerBehavior( Widget widget ) {
    String code = "var f = new org.eclipse.rap.clientscripting.Function( \"";
    code += getJSHandler( "Logger.js" );
    code += "\" );";
    String[] events = {
      "SWT.KeyDown",
      "SWT.KeyUp",
      "SWT.FocusIn",
      "SWT.FocusOut",
      "SWT.MouseDown",
      "SWT.MouseUp",
      "SWT.MouseEnter",
      "SWT.MouseExit",
      "SWT.MouseMove",
      "SWT.MouseDoubleClick",
    };
    for( int i = 0; i < events.length; i++ ) {
      code += "new org.eclipse.rap.clientscripting.EventBinding( ";
      code += getWidgetRef( widget );
      code += ", org.eclipse.rap.clientscripting." + events[ i ] + ", f );";
    }
    JSExecutor.executeJS( code );
  }

  private String getWidgetRef( Widget widget ) {
    String result = "org.eclipse.rwt.protocol.ObjectManager.getObject( \"";
    result += WidgetUtil.getId( widget );
    result += "\" )";
    return result;
  }

  private String getJSHandler( String fileName ) {
    String code = getFileContent( "org/eclipse/rap/clientscripting/" + fileName ).toString();
    code = code.replace( "\"", "\\\"" );
    code = code.replace( "\n", "\\n" );
    return code;
  }

  private static StringBuffer getFileContent( final String file ) {
    StringBuffer buffer = new StringBuffer();
    InputStream stream = Demo.class.getClassLoader().getResourceAsStream( file );
    if( stream != null ) {
      InputStreamReader inputStreamReader = new InputStreamReader( stream );
      BufferedReader bufferedReader = new BufferedReader( inputStreamReader );
      try {
        String line = null;
        try {
          while( ( line = bufferedReader.readLine() ) != null ) {
            buffer.append( line );
            buffer.append( '\n' );
          }
        } finally {
          bufferedReader.close();
        }
      } catch( IOException e ) {
        buffer = null;
      }
    } else {
      buffer = null;
    }
    return buffer;
  }

}
