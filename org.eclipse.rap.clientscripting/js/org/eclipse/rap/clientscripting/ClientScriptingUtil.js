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
 
qx.Class.createNamespace( "org.eclipse.rap.clientscripting", {} );
 
org.eclipse.rap.clientscripting.ClientScriptingUtil = {

  _wrapperHelper : function(){},
  
  _getterMapping : {
    "org.eclipse.rwt.widgets.Text" : {
      "getText" : function( widget ) { return function() { return widget.getValue(); }; },
      "getSelection" : function( widget ) { return function() { return widget.getSelection(); };
      }
    }
  },

  getNativeEventType : function( source, eventType ) {
    var SWT = org.eclipse.rap.clientscripting.SWT;
    var result;
    switch( eventType ) { // TODO [tb] : Implementing keydown as traverse event?
      case SWT.KeyDown:
        result = "keypress";
      break;
      case SWT.KeyUp:
        result = "keyup";
      break;
      case SWT.MouseDown:
        result = "mousedown";
      break;
      case SWT.MouseUp:
        result = "mouseup";
      break;
      case SWT.MouseMove:
        result = "mousemove";
      break;
      case SWT.MouseEnter:
        result = "mouseover";
      break;
      case SWT.MouseExit:
        result = "mouseout";
      break;
      case SWT.MouseDoubleClick:
        result = "dblclick";
      break;
      case SWT.FocusIn:
        result = "focus";
      break;
      case SWT.FocusOut:
        result = "blur";
      break;
    }
    if( source.classname === "org.eclipse.rwt.widgets.Text" ) {
      switch( eventType ) {
        case SWT.Verify:
          result = "input"; // TODO [tb] : does currently not react on programatic changes
        break;
        case SWT.Modify:
          result = "changeValue";
        break;
      }
    }
    return result;
  },

  wrapAsProto : function( object ) {
    this._wrapperHelper.prototype = object;
    var result = new this._wrapperHelper();
    this._wrapperHelper.prototype = null;
    return result;
  },
  
  disposeObject : function( object ) {
    for( var key in object ) {
      if( object.hasOwnProperty( key ) ) {
        object[ key ] = null;
      }
    }
  },
  
  postProcessEvent : function( event, wrappedEvent, originalEvent ) {
    var SWT = org.eclipse.rap.clientscripting.SWT;
    switch( event.type ) {
      case SWT.Verify:
        this._postProcessVerifyEvent( event, wrappedEvent, originalEvent );
      break;
      case SWT.KeyDown:
      case SWT.KeyUp:
        this._postProcessKeyEvent( event, wrappedEvent, originalEvent );
      break;
    }
  },

  attachSetter : function( proxy, source ) {
    var ObjectManager = org.eclipse.rwt.protocol.ObjectManager;
    var id = ObjectManager.getId( source );
    var properties = ObjectManager.getEntry( id ).adapter.properties;
    for( var i = 0; i < properties.length; i++ ) {
      var property = properties[ i ];
      proxy[ "set" + qx.lang.String.toFirstUp( property ) ] = 
        this._createSetter( id, property );
    }
  },

  attachGetter : function( proxy, source ) {
    var getterMap = this._getterMapping[ source.classname ];
    for( var key in getterMap ) {
      proxy[ key ] = getterMap[ key ]( source );
    }
  },
  
  attachUserData : function( proxy, source ) {
    var setter = this._setUserData;
    var getter = this._getUserData;
    proxy.setData = function( property, value ) {
      setter( source, arguments );
    };
    proxy.getData = function( property ) {
      return getter( source, arguments );
    };
  },
  
  addDisposeListener : function( widget, listener ) {
    var orgDestroy = widget.destroy;
    widget.destroy = function() {
      listener( this );
      orgDestroy.call( widget );
    };
  },

  initEvent : function( event, type, originalEvent ) {
    var SWT = org.eclipse.rap.clientscripting.SWT;
    var control = org.eclipse.swt.WidgetUtil.getControl( originalEvent.getTarget() );
    event.widget = org.eclipse.rap.clientscripting.WidgetProxy.getInstance( control );
    event.type = type;
    switch( type ) {
      case SWT.KeyDown:
      case SWT.KeyUp:
        this._initKeyEvent( event, originalEvent );
      break;
      case SWT.MouseDown:
      case SWT.MouseUp:
      case SWT.MouseMove:
      case SWT.MouseEnter:
      case SWT.MouseExit:
      case SWT.MouseDoubleClick:
        this._initMouseEvent( event, originalEvent );
      break;
      case SWT.Verify:
        this._initVerifyEvent( event, originalEvent );
      break;
    }
  },

  _createSetter : function( id, property ) {
    var setProperty = this._setProperty;
    var result = function( value ) {
      setProperty( id, property, value );
    };
    return result;
  },

  _setProperty : function( id, property, value ) {
    var props = {};
    props[ property ] = value;
    org.eclipse.rwt.protocol.Processor.processOperation( {
      "target" : id,
      "action" : "set",
      "properties" : props
    } );
  },
  
  _setUserData : function( source, args) {
    if( args.length !== 2 ) {
      var msg =  "Wrong number of arguments in SetData: Expected 2, found " + args.length;
      throw new Error( msg );
    }
    var property = args[ 0 ];
    var value = args[ 1 ];
    var USERDATA_KEY = org.eclipse.rap.clientscripting.WidgetProxy._USERDATA_KEY;
    var data = source.getUserData( USERDATA_KEY );
    if( data == null ) {
      data = {};
      source.setUserData( USERDATA_KEY, data );
    }
    data[ property ] = value;
  },
  
  _getUserData : function( source, args ) {
    if( args.length !== 1 ) {
      var msg =  "Wrong number of arguments in SetData: Expected 1, found " + args.length;
      throw new Error( msg );
    }
    var property = args[ 0 ];
    var result = null;
    var USERDATA_KEY = org.eclipse.rap.clientscripting.WidgetProxy._USERDATA_KEY;
    var data = source.getUserData( USERDATA_KEY );
    if( data !== null && typeof data[ property ] !== "undefined" ) {
      result = data[ property ];
    }
    return result;
  },

  _initKeyEvent : function( event, originalEvent ) {
    var charCode = originalEvent.getCharCode();
    var SWT = org.eclipse.rap.clientscripting.SWT;
    if( charCode !== 0 ) {
      event.character = String.fromCharCode( charCode );
      // TODO [tb] : keyCode will be off when character is not a-z
      event.keyCode = event.character.toLowerCase().charCodeAt( 0 );
    } else {
      var keyCode = this._getLastKeyCode();
      switch( keyCode ) {
        case 16: 
          event.keyCode = SWT.SHIFT;
        break;
        case 17: 
          event.keyCode = SWT.CTRL;
        break;
        case 18: 
          event.keyCode = SWT.ALT;
        break;
        case 224: 
          event.keyCode = SWT.COMMAND;
        break;
        default:
          event.keyCode = keyCode;
        break;
      }
    }
    this._setStateMask( event, originalEvent );
  },

  _initMouseEvent : function( event, originalEvent ) {
    var target = originalEvent.getTarget()._getTargetNode();
    var offset = qx.bom.element.Location.get( target, "scroll" );
    event.x = originalEvent.getPageX() - offset.left;
    event.y = originalEvent.getPageY() - offset.top;
    if( originalEvent.isLeftButtonPressed() ) {
      event.button = 1;
    } else if( originalEvent.isRightButtonPressed() ) {
      event.button = 3;
    } if( originalEvent.isMiddleButtonPressed() ) {
      event.button = 2;
    }
    this._setStateMask( event, originalEvent );
  },
  
  _initVerifyEvent : function( event, originalEvent ) {
    var text = originalEvent.getTarget();
    if( text.classname === "org.eclipse.rwt.widgets.Text" ) {
      var keyCode = this._getLastKeyCode();
      var newValue = text.getComputedValue();
      var oldValue = text.getValue();
      var oldSelection = text.getSelection();
      var diff = this._getDiff( newValue, oldValue, oldSelection, keyCode );
      if(    diff[ 0 ].length === 1 
          && diff[ 1 ] === diff[ 2 ]
          && diff[ 0 ] === originalEvent.getData()
      ) {
        event.keyCode = keyCode;
        event.character = diff[ 0 ];
      }
      event.text = diff[ 0 ];
      event.start = diff[ 1 ];
      event.end = diff[ 2 ];
    }
  },
  
  _postProcessVerifyEvent : function( event, wrappedEvent, originalEvent ) {
    var widget = originalEvent.getTarget();
    if( wrappedEvent.doit !== false ) {
      if( event.text !== wrappedEvent.text && event.text !== "" ) {
        // insert replacement text
        originalEvent.preventDefault();
        var currentText = widget.getValue();
        var textLeft = currentText.slice( 0, event.start );
        var textRight = currentText.slice( event.end, currentText.length );
        var carret = textLeft.length + wrappedEvent.text.length;
        widget.setValue( textLeft + wrappedEvent.text + textRight );
        widget.setSelection( [ carret, carret ] );
      }
    } else {
      // undo any change
      originalEvent.preventDefault();
      widget._renderValue();
      widget._renderSelection();
    }
  },

  _postProcessKeyEvent : function( event, wrappedEvent, originalEvent ) {
    if( wrappedEvent.doit === false ) {
      originalEvent.preventDefault();
    }
  },

  _setStateMask : function( event, originalEvent ) {
    var SWT = org.eclipse.rap.clientscripting.SWT;
    event.stateMask |= originalEvent.isShiftPressed() ? SWT.SHIFT : 0;
    event.stateMask |= originalEvent.isCtrlPressed() ? SWT.CTRL : 0;
    event.stateMask |= originalEvent.isAltPressed() ? SWT.ALT : 0;
    event.stateMask |= originalEvent.isMetaPressed() ? SWT.COMMAND : 0;
  },

  _getLastKeyCode : function() {
    // NOTE : While this is a private field, this mechanism must be integrated with 
    // KeyEventSupport anyway to support the doit flag better.
    return org.eclipse.rwt.KeyEventSupport.getInstance()._currentKeyCode;
  },

  _getDiff : function( newValue, oldValue, oldSel, keyCode ) {
    var start;
    var end;
    var text;
    if( newValue.length >= oldValue.length || oldSel[ 0 ] !== oldSel[ 1 ] ) {
      start = oldSel[ 0 ];
      end = oldSel[ 1 ];
      text = newValue.slice( start, newValue.length - ( oldValue.length - oldSel[ 1 ] ) );
    } else {
      text = "";
      if(    oldSel[ 0 ] === oldSel[ 1 ] 
          && keyCode === 8 // backspace
          && ( oldValue.length - 1 ) === newValue.length  
      ) { 
        start = oldSel[ 0 ] - 1;
        end = oldSel[ 0 ];
      } else {
        start = oldSel[ 0 ];
        end = start + oldValue.length - newValue.length;
      }
    }
    return [ text, start, end ];
  }

};