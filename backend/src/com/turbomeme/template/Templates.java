package com.turbomeme.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Simple template utility.
 *
 * @author Tuomas Hynninen (tuomas.hynninen@gmail.com)
 */
public final class Templates
{
  private Configuration configuration;

  public Templates(final ServletContext context, final String path)
  {
    // Configure Freemarker templates
    configuration = new Configuration(Configuration.VERSION_2_3_22);
    configuration.setServletContextForTemplateLoading(context, path);
    configuration.setDefaultEncoding("UTF-8");
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
  }

  public String print(final String templateName, final Map<String, Object> data) throws IOException, TemplateException
  {
    final Template template = configuration.getTemplate(templateName);

    try (final StringWriter writer = new StringWriter())
    {
      template.process(data, writer);
      return writer.toString();
    }
  }
}