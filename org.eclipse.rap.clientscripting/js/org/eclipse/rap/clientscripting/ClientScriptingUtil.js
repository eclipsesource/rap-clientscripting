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
  
  createFunction : function( code ) {
    var wrappedFunction = "var result = " + code + ";result;";
    var result = eval( wrappedFunction );
    if( typeof result !== "function" ) {
      throw new Error( "JavaScript code returns " + typeof result + ", must be function" );
    }
    return result;
  },
  
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
  
  initEvent : function( event, type, originalEvent ) {
    var control = org.eclipse.swt.WidgetUtil.getControl( originalEvent.getTarget() );
    event.widget = org.eclipse.rap.clientscripting.WidgetProxy.getInstance( control );
    switch( type ) {
      case "KeyDown":
        this._initKeyEvent( event, originalEvent );
      break;
    }
  },
  
  _initKeyEvent : function( event, originalEvent ) {
    event.character = String.fromCharCode( originalEvent.getCharCode() );
  }

};