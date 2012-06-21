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

(function(){

var ObjectManager = org.eclipse.rwt.protocol.ObjectManager;
var ClientScriptingUtil = org.eclipse.rap.clientscripting.ClientScriptingUtil;

org.eclipse.rap.clientscripting.WidgetProxy = function( originalWidget ) {
  var WidgetProxy = org.eclipse.rap.clientscripting.WidgetProxy;
  ClientScriptingUtil.attachSetter( this, originalWidget );
  ClientScriptingUtil.attachGetter( this, originalWidget );
  ClientScriptingUtil.attachUserData( this, originalWidget );
  this.setData( WidgetProxy._ID, ObjectManager.getId( originalWidget ) );
  ClientScriptingUtil.addDisposeListener( originalWidget, function() {
    WidgetProxy.disposeWidgetProxy( originalWidget );
  } );
};

org.eclipse.rap.clientscripting.WidgetProxy._PROXY_KEY = 
  "org.eclipse.rap.clientscripting.WidgetProxy.PROXY";

org.eclipse.rap.clientscripting.WidgetProxy._USERDATA_KEY = 
  "org.eclipse.rap.clientscripting.WidgetProxy.USERDATA";

org.eclipse.rap.clientscripting.WidgetProxy._ID = 
  "org.eclipse.rap.clientscripting.WidgetProxy.ID";

org.eclipse.rap.clientscripting.WidgetProxy.getInstance = function( widget ) {
  var protoInstance = widget.getUserData( this._PROXY_KEY );
  if( protoInstance == null ) {
    protoInstance = new org.eclipse.rap.clientscripting.WidgetProxy( widget );
    widget.setUserData( this._PROXY_KEY, protoInstance );
  }
  return ClientScriptingUtil.wrapAsProto( protoInstance );
};

org.eclipse.rap.clientscripting.WidgetProxy.disposeWidgetProxy = function( widget ) {
  var protoInstance = widget.getUserData( this._PROXY_KEY );
  var userData = widget.getUserData( this._USERDATA_KEY );
  ClientScriptingUtil.disposeObject( protoInstance );
  ClientScriptingUtil.disposeObject( userData );
};

}());