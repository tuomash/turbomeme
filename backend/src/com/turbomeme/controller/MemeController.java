package com.turbomeme.controller;

import com.google.common.base.Preconditions;
import com.turbomeme.datamodel.Meme;
import com.turbomeme.datastorage.MemeDAO;
import com.turbomeme.image.MemeBank;
import com.turbomeme.image.MemeImage;
import com.turbomeme.servlet.Environment;
import com.turbomeme.servlet.InvalidInputException;
import com.turbomeme.servlet.request.CreateMemeRequest;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public final class MemeController
{
  private static final Logger log = LoggerFactory.getLogger(MemeController.class);

  private MemeBank memeBank;
  private Cache cache;

  public MemeController(final MemeBank memeBank)
  {
    Preconditions.checkNotNull(memeBank, "Meme bank cannot be null!");
    this.memeBank = memeBank;

    // Configure Ehcache
    final CacheManager manager = CacheManager.getInstance();
    cache = manager.getCache("htmlCache");
  }

  public Meme create(final CreateMemeRequest request) throws InvalidInputException, ParseException
  {
    return create(request.getMemeImageId(), request.getMemeImageContent());
  }

  public Meme create(final Integer memeImageId, final JSONObject memeImageContent) throws InvalidInputException, ParseException
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
    if (!json.containsKey("objects"))
    {
      throw new InvalidInputException("Meme json doesn't contain key 'objects'!");
    }

    final JSONArray objects = (JSONArray) json.get("objects");

    if (objects.isEmpty())
    {
      throw new InvalidInputException("Objects array is empty!");
    }

    for (int i = 0; i < objects.size(); i++)
    {
      final JSONObject obj = (JSONObject) objects.get(i);
      final String type = (String) obj.get("type");

      // Only validate images
      if (!type.equals("image"))
      {
        continue;
      }

      if (!obj.containsKey("src"))
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
        throw new InvalidInputException(e);
      }

      if (!uri.getHost().equals(Environment.HOST))
      {
        throw new InvalidInputException("Input URI host doesn't match server " +
            "host! [uriHost=" + uri.getHost() + ", serverHost=" + Environment.HOST + "]");
      }

      final String path = StringUtils.substring(uri.getPath(), 1);

      if (!image.getWwwPath().equals(path))
      {
        throw new InvalidInputException("Input URI path doesn't match meme image's path! " +
            "[path=" + path + ", memeImagePath=" + image.getWwwPath() + "]");
      }
    }
  }
}