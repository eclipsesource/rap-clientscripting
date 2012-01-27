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
 
org.eclipse.rap.clientscripting.EventProxy = function( eventType, originalEvent ) {
  org.eclipse.rap.clientscripting.ClientScriptingUtil.initEvent( this, eventType, originalEvent );
};

org.eclipse.rap.clientscripting.EventProxy.prototype = {
  
  widget : null,

  doit : true,
  
  character : '\u0000',
  
  keyCode : 0,
    
};

org.eclipse.rap.clientscripting.EventProxy.disposeEventProxy = function( eventProxy ) {
  eventProxy.widget = null;
};