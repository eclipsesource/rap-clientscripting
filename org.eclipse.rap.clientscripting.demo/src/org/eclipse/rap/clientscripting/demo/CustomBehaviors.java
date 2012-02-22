package org.eclipse.rap.clientscripting.demo;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;


public class CustomBehaviors {

  private static final String RESOURCES_PREFIX = "org/eclipse/rap/clientscripting/demo/";

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

  public static void addDateFieldBehavior( Text text ) {
    text.setText( "__.__.____" );
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DateField.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.addTo( text, ClientListener.KeyDown );
    clientListener.addTo( text, ClientListener.Verify );
    clientListener.addTo( text, ClientListener.MouseUp );
    clientListener.addTo( text, ClientListener.MouseDown );
  }

  public static void addCounterBehavior( Control control ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Counter.js" );
    ClientListener listener = new ClientListener( scriptCode );
    listener.addTo( control, ClientListener.MouseDown );
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

}
