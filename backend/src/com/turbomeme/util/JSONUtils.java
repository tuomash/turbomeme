package com.turbomeme.util;

import com.google.common.base.Preconditions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * JSON utility functions.
 *
 * @author Tuomas Hynninen (tuomas.hynninen@gmail.com)
 */
public final class JSONUtils
{
  public static JSONObject parseObject(final String jsonStr) throws ParseException
  {
    Preconditions.checkNotNull(jsonStr, "Json string was null!");
    Preconditions.checkArgument(!jsonStr.isEmpty(), "Json string was empty!");
    return (JSONObject) new JSONParser().parse(jsonStr);
  }

  public static JSONArray parseArray(final String jsonStr) throws ParseException
  {
    Preconditions.checkNotNull(jsonStr, "Json string was null!");
    Preconditions.checkArgument(!jsonStr.isEmpty(), "Json string was empty!");
    return (JSONArray) new JSONParser().parse(jsonStr);
  }
}