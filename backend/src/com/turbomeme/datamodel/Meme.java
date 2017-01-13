package com.turbomeme.datamodel;

import com.turbomeme.servlet.Environment;
import org.javalite.activejdbc.Model;

/**
 * Meme data model.
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
    return Environment.createRootURL() + "m/" + get("hash");
  }
}