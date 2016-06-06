package com.turbomeme.image;

import com.google.common.base.Preconditions;
import org.json.simple.JSONObject;

public final class MemeImage implements Constants, Comparable<MemeImage>
{
  private Integer id;
  private Integer width;
  private Integer height;
  private String description;
  private String fileName;
  private String wwwPath;

  public void validate()
  {
    System.out.println("Validating: " + fileName);
    Preconditions.checkNotNull(id, "Id cannot be null!");
    Preconditions.checkArgument(id > 0, "Id cannot be 0 or smaller!");

    Preconditions.checkNotNull(width, "Width cannot be null!");
    Preconditions.checkArgument(width > 0, "Width cannot be 0 or smaller!");
    Preconditions.checkArgument(width <= MAX_IMAGE_WIDTH, "Width has to be equal or smaller than " + MAX_IMAGE_WIDTH + "! " +
        "[fileName=" + fileName + ", width=" + width + "]");

    Preconditions.checkNotNull(height, "Height cannot be null!");
    Preconditions.checkArgument(height > 0, "Height cannot be 0 or smaller!");

    Preconditions.checkNotNull(fileName, "File name cannot be null!");
    Preconditions.checkArgument(!fileName.isEmpty(), "File name cannot be empty!");

    Preconditions.checkNotNull(wwwPath, "WWW path cannot be null!");
    Preconditions.checkArgument(!wwwPath.isEmpty(), "WWW path cannot be empty!");
  }

  public JSONObject toJSON()
  {
    validate();
    final JSONObject json = new JSONObject();
    json.put("id", id);
    json.put("width", width);
    json.put("height", height);
    json.put("description", description);
    json.put("fileName", fileName);
    json.put("wwwPath", wwwPath);
    return json;
  }

  public static MemeImage fromJSON(final JSONObject json)
  {
    final MemeImage image = new MemeImage();
    image.setId(((Long) json.get("id")).intValue());
    image.setWidth(((Long) json.get("width")).intValue());
    image.setHeight(((Long) json.get("height")).intValue());
    image.setDescription((String) json.get("description"));
    image.setFileName((String) json.get("fileName"));
    image.setWwwPath((String) json.get("wwwPath"));
    image.validate();
    return image;
  }

  @Override
  public int compareTo(final MemeImage o)
  {
    return this.getFileName().compareTo(o.getFileName());
  }

  public Integer getId()
  {
    return id;
  }

  public void setId(final Integer id)
  {
    this.id = id;
  }

  public Integer getWidth()
  {
    return width;
  }

  public void setWidth(final Integer width)
  {
    this.width = width;
  }

  public Integer getHeight()
  {
    return height;
  }

  public void setHeight(final Integer height)
  {
    this.height = height;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(final String description)
  {
    this.description = description;
  }

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName(final String fileName)
  {
    this.fileName = fileName;
  }

  public String getWwwPath()
  {
    return wwwPath;
  }

  public void setWwwPath(final String wwwPath)
  {
    this.wwwPath = wwwPath;
  }
}