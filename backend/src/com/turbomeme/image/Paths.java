package com.turbomeme.image;

import java.io.File;

public final class Paths
{
  public static final String CURRENT_DIR = System.getProperty("user.dir");
  public static final String BACKEND_DIR = CURRENT_DIR + File.separator + "backend" + File.separator;
  public static final String BACKEND_RESOURCE_DIR = CURRENT_DIR + File.separator + "backend" + File.separator + "resource" + File.separator;
  public static final String FRONTEND_DIR = CURRENT_DIR + File.separator + "frontend" + File.separator;
  public static final String IMAGES_DIR = FRONTEND_DIR + File.separator + "img" + File.separator + "memes" + File.separator;
  public static final String UI_DIR = FRONTEND_DIR + File.separator + "img" + File.separator + "ui" + File.separator;
}