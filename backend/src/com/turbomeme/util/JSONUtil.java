package com.turbomeme.util;

import com.google.common.base.Preconditions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class JSONUtil
{
  public static JSONObject parse(final String jsonStr) throws ParseException
  {
    Preconditions.checkNotNull(jsonStr, "Json string was null!");
    Preconditions.checkArgument(!jsonStr.isEmpty(), "Json string was empty!");
    return (JSONObject) new JSONParser().parse(jsonStr);
  }
}