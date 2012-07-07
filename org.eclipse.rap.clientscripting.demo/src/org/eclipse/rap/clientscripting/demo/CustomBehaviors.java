package org.eclipse.rap.clientscripting.demo;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.clientscripting.Listener;
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
    final ClientListener clientListener = new ClientListener( scriptCode );
    clientListener.addTo( text, ClientListener.Verify );
    ( new Listener() {
      public ClientListener getClientImpl() {
        return clientListener;
      }
    } ).addTo( text, ClientListener.Verify );
  }

  public static void addDigitsOnlyBehavior( Text text ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DigitsOnly.js" );
    final ClientListener clientListener = new ClientListener( scriptCode );
    ( new Listener() {
      public ClientListener getClientImpl() {
        return clientListener;
      }
    } ).addTo( text, ClientListener.Modify );  }

  public static void addDateFieldBehavior( Text text ) {
    text.setText( "__.__.____" );
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DateField.js" );
    final ClientListener clientListener = new ClientListener( scriptCode );
    Listener listener = new Listener() {
      public ClientListener getClientImpl() {
        return clientListener;
      }
    };
    listener.addTo( text, ClientListener.KeyDown );
    listener.addTo( text, ClientListener.Verify );
    listener.addTo( text, ClientListener.MouseUp );
    listener.addTo( text, ClientListener.MouseDown );
  }

  public static void addCounterBehavior( Control control ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Counter.js" );
    final ClientListener clientListener = new ClientListener( scriptCode );
    ( new Listener() {
      @SuppressWarnings("unused")
      private String preFix = "Click:";
      @SuppressWarnings("unused")
      private int initNumber = 1;
      public ClientListener getClientImpl() {
        return clientListener;
      }
    } ).addTo( control, ClientListener.MouseDown );
  }

  public static void addLoggerBehavior( Widget widget ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Logger.js" );
    final ClientListener clientListener = new ClientListener( scriptCode );
    Listener listener = new Listener() {
      public ClientListener getClientImpl() {
        return clientListener;
      }
    };
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

  public static void addCopyContentBehavior( final Text text1, final Text text2 ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "CopyContent.js" );
    final ClientListener clientListener = new ClientListener( scriptCode );
    ( new Listener() {
      @SuppressWarnings("unused")
      private Text target = text2;
      public ClientListener getClientImpl() {
        return clientListener;
      }
    } ).addTo( text1, ClientListener.Modify );
    ( new Listener() {
      @SuppressWarnings("unused")
      private Text target = text1;
      public ClientListener getClientImpl() {
        return clientListener;
      }
    } ).addTo( text2, ClientListener.Modify );
  }


}
