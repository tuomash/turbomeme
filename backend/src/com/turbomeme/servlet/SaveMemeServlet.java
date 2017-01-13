package com.turbomeme.servlet;

import com.turbomeme.datamodel.Meme;
import com.turbomeme.servlet.request.CreateMemeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Saves new memes into the database.
 *
 * @author Tuomas Hynninen (tuomas.hynninen@gmail.com)
 */
public final class SaveMemeServlet extends AServlet
{
  private static final Logger log = LoggerFactory.getLogger(SaveMemeServlet.class);

  @Override
  protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException
  {
    try
    {
      // Create a new meme
      final Meme meme = controller.create(CreateMemeRequest.parse(request));

      // Print html and store it to cache for instant access
      storeHtmlToCache((String) meme.get("hash"), printHtml(meme));

      // Redirect user to meme's address
      response.sendRedirect(meme.createURL());
    }
    catch (final InvalidInputException e)
    {
      log.error("Invalid input!", e);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    catch (final Exception e)
    {
      log.error("Unhandled exception!", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}