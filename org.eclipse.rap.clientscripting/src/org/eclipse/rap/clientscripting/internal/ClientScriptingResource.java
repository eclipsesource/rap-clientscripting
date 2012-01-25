/**
 * Copyright (c) 2012 EclipseSource
 * All rights reserved.
 */

package org.eclipse.rap.clientscripting.internal;

import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;


public class ClientScriptingResource implements IResource {

  private static final String PATH_PREFIX = "/org/eclipse/rap/clientscripting/";

  private String location;

  public ClientScriptingResource( String location ) {
    this.location = PATH_PREFIX + location;
  }

  public ClassLoader getLoader() {
    return ClientScriptingResource.class.getClassLoader();
  }

  public String getLocation() {
    return location;
  }

  public String getCharset() {
    return "UTF-8";
  }

  public RegisterOptions getOptions() {
    return RegisterOptions.VERSION;
  }

  public boolean isJSLibrary() {
    return true;
  }

  public boolean isExternal() {
    return false;
  }
}
