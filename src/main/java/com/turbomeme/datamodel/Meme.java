package com.turbomeme.datamodel;

import com.turbomeme.servlet.Environment;
import org.javalite.activejdbc.Model;

/**
 * Meme data model. See src/main/resources/schema.sql.
 */
public class Meme extends Model
{
  public Meme()
  {
  }

  public String createURL()
  {
    return Environment.ROOT_URL + "m/" + get("hash");
  }
}
