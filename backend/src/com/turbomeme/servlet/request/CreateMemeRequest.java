package com.turbomeme.servlet.request;

import com.google.common.base.Preconditions;
import com.turbomeme.servlet.InvalidInputException;
import com.turbomeme.util.JSONUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;

/**
 * Meme creation request.
 *
 * @author Tuomas Hynninen (tuomas.hynninen@gmail.com)
 */
public final class CreateMemeRequest
{
  private Integer memeImageId;
  private JSONObject memeImageContent;

  public CreateMemeRequest(final Integer memeImageId, final JSONObject memeImageContent)
  {
    Preconditions.checkNotNull(memeImageId, "Meme image id cannot be null!");
    Preconditions.checkNotNull(memeImageContent, "Meme image content cannot be null!");

    this.memeImageId = memeImageId;
    this.memeImageContent = memeImageContent;
  }

  public static CreateMemeRequest parse(final HttpServletRequest request) throws InvalidInputException
  {
    Preconditions.checkNotNull(request, "Request cannot be null!");

    final String memeImageIdStr = request.getParameter("meme-image-id");

    if (memeImageIdStr == null)
    {
      throw new InvalidInputException("Meme image id string was null!");
    }
    else if (memeImageIdStr.isEmpty())
    {
      throw new InvalidInputException("Meme image id string was empty!");
    }

    final Integer memeImageId;

    try
    {
      memeImageId = Integer.parseInt(memeImageIdStr);
    }
    catch (final NumberFormatException e)
    {
      throw new InvalidInputException("Couldn't parse meme image id! [memeImageId=" + memeImageIdStr + "]", e);
    }

    final String jsonStr = request.getParameter("json");

    if (jsonStr == null)
    {
      throw new InvalidInputException("Meme json string was null!");
    }
    else if (jsonStr.isEmpty())
    {
      throw new InvalidInputException("Meme json string was empty!");
    }

    final JSONObject memeImageContent;

    try
    {
      memeImageContent = JSONUtil.parseObject(jsonStr);
    }
    catch (final ParseException e)
    {
      throw new InvalidInputException("Couldn't parse json!", e);
    }

    return new CreateMemeRequest(memeImageId, memeImageContent);
  }

  public Integer getMemeImageId()
  {
    return memeImageId;
  }

  public JSONObject getMemeImageContent()
  {
    return memeImageContent;
  }
}