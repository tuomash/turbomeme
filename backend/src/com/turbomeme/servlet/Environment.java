package com.turbomeme.servlet;

import com.google.common.base.Preconditions;

/**
 * Global environment.
 *
 * @author Tuomas Hynninen (tuomas.hynninen@gmail.com)
 */
public final class Environment
{
  public static final String PROTOCOL;
  public static final String HOST;
  public static final String PORT;
  public static final String ROOT_URL;

  static
  {
    PROTOCOL = "http://";
    HOST = System.getProperty("turbomemeHost");

    Preconditions.checkNotNull(PROTOCOL, "Protocol cannot be null!");
    Preconditions.checkNotNull(!PROTOCOL.isEmpty(), "Protocol cannot be empty!");
    Preconditions.checkNotNull(HOST, "Host cannot be null!");
    Preconditions.checkState(!HOST.isEmpty(), "Host cannot be empty!");

    PORT = System.getProperty("turbomemePort");

    String url = PROTOCOL + PORT;

    if (PORT != null && !PORT.isEmpty())
    {
      url = url + ":" + PORT;
    }

    ROOT_URL = url + "/";
  }
}