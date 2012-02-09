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
   * Currently only effective on key events for Text or Text-like widgets.
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
   * depending on the event, the key code of the key that was typed, as defined
   * by the key code constants in class <code>SWT</code>. When the character
   * field of the event is ambiguous, this field contains the unaffected value
   * of the original character. For example, typing Shift+M or M result in different
   * characters ( 'M' and 'm' ), but the same keyCode (109, character code for 'm').
   */
  keyCode : 0,
  
  /**
   * the type of event, as defined by the event type constants in class <code>SWT</code>.
   * Currently supports SWT.KeyDown
   */
  type : 0,

  /**
   * depending on the event, the state of the keyboard modifier keys and mouse
   * masks at the time the event was generated.
   */
  stateMask : 0,
   
  /**
   * the button that was pressed or released; 1 for the first button, 2 for the
   * second button, and 3 for the third button, etc.
   */
  button : 0,
  
  /**
   * x coordinate of the pointer at the time of the event
   */
  x : 0,
  
  /**
   * y coordinate of the pointer at the time of the event
   */
  y : 0,

  /**
   * depending on the event, the new text that will be inserted.
   * Setting this field will change the text that is about to
   * be inserted or deleted.
   */
  text : ""

};

org.eclipse.rap.clientscripting.EventProxy.disposeEventProxy = function( eventProxy ) {
  eventProxy.widget = null;
};