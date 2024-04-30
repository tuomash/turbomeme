package com.turbomeme.datastorage;

import org.javalite.activejdbc.Base;

/**
 * Database connection.
 */
public final class Connection
{
  private static final String DB_DRIVER = "org.postgresql.Driver";
  private static final String DB_HOST = "jdbc:postgresql://" + System.getProperty("turbomeme.db.host") + ":"
      + System.getProperty("turbomeme.db.port") + "/" + System.getProperty("turbomeme.db.name");
  private static final String DB_USERNAME = System.getProperty("turbomeme.db.username");
  private static final String DB_PASSWORD = System.getProperty("turbomeme.db.password");

  public void open()
  {
    Base.open(DB_DRIVER, DB_HOST, DB_USERNAME, DB_PASSWORD);
  }

  public void close()
  {
    Base.close();
  }
}
