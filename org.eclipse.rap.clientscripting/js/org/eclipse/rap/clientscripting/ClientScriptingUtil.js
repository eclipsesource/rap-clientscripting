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
      "getText" : function( widget ) { return function() { return widget.getValue(); } },
      "getSelection" : function( widget ) { 
        return function() { 
          var start = widget.getSelectionStart();
          var length = widget.getSelectionLength();
          return result = [ start, start + length ]; 
        }
      }
    }
  },

  getNativeEventType : function( source, eventType ) {
    var SWT = org.eclipse.rap.clientscripting.SWT;
    var result;
    switch( eventType ) {
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
      case SWT.FocusIn:
        result = "focus";
      break;
      case SWT.FocusOut:
        result = "blur";
      break;
    }
    return result;
  },

  wrapAsProto : function( object ) {
    this._wrapperHelper.prototype = object;
    var result = new this._wrapperHelper();
    this._wrapperHelper.prototype = null;
    return result;
  },

  postProcessEvent : function( event, originalEvent ) {
    if( event.doit === false ) {
      originalEvent.preventDefault();
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

  initEvent : function( event, type, originalEvent ) {
    var SWT = org.eclipse.rap.clientscripting.SWT;
    var control = org.eclipse.swt.WidgetUtil.getControl( originalEvent.getTarget() );
    event.widget = org.eclipse.rap.clientscripting.WidgetProxy.getInstance( control );
    event.type = type;
    switch( type ) {
      case SWT.KeyDown:
        this._initKeyEvent( event, originalEvent );
      break;
      case SWT.MouseDown:
        this._initMouseEvent( event, originalEvent );
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

  _initKeyEvent : function( event, originalEvent ) {
    var charCode = originalEvent.getCharCode();
    var SWT = org.eclipse.rap.clientscripting.SWT;
    if( charCode !== 0 ) {
      event.character = String.fromCharCode( charCode );
      // TODO [tb] : keyCode will be off when character is not a-z
      event.keyCode = event.character.toLowerCase().charCodeAt( 0 );
    } else {
      // NOTE : While this is a private field, this mechanism must be integrated with 
      // KeyEventSupport anyway to support the doit flag better.
      var keyCode = org.eclipse.rwt.KeyEventSupport.getInstance()._currentKeyCode;
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

  _setStateMask : function( event, originalEvent ) {
    var SWT = org.eclipse.rap.clientscripting.SWT;
    event.stateMask |= originalEvent.isShiftPressed() ? SWT.SHIFT : 0;
    event.stateMask |= originalEvent.isCtrlPressed() ? SWT.CTRL : 0;
    event.stateMask |= originalEvent.isAltPressed() ? SWT.ALT : 0;
    event.stateMask |= originalEvent.isMetaPressed() ? SWT.COMMAND : 0;
  }

};