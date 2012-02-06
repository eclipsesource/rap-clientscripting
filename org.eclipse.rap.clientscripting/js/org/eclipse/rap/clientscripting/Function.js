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
 
// TODO [tb] : consider a name thats not a native Constructor ( "Function" )
org.eclipse.rap.clientscripting.Function = function( /* code */ ) {
  // NOTE: the eval'd code will have the same scope as this function, therefore no local
  // variables except the "imports" are used.
  var SWT = org.eclipse.rap.clientscripting.SWT;
  eval( arguments[ 0 ] );
  this._function = handleEvent; // TODO [tb] : allow multiple functions, specified by server 
  if( typeof this._function !== "function" ) {
    throw new Error( "JavaScript code returns " + typeof result + ", must be function" );
  }
};

org.eclipse.rap.clientscripting.Function.prototype = {

  call : function() {
    this._function.apply( window, arguments );
  }

};