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
 
org.eclipse.rap.clientscripting.EventBinding = function( source, eventType, targetFunction ) {
  try {
    var ClientScriptingUtil = org.eclipse.rap.clientscripting.ClientScriptingUtil;
    ClientScriptingUtil.prepareSource( source, eventType );
    this._nativeType = ClientScriptingUtil.getNativeEventType( source, eventType );
    this._eventType = eventType;
    this._targetFunction = targetFunction;
    this._source = source;
    this._bind();
  } catch( ex ) {
    throw new Error( "Could not create EventBinding " + eventType + ":" + ex.message );
  }
};

org.eclipse.rap.clientscripting.EventBinding.prototype = {

  _bind : function() {
    this._source.addEventListener( this._nativeType, this._processEvent, this );
  },
  
  _unbind : function() {
    this._source.removeEventListener( this._nativeType, this._processEvent, this );
  },

  _processEvent : function( event ) {
    var ClientScriptingUtil = org.eclipse.rap.clientscripting.ClientScriptingUtil;
    var EventProxy = org.eclipse.rap.clientscripting.EventProxy;
    var eventProxy = new EventProxy( this._eventType, event );
    var wrappedEventProxy = ClientScriptingUtil.wrapAsProto( eventProxy );
    this._targetFunction.call( wrappedEventProxy );
    ClientScriptingUtil.postProcessEvent( eventProxy, wrappedEventProxy, event );
    EventProxy.disposeEventProxy( eventProxy );
  },

  dispose : function() {
    var ClientScriptingUtil = org.eclipse.rap.clientscripting.ClientScriptingUtil;
    ClientScriptingUtil.restoreSource( this._source, this._eventType );
    this._unbind();
    this._source = null;
    this._targetFunction = null;
  }

};
  