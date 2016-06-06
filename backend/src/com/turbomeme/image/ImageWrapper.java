package com.turbomeme.image;

import java.awt.image.BufferedImage;

public final class ImageWrapper
{
  private final String fileName;
  private final BufferedImage image;

  public ImageWrapper(final String fileName, final BufferedImage image)
  {
    this.fileName = fileName;
    this.image = image;
  }

  public String getFileName()
  {
    return fileName;
  }

  public BufferedImage getImage()
  {
    return image;
  }
}