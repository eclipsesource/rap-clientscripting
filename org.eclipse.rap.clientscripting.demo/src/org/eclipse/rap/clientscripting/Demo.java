package org.eclipse.rap.clientscripting;

import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("restriction")
public class Demo implements IEntryPoint {
    
  public int createUI() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new GridLayout( 1, false ) );
    
    Text text = new Text( shell, SWT.SINGLE | SWT.READ_ONLY );
    text.setText( "__.__.____" );
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
    String code =   "listener = new org.eclipse.rap.clientscripting.ClientEventListener( " 
                  + "\"function(){}\" );";
    JSExecutor.executeJS( code );
  }
  
}
