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
/*global handleEvent:false */
org.eclipse.rap.clientscripting.Function = function( /* code */ ) {
  // NOTE: the eval'd code will have the same scope as this function, therefore no local
  // variables except the "imports" are used.
  org.eclipse.rap.clientscripting.Function.addFunction( this );
  var SWT = org.eclipse.rap.clientscripting.SWT;
  try {
    eval( this._getScopeScript( arguments[ 1 ] ) );
  } catch( ex ) {
    var msg = "Could not create ClientFunction scope: " + ( ex.message ? ex.message : ex );
    throw new Error( msg );
  }
  try {
    eval( arguments[ 0 ] );
  } catch( ex ) {
    var msg = "Could not parse ClientFunction: " + ( ex.message ? ex.message : ex );
    throw new Error( msg );
  }
  try {
    this._function = handleEvent; // TODO [tb] : allow specific function name(s) 
  } catch( ex ) {
    // handled in next if
  }
  if( typeof this._function !== "function" ) {
    throw new Error( "JavaScript code does not define a \"handleEvent\" function" );
  }
};

org.eclipse.rap.clientscripting.Function.prototype = {

  call : function( arg ) {
    this._function.call( window, arg );
  },

  _getScopeScript : function( scope ) {
    var ClientScriptingUtil = org.eclipse.rap.clientscripting.ClientScriptingUtil;
    var result =  ClientScriptingUtil.createScopeScript( scope ? scope : {}, this );
    return result;
  }

};

org.eclipse.rap.clientscripting.Function._db = {};

org.eclipse.rap.clientscripting.Function.addFunction = function( func ) {
  var hashCode = qx.core.Object.toHashCode( func );
  this._db[ hashCode ] = func;
},

org.eclipse.rap.clientscripting.Function.getFunction = function( hashCode ) {
  return this._db[ hashCode ];
}