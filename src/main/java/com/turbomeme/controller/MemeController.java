package com.turbomeme.controller;

import com.google.common.base.Preconditions;
import com.turbomeme.datamodel.Meme;
import com.turbomeme.datastorage.MemeDAO;
import com.turbomeme.image.MemeBank;
import com.turbomeme.image.MemeImage;
import com.turbomeme.servlet.Environment;
import com.turbomeme.servlet.InvalidInputException;
import com.turbomeme.servlet.request.CreateMemeRequest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Meme controller.
 */
public final class MemeController
{
  private final MemeBank memeBank;

  public MemeController(final MemeBank memeBank)
  {
    Preconditions.checkNotNull(memeBank, "Meme bank cannot be null!");
    this.memeBank = memeBank;
  }

  public Meme create(final CreateMemeRequest request) throws InvalidInputException
  {
    return create(request.getMemeImageId(), request.getMemeImageContent());
  }

  public Meme create(final Integer memeImageId, final JSONObject memeImageContent) throws InvalidInputException
  {
    if (!memeBank.hasMeme(memeImageId))
    {
      throw new InvalidInputException("No such meme! [memeImageId=" + memeImageId + "]");
    }

    final MemeImage image = memeBank.getMeme(memeImageId);
    validateImageContent(image, memeImageContent);
    return MemeDAO.insert(memeImageContent.toString(), image.getWidth(), image.getHeight(), image.getId());
  }

  public void validateImageContent(final MemeImage image, final JSONObject json) throws InvalidInputException
  {
    if (!json.has("objects"))
    {
      throw new InvalidInputException("Meme json doesn't contain key 'objects'!");
    }

    final JSONArray objects = (JSONArray) json.get("objects");

    if (objects.isEmpty())
    {
      throw new InvalidInputException("Objects array is empty!");
    }

    for (int i = 0; i < objects.length(); i++)
    {
      final JSONObject obj = (JSONObject) objects.get(i);
      final String type = (String) obj.get("type");

      // Only validate images
      if (!type.equals("image"))
      {
        continue;
      }

      if (!obj.has("src"))
      {
        throw new InvalidInputException("Object json doesn't contain key 'src'!" + "[obj=" + obj + "]");
      }

      final String uriStr = (String) obj.get("src");
      final URI uri;

      try
      {
        uri = new URI(uriStr);
      }
      catch (final URISyntaxException e)
      {
        throw new InvalidInputException("Invalid URI!", e);
      }

      if (!uri.getHost().equals(Environment.HOST))
      {
        throw new InvalidInputException("Input URI host doesn't match server " +
            "host! [uriHost=" + uri.getHost() + ", serverHost=" + Environment.HOST + "]");
      }

      final String imagePath;

      if (!Environment.NAME.isEmpty())
      {
        imagePath = "/" + Environment.NAME + "/" + image.getWwwPath();
      }
      else
      {
        imagePath = "/" + image.getWwwPath();
      }

      if (!imagePath.equals(uri.getPath()))
      {
        throw new InvalidInputException("Input URI path doesn't match meme image's path! " +
            "[path=" + uri.getPath() + ", memeImagePath=" + imagePath + "]");
      }
    }
  }
}
