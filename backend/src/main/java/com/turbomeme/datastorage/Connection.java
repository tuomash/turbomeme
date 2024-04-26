package main.java.com.turbomeme.datastorage;

import org.javalite.activejdbc.Base;

/**
 * Database connection.
 */
public final class Connection
{
  private static final String DB_DRIVER = "org.postgresql.Driver";
  private static final String DB_HOST = System.getProperty("turbomemeDbHost");
  private static final String DB_USERNAME = System.getProperty("turbomemeDbUsername");
  private static final String DB_PASSWORD = System.getProperty("turbomemeDbPassword");

  public void open()
  {
    Base.open(DB_DRIVER, DB_HOST, DB_USERNAME, DB_PASSWORD);
  }

  public void close()
  {
    Base.close();
  }
}
