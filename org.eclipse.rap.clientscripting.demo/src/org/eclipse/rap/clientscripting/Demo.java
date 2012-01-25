package org.eclipse.rap.clientscripting;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Demo implements IEntryPoint {
    
  @Override
  public int createUI() {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new GridLayout( 1, false ) );
    
    Text text = new Text( shell, SWT.SINGLE | SWT.READ_ONLY );
    text.setText( "__.__.____" );
    
    shell.pack();
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() )
        display.sleep();
    }
    display.dispose();
    return 0;
  }
  
}
