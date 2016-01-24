package com.turbomeme.servlet;

import com.turbomeme.datamodel.Meme;
import com.turbomeme.datastorage.MemeDAO;
import com.turbomeme.image.Constants;
import freemarker.template.*;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public final class GetMemeServlet extends HttpServlet implements Constants
{
	private static final Logger log;

	static
	{
		log = LoggerFactory.getLogger(GetMemeServlet.class);
	}

	private Configuration configuration;
	private Cache cache;

	@Override
	public void init(final ServletConfig config) throws ServletException
	{
		super.init(config);

		// Configure Freemarker templates
		configuration = new Configuration(Configuration.VERSION_2_3_22);
		configuration.setServletContextForTemplateLoading(
				config.getServletContext(), "/WEB-INF/classes/");
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(
				TemplateExceptionHandler.RETHROW_HANDLER);

		// Configure Ehcache
		final CacheManager manager = CacheManager.getInstance();
		cache = manager.getCache("htmlCache");
	}

	@Override
	protected void doGet(final HttpServletRequest request,
											 final HttpServletResponse response)
			throws ServletException, IOException
	{
		try
		{
			process(request.getPathInfo(), response);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		catch (final InvalidInputException e1)
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			log.error(e1.getMessage(), e1);
			final Template template = configuration.getTemplate("404.ftl");
			final Map<String, Object> data = new HashMap<>();
			data.put("url", Environment.createRootURL());

			try
			{
				template.process(data, response.getWriter());
			}
			catch (final TemplateException e2)
			{
				log.error(e2.getMessage(), e2);
			}
		}
		catch (final Exception e)
		{
			log.error(e.getMessage(), e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void process(final String pathInfo,
											 final HttpServletResponse response)
			throws InvalidInputException, IOException, TemplateException
	{
		if (pathInfo == null)
		{
			throw new InvalidInputException("Path info cannot be null!");
		}
		else if (pathInfo.isEmpty())
		{
			throw new InvalidInputException("Path info cannot be empty!");
		}
		else if (pathInfo.length() != MEME_HASH_WITH_SLASH_LENGTH)
		{
			throw new InvalidInputException("Path info is of illegal length!");
		}

		response.setContentType("text/html;charset=UTF-8");
		final String hash = StringUtils.substring(pathInfo, 1);

		// First check from cache
		final Element cachedHtml = cache.get(hash);
		final String html;

		if (cachedHtml != null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Getting from cache. [hash=" + hash + "]");
			}

			html = (String) cachedHtml.getObjectValue();
		}
		else
		{
			// Find from database
			final Meme meme = MemeDAO.find(hash);

			if (meme == null)
			{
				throw new InvalidInputException("Meme not found! [hash=" + hash + "]");
			}

			// Setup template and its data
			final Template template = configuration.getTemplate("meme.ftl");
			final Map<String, Object> data = new HashMap<>();
			data.put("json", StringEscapeUtils.escapeHtml4((String) meme.get("data")));
			data.put("canvasWidth", meme.get("canvas_width"));
			data.put("canvasHeight", meme.get("canvas_height"));
			data.put("fabricJsPath", Environment.createRootURL() +
					"lib/fabricjs/1.5.0/fabric.min.js");
			data.put("templateJsPath", Environment.createRootURL() + "template.js");
			// Output template
			final StringWriter writer = new StringWriter();
			template.process(data, writer);
			html = writer.toString();
			// Store to cache
			final Element element = new Element(hash, html);
			cache.put(element);
		}

		response.getWriter().write(html);
	}
}