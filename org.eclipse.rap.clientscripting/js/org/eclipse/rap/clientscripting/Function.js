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
 
 
org.eclipse.rap.clientscripting.Function = function( code ) {
  this._function = org.eclipse.rap.clientscripting.ClientScriptingUtil.createFunction( code );
};

org.eclipse.rap.clientscripting.Function.prototype = {

  call : function() {
    this._function.apply( window, arguments );
  }

};