package com.turbomeme.image;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemeImageTest
{
  @Test
  public void testValidationFailure()
  {
    final MemeImage image = new MemeImage();
    Assertions.assertThrows(NullPointerException.class, image::validate);
    image.setId(0);
    Assertions.assertThrows(IllegalArgumentException.class, image::validate);
  }

  @Test
  public void testValidationSuccess()
  {
    final MemeImage image = new MemeImage();
    image.setId(1);
    image.setWidth(100);
    image.setHeight(100);
    image.setFileName("example.png");
    image.setWwwPath("www.google.com");
    Assertions.assertDoesNotThrow(image::validate);
  }
}
