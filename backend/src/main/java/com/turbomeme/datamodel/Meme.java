package main.java.com.turbomeme.datamodel;

import main.java.com.turbomeme.servlet.Environment;
import org.javalite.activejdbc.Model;

/**
 * Meme data model. See resource/schema.sql.
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
