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
  var ClientScriptingUtil = org.eclipse.rap.clientscripting.ClientScriptingUtil;
  var sourceType;
  this._nativeType = ClientScriptingUtil.getNativeEventType( sourceType, eventType );
  this._targetFunction = targetFunction;
  this._source = source;
  this._bind();
};

org.eclipse.rap.clientscripting.EventBinding.prototype = {
  
  _bind : function() {
    this._source.addEventListener( this._nativeType, this._targetFunction.call );
  },
  
  _unbind : function() {
    this._source.removeEventListener( this._nativeType, this._targetFunction.call );
  },

  dispose : function() {
    this._unbind();
    this._source = null;
    this._targetFunction = null;
  }

};
  