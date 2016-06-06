package com.turbomeme.servlet;

import com.turbomeme.datamodel.Meme;
import com.turbomeme.datastorage.MemeDAO;
import com.turbomeme.image.ImageDataGenerator;
import com.turbomeme.image.MemeBank;
import com.turbomeme.image.MemeImage;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class SaveMemeServlet extends HttpServlet
{
  private static final Logger log;

  static
  {
    log = LoggerFactory.getLogger(SaveMemeServlet.class);
  }

  private MemeBank memeBank;

  @Override
  public void init(final ServletConfig config) throws ServletException
  {
    super.init(config);
    final URL url;

    try
    {
      url = config.getServletContext().getResource("/WEB-INF/classes/image-data.json");
    }
    catch (final MalformedURLException e)
    {
      throw new ServletException("Couldn't load meme bank!", e);
    }

    memeBank = ImageDataGenerator.loadMemeBank(url.getPath());
  }

  @Override
  protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      response.sendRedirect(process(request));
    }
    catch (final InvalidInputException e)
    {
      log.error(e.getMessage(), e);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    catch (final Exception e)
    {
      log.error(e.getMessage(), e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private String process(final HttpServletRequest request) throws InvalidInputException
  {
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
      throw new InvalidInputException("Couldn't parse meme image id! " +
          "[memeImageId=" + memeImageIdStr + "]", e);
    }

    if (!memeBank.hasMeme(memeImageId))
    {
      throw new InvalidInputException("No such meme! [memeImageId=" +
          memeImageId + "]");
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

    final JSONParser parser = new JSONParser();
    final Object jsonObj;

    try
    {
      jsonObj = parser.parse(jsonStr);
    }
    catch (final ParseException e)
    {
      throw new InvalidInputException("Couldn't parse meme json string! " +
          "[jsonStr=" + jsonStr + "]", e);
    }

    final JSONObject json = (JSONObject) jsonObj;

    if (!json.containsKey("objects"))
    {
      throw new InvalidInputException("Meme json doesn't contain key 'objects'!" + "[jsonStr=" + jsonStr + "]");
    }

    final JSONArray objects = (JSONArray) json.get("objects");

    if (objects.isEmpty())
    {
      throw new InvalidInputException("Objects array is empty! [jsonStr=" +
          jsonStr + "]");
    }

    final MemeImage image = memeBank.getMeme(memeImageId);

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
            "host! [uriHost=" + uri.getHost() + ", serverHost=" +
            Environment.HOST + "]");
      }

      final String path = StringUtils.substring(uri.getPath(), 1);

      if (!image.getWwwPath().equals(path))
      {
        throw new InvalidInputException("Input URI path doesn't match meme " +
            "image's path! [path=" + path + ", memeImagePath=" +
            image.getWwwPath() + "]");
      }
    }

    final Meme meme = MemeDAO.insert(jsonStr, image.getWidth(), image.getHeight(), image.getId());
    return meme.createURL();
  }
}