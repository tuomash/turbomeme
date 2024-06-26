package com.turbomeme.image;

import java.io.File;

/**
 * Global paths.
 */
public interface Paths
{
  String CURRENT_DIR = System.getProperty("user.dir");
  String BACKEND_RESOURCE_DIR = CURRENT_DIR + File.separator + "src" + File.separator
      + "main" + File.separator + "resources" + File.separator;
  String FRONTEND_DIR = CURRENT_DIR + File.separator + "src" + File.separator + "main" + File.separator
      + "webapp" + File.separator;
  String IMAGES_DIR = FRONTEND_DIR + File.separator + "img" + File.separator + "memes" + File.separator;
  String UI_DIR = FRONTEND_DIR + File.separator + "img" + File.separator + "ui" + File.separator;
}
