package org.eclipse.rap.clientscripting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.rwt.lifecycle.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

@SuppressWarnings("restriction")
public class Demo implements IEntryPoint {
    
  public int createUI() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new GridLayout( 1, false ) );
    
    Text text = new Text( shell, SWT.SINGLE );
    text.setText( "__.__.____ " ); // last empty space prevents horizontal scrolling
    addMaskBehavior( text );
    
    shell.pack();
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
    return 0;
  }

  private void addMaskBehavior( Text text ) {
    String code = "var f = new org.eclipse.rap.clientscripting.Function( \"";
    code += getJSHandler( "DateMask.js" );
    code += "\" );";
    code += "new org.eclipse.rap.clientscripting.EventBinding( ";
    code += getWidgetRef( text );
    code += ", org.eclipse.rap.clientscripting.SWT.KeyDown, f );";
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
