package main.java.com.turbomeme.servlet;

/**
 * Global environment variables.
 */
public final class Environment
{
  public static final String ROOT_URL;

  public static String PROTOCOL = "http://";
  public static String HOST = "localhost";
  public static String PORT = "8080";

  static
  {
    final String protocol = System.getProperty("protocol");

    if (protocol != null && !protocol.isEmpty())
    {
      PROTOCOL = protocol;
    }

    final String host = System.getProperty("host");

    if (host != null && !host.isEmpty())
    {
      HOST = host;
    }

    final String port = System.getProperty("port");

    if (port != null && !port.isEmpty())
    {
      PORT = port;
    }

    ROOT_URL = PROTOCOL + HOST + PORT + "/";
  }
}
