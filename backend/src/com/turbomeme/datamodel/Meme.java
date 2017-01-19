package com.turbomeme.datamodel;

import com.turbomeme.servlet.Environment;
import org.javalite.activejdbc.Model;

/**
 * Meme data model. See resource/schema.sql.
 *
 * @author Tuomas Hynninen (tuomas.hynninen@gmail.com)
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