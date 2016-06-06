package com.turbomeme.datamodel;

import com.turbomeme.servlet.Environment;
import org.javalite.activejdbc.Model;

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