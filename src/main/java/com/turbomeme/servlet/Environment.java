package com.turbomeme.servlet;

/**
 * Global environment variables.
 */
public final class Environment
{
  public static final String ROOT_URL;

  public static String PROTOCOL = "http://";
  public static String HOST = "localhost";
  public static String PORT = "8080";
  public static String NAME = "";

  static
  {
    final String protocol = System.getProperty("turbomeme.app.protocol");

    if (protocol != null && !protocol.isEmpty())
    {
      PROTOCOL = protocol;
    }

    final String host = System.getProperty("turbomeme.app.host");

    if (host != null && !host.isEmpty())
    {
      HOST = host;
    }

    final String port = System.getProperty("turbomeme.app.port");

    if (port != null && !port.isEmpty())
    {
      PORT = port;
    }

    final String name = System.getProperty("turbomeme.app.name");

    if (name != null && !name.isEmpty())
    {
      NAME = name;
    }

    ROOT_URL = PROTOCOL + HOST + ":" + PORT + "/" + (!NAME.isEmpty() ? NAME + "/" : "");
  }
}
