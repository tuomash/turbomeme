package main.java.com.turbomeme.util;

import com.google.common.base.Preconditions;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JSON utility functions.
 */
public final class JSONUtils
{
  public static JSONObject parseObject(final String jsonStr)
  {
    Preconditions.checkNotNull(jsonStr, "Json string was null!");
    Preconditions.checkArgument(!jsonStr.isEmpty(), "Json string was empty!");
    return new JSONObject(jsonStr);
  }

  public static JSONArray parseArray(final String jsonStr)
  {
    Preconditions.checkNotNull(jsonStr, "Json string was null!");
    Preconditions.checkArgument(!jsonStr.isEmpty(), "Json string was empty!");
    return new JSONArray(jsonStr);
  }
}
