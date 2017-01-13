package com.turbomeme.servlet;

import com.turbomeme.controller.MemeController;
import com.turbomeme.datamodel.Meme;
import com.turbomeme.image.Constants;
import com.turbomeme.image.ImageDataGenerator;
import com.turbomeme.template.Templates;
import freemarker.template.TemplateException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Global paths.
 *
 * @author Tuomas Hynninen (tuomas.hynninen@gmail.com)
 */
public abstract class AServlet extends HttpServlet implements Constants
{
  protected MemeController controller;
  protected Templates templates;
  protected Cache htmlCache;

  @Override
  public void init(final ServletConfig config) throws ServletException
  {
    super.init(config);

    // Initialize templates
    templates = new Templates(config.getServletContext(), "/WEB-INF/classes/");

    // Create a meme bank and initialize controller
    final URL url;

    try
    {
      url = config.getServletContext().getResource("/WEB-INF/classes/image-data.json");
    }
    catch (final MalformedURLException e)
    {
      throw new ServletException("Couldn't load meme JSON data file!", e);
    }

    controller = new MemeController(ImageDataGenerator.loadMemeBank(url.getPath()));

    // Get HTML cache instance
    final CacheManager manager = CacheManager.getInstance();
    htmlCache = manager.getCache("htmlCache");
  }

  public String printHtml(final Meme meme) throws IOException, TemplateException
  {
    // Setup template data
    final Map<String, Object> data = new HashMap<>();
    data.put("json", StringEscapeUtils.escapeHtml4((String) meme.get("data")));
    data.put("canvasWidth", meme.get("canvas_width"));
    data.put("canvasHeight", meme.get("canvas_height"));
    data.put("fabricJsPath", Environment.createRootURL() + "lib/fabricjs/1.5.0/fabric.min.js");
    data.put("templateJsPath", Environment.createRootURL() + "template.js");

    // Generate output html
    return templates.print("meme.ftl", data);
  }

  public void storeHtmlToCache(final String hash, final String html)
  {
    // Store to cache
    final Element element = new Element(hash, html);
    htmlCache.put(element);
  }
}