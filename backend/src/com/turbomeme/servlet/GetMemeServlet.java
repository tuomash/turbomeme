package com.turbomeme.servlet;

import com.turbomeme.datamodel.Meme;
import com.turbomeme.datastorage.MemeDAO;
import freemarker.template.TemplateException;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Returns the requested meme as an HTML page.
 *
 * @author Tuomas Hynninen (tuomas.hynninen@gmail.com)
 */
public final class GetMemeServlet extends AServlet
{
  private static final Logger log = LoggerFactory.getLogger(GetMemeServlet.class);

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      final String html = getHtml(request.getPathInfo());

      // Invalid input, show 404
      if (html == null)
      {
        final Map<String, Object> data = new HashMap<>();
        data.put("url", Environment.ROOT_URL);
        response.getWriter().write(templates.print("404.ftl", data));
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
      else
      {
        response.getWriter().write(html);
        response.setStatus(HttpServletResponse.SC_OK);
      }

      response.setContentType("text/html;charset=UTF-8");
    }
    catch (final TemplateException e)
    {
      log.error("Couldn't write 404 page!", e);
      response.getWriter().write("Internal server error");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    catch (final Exception e)
    {
      log.error("Unhandled exception!", e);
      response.getWriter().write("Internal server error");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private String getHtml(final String pathInfo) throws InvalidInputException, IOException, TemplateException
  {
    if (pathInfo == null)
    {
      log.debug("Path info cannot be null!");
      return null;
    }
    else if (pathInfo.isEmpty())
    {
      log.debug("Path info cannot be empty!");
      return null;
    }
    else if (pathInfo.length() != MEME_HASH_WITH_SLASH_LENGTH)
    {
      log.debug("Path info is of illegal length!");
      return null;
    }

    final String hash = StringUtils.substring(pathInfo, 1);

    // First check from cache
    final Element cachedHtml = htmlCache.get(hash);

    if (cachedHtml != null)
    {
      log.debug("Getting from cache. [hash=" + hash + "]");
      return (String) cachedHtml.getObjectValue();
    }
    else
    {
      // No cache hit, find from database
      final Meme meme = MemeDAO.find(hash);

      if (meme == null)
      {
        log.debug("Meme not found! [hash=" + hash + "]");
        return null;
      }

      final String html = printHtml(meme);
      storeHtmlToCache(hash, html);
      return html;
    }
  }
}