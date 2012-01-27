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
 
org.eclipse.rap.clientscripting.WidgetProxy = function( originalWidget ) {
  var ClientScriptingUtil = org.eclipse.rap.clientscripting.ClientScriptingUtil;
  ClientScriptingUtil.attachSetter( this, originalWidget );
  ClientScriptingUtil.attachGetter( this, originalWidget );
};

org.eclipse.rap.clientscripting.WidgetProxy._USERDATA_KEY = 
  "org.eclipse.rap.clientscripting.WidgetProxy";

org.eclipse.rap.clientscripting.WidgetProxy.getInstance = function( widget ) {
  var protoInstance = widget.getUserData( this._USERDATA_KEY );
  if( protoInstance == null ) {
    protoInstance = new org.eclipse.rap.clientscripting.WidgetProxy( widget );
    widget.setUserData( this._USERDATA_KEY, protoInstance );
  }
  return org.eclipse.rap.clientscripting.ClientScriptingUtil.wrapAsProto( protoInstance );
}