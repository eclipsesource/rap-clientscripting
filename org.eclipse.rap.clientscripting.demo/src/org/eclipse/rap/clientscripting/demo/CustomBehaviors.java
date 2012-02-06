package org.eclipse.rap.clientscripting.demo;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.swt.SWT;
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
    clientListener.bindTo( text, SWT.KeyUp );
    clientListener.bindTo( text, SWT.MouseUp );
  }
  
  public static void addDigitsOnlyBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readContent( RESOURCES_PREFIX + "DigitsOnly.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.bindTo( text, SWT.KeyUp );
    clientListener.bindTo( text, SWT.MouseUp );
  }

  public static void addDateMaskBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readContent( RESOURCES_PREFIX + "DateMask.js" );
    ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.bindTo( text, SWT.KeyDown );
    clientListener.bindTo( text, SWT.MouseUp );
    clientListener.bindTo( text, SWT.MouseDown );
  }

  public static void addCounterBehavior( Control control ) {
    String scriptCode = ResourceLoaderUtil.readContent( RESOURCES_PREFIX + "Counter.js" );
    ClientListener listener = new ClientListener( scriptCode );
    listener.bindTo( control, SWT.MouseDown );
  }

  public static void addLoggerBehavior( Widget widget ) {
    String scriptCode = ResourceLoaderUtil.readContent( RESOURCES_PREFIX + "Logger.js" );
    ClientListener listener = new ClientListener( scriptCode );
    listener.bindTo( widget, SWT.KeyDown );
    listener.bindTo( widget, SWT.KeyUp );
    listener.bindTo( widget, SWT.FocusIn );
    listener.bindTo( widget, SWT.FocusOut );
    listener.bindTo( widget, SWT.MouseDown );
    listener.bindTo( widget, SWT.MouseUp );
    listener.bindTo( widget, SWT.MouseDoubleClick );
  }

}
