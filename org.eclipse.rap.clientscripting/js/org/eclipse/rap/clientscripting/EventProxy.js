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
  
  /**
   * An object representing the widget that issued the event. 
   * It has setter and getter named after the properties used in the RAP protocol. 
   * Only a subset of getter is currently supported.
   * (See org.eclipse.rap.clientscripting.ClientScriptingUtil#attachGetter.)
   * Setting properties might result in server and client getting out-of-sync in RAP 1.5,
   * unless it is a property that can be changed by user-input (e.g. selection).
   */
  widget : null,

  /**
   * depending on the event, a flag indicating whether the operation should be
   * allowed. Setting this field to false will cancel the operation.
   * Currently only supports preventing input into a Text or Text-like widget.
   */
  doit : true,
  
  /**
   * depending on the event, the character represented by the key that was
   * typed. This is the final character that results after all modifiers have
   * been applied. For non-printable keys (like arrow-keys) this field is not set.
   * Changing its value has no effect.  
   */
  character : '\u0000',
  
  /**
   * Currently not supported.
   */
  keyCode : 0,
    
};

org.eclipse.rap.clientscripting.EventProxy.disposeEventProxy = function( eventProxy ) {
  eventProxy.widget = null;
};