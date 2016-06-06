package com.turbomeme.datastorage;

import org.javalite.activejdbc.Base;

public final class Connection
{
  private static final String DB_DRIVER;
  private static final String DB_HOST;
  private static final String DB_USERNAME;
  private static final String DB_PASSWORD;

  static
  {
    DB_DRIVER = "org.postgresql.Driver";
    DB_HOST = System.getProperty("turbomemeDbHost");
    DB_USERNAME = System.getProperty("turbomemeDbUsername");
    DB_PASSWORD = System.getProperty("turbomemeDbPassword");
  }

  public static void open()
  {
    Base.open(DB_DRIVER, DB_HOST, DB_USERNAME, DB_PASSWORD);
  }

  public static void close()
  {
    Base.close();
  }
}