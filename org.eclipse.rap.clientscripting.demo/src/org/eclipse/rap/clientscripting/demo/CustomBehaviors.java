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
    String scriptCode = ResourceLoaderUtil.readContent( RESOURCES_PREFIX + "UpperCase.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.bindTo( text, ClientListener.KeyUp );
    clientListener.bindTo( text, ClientListener.MouseUp );
  }
  
  public static void addDigitsOnlyBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readContent( RESOURCES_PREFIX + "DigitsOnly.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.bindTo( text, ClientListener.KeyUp );
    clientListener.bindTo( text, ClientListener.MouseUp );
  }

  public static void addDateMaskBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readContent( RESOURCES_PREFIX + "DateMask.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.bindTo( text, ClientListener.KeyDown );
    clientListener.bindTo( text, ClientListener.Verify );
    clientListener.bindTo( text, ClientListener.MouseUp );
    clientListener.bindTo( text, ClientListener.MouseDown );
  }

  public static void addCounterBehavior( Control control ) {
    String scriptCode = ResourceLoaderUtil.readContent( RESOURCES_PREFIX + "Counter.js" );
    ClientListener listener = new ClientListener( scriptCode );
    listener.bindTo( control, ClientListener.MouseDown );
  }

  public static void addLoggerBehavior( Widget widget ) {
    String scriptCode = ResourceLoaderUtil.readContent( RESOURCES_PREFIX + "Logger.js" );
    ClientListener listener = new ClientListener( scriptCode );
    listener.bindTo( widget, ClientListener.KeyDown );
    listener.bindTo( widget, ClientListener.KeyUp );
    listener.bindTo( widget, ClientListener.FocusIn );
    listener.bindTo( widget, ClientListener.FocusOut );
    listener.bindTo( widget, ClientListener.MouseDown );
    listener.bindTo( widget, ClientListener.MouseUp );
    listener.bindTo( widget, ClientListener.MouseDoubleClick );
  }

}
