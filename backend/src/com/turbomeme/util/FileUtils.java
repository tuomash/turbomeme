package com.turbomeme.util;

import java.io.FileWriter;
import java.io.IOException;

/**
 * File utility functions.
 */
public final class FileUtils
{
  public static void writeFile(final String path, final String content)
  {
    try (final FileWriter file = new FileWriter(path))
    {
      file.write(content);
      file.flush();
    }
    catch (final IOException e)
    {
      throw new IllegalStateException("Couldn't write file! [path=" + path + "]", e);
    }
  }
}
