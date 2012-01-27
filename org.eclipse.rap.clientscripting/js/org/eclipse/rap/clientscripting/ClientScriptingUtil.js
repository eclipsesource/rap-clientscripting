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
  
  _eventTypeMapping : {
    "KeyDown" : "keypress"
  },
  
  _wrapperHelper : function(){},
  
  getNativeEventType : function( protocolAdapter, eventType ) {
    return this._eventTypeMapping[ eventType ];
  },
  
  getProtocolAdapter : function( object ) {
    var ObjectManager = org.eclipse.rwt.protocol.ObjectManager;
    var id = ObjectManager.getId( object );
    return ObjectManager.getEntry( id ).adapter;
  },
  
  wrapAsProto : function( object ) {
    this._wrapperHelper.prototype = object;
    var result = new this._wrapperHelper();
    this._wrapperHelper.prototype = null;
    return result;
  },
  
  postProcessEvent : function( eventProxy, event ) {
    if( eventProxy.doit === false ) {
      event.preventDefault();
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
    }
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
  
  _setStateMask : function( event, originalEvent ) {
    var SWT = org.eclipse.rap.clientscripting.SWT;
    event.stateMask |= originalEvent.isShiftPressed() ? SWT.SHIFT : 0;
    event.stateMask |= originalEvent.isCtrlPressed() ? SWT.CTRL : 0;
    event.stateMask |= originalEvent.isAltPressed() ? SWT.ALT : 0;
    event.stateMask |= originalEvent.isMetaPressed() ? SWT.COMMAND : 0;
  }

};