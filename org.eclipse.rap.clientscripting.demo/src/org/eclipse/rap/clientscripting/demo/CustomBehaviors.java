package org.eclipse.rap.clientscripting.demo;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.clientscripting.ClientListener;
import org.eclipse.rap.clientscripting.JavaFunction;
import org.eclipse.rap.clientscripting.Listener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;


public class CustomBehaviors {

  private static final String RESOURCES_PREFIX = "org/eclipse/rap/clientscripting/demo/";

  private CustomBehaviors() {
    // prevent instantiation
  }

  public static void addUpperCaseBehavior( Text text ) {
    ( new Listener() {
      public String getClientImpl() {
        return ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "UpperCase.js" );
      }
      public void handleEvent( Event event ) {
        if( event.keyCode == 0 && event.text.length() > 1 ) {
          event.text = "dont paste here!";
        } else {
          event.text = event.text.toUpperCase();
        }
      }
    } ).addTo( text, ClientListener.Verify );
  }

  public static void addDigitsOnlyBehavior( Text text ) {
    ( new Listener() {
      public String getClientImpl() {
        return ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DigitsOnly.js" );
      }
      public void handleEvent( Event event ) {
        String regexp = "^[0-9]*$";
        Text widget = ( Text )event.widget;
        String text = widget.getText();
        if( !text.matches( regexp ) ) {
          widget.setBackground( new Color( widget.getDisplay(), new RGB( 255, 255, 128 ) ) );
          widget.setToolTipText( "Digits only!" );
        } else {
          widget.setBackground( null );
          widget.setToolTipText( null );
        }      }
    } ).addTo( text, ClientListener.Modify );  }

  public static void addDateFieldBehavior( Text text ) {
    text.setText( "__.__.____" );
    Listener listener = new Listener() {

      private int changeflag = 0; // not required in clientscripting, SWT only

      public String getClientImpl() {
        return ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "DateField.js" );
      }

      public void handleEvent( Event event ) {
        switch( event.type ) {
          case SWT.KeyDown:
            this.handleKeyDownEvent( event );
          break;
          case SWT.MouseDown:
          case SWT.MouseUp:
            this.handleMouseEvent( event );
          break;
          case SWT.Verify:
            this.handleVerifyEvent( event );
          break;
        }
      };

      private void handleKeyDownEvent( Event event ) {
        Text widget = ( Text )event.widget;
        char newCh = event.character;
        int keyCode = event.keyCode;
        int sel = widget.getSelection().x;
        String text = widget.getText();
        if( sel < text.length() && text.charAt( sel ) == '_' && isNumber( newCh ) ) {
          text = replaceNextChar( text, sel, newCh );
          System.out.println( text );
          sel++;
          if( sel < text.length() && text.charAt( sel ) == '.' ) {
            sel++;
          }
        } else if( keyCode == SWT.BS && sel > 0 ) {
          if( text.charAt( sel - 1 ) == '.' ) {
            sel--;
          }
          if( isNumber( text.charAt( sel - 1 ) ) ) {
            text = replacePrevChar( text, sel, '_' );
            sel--;
          } else {
            sel--;
          }
          event.doit = false;
        }
        changeflag = 1;
        widget.setText( text );
        changeflag = 0;
        widget.setSelection( sel );
        event.doit = false; // prevent key input 
      }

      private void handleMouseEvent( Event event ) {
        // not supported: 
        //event.doit = false; // prevent mouse selection change
      }

      private void handleVerifyEvent( Event event ) {
        if( changeflag == 0 && event.character == '\u0000' ) {
          event.doit = false; // prevent pasting
        }
      };

      private boolean isNumber( char character ) {
        int charCode = character;
        return charCode >= 48 && charCode <= 57;
      };

      private String replaceNextChar( String text, int sel, char value ) {
        String leftPart = text.substring( 0, sel );
        String rightPart = text.substring( sel + 1, text.length() );
        text = leftPart + value + rightPart;
        return text;
      };

      private String replacePrevChar( String text, int sel, char value ) {
        String leftPart = text.substring( 0, sel - 1 );
        String rightPart = text.substring( sel, text.length() );
        text = leftPart + value + rightPart;
        return text;
      };

    };
    listener.addTo( text, ClientListener.KeyDown );
    listener.addTo( text, ClientListener.Verify );
    listener.addTo( text, ClientListener.MouseUp );
    listener.addTo( text, ClientListener.MouseDown );
  }

  public static void addCounterBehavior( Control control ) {
    ( new Listener() {
      private String preFix = "Click: ";
      private int count = 0;
      public String getClientImpl() {
        return ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Counter.js" );
      }
      public void handleEvent( Event event ) {
        Button widget = ( Button ) event.widget;
        count++;
        widget.setText( preFix + count );
      }
    } ).addTo( control, ClientListener.MouseDown );
  }

  public static void addLoggerBehavior( Widget widget ) {
    Listener listener = new Listener() {
      public String getClientImpl() {
        return ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Logger.js" );
      }
      public void handleEvent(Event event) {
        System.out.println( event );
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

  public static void addCopyContentBehavior( final Text arg1, final Text arg2 ) {
    // NOTE : SWT fires a change event even if setText is used, unlike clientscripting, therefore the flag
    Listener listener = new Listener() {
      private Text text1 = arg1; // TODO [tb] : this naming restrictions are a problem?
      private Text text2 = arg2;
      private int changeFlag = 0; // boolean not yet supported by clientscripting sync
      public String getClientImpl() {
        return ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "CopyContent.js" );
      }
      public void handleEvent( Event event ) {
        if( changeFlag == 0 ) {
          changeFlag = 1;
          // cannot compare widgets in clientscripting, compare values instead:
          String value = ( ( Text )event.widget ).getText();
          if( !value.equals( text1.getText() ) ) { // prevent the selection from being reset
            text1.setText( value );
          }
          if( !value.equals( text2.getText() ) ) {
            text2.setText( value );
          }
          changeFlag = 0;
        }
      }
    };
    listener.addTo( arg1, ClientListener.Modify );
    listener.addTo( arg2, ClientListener.Modify );
  }

  public static void addHoverBehavior( final Label label ) {
    String scriptCode = ResourceLoaderUtil.readTextContent( RESOURCES_PREFIX + "Hover.js" );
    Map<String, Object> context = new HashMap<String, Object>();
    context.put( "showDialog", new JavaFunction() {
      public Object execute( Object[] args ) {
        MessageBox mb = new MessageBox( label.getShell(), SWT.OK | SWT.ICON_ERROR );
        mb.setText( "Illiterate?" );
        mb.setMessage( "I told you not to go there!" );
        mb.open();
        return null;
      }
    } );
    ClientListener listener = new ClientListener( scriptCode, context );
    listener.addTo( label, ClientListener.MouseEnter );
  }


}
