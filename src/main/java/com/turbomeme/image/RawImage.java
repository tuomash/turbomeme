package com.turbomeme.image;

import java.awt.image.BufferedImage;

/**
 * Simple container for image file name and data.
 */
public final class RawImage
{
  private final String fileName;
  private final BufferedImage image;

  public RawImage(final String fileName, final BufferedImage image)
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
