package main.java.com.turbomeme.image;

import java.util.Map;

/**
 * Holds all the meme images.
 */
public final class MemeBank
{
  private Map<Integer, MemeImage> idToMemeMap;
  private Map<String, MemeImage> fileNameToMemeMap;

  public boolean hasMeme(final Integer id)
  {
    return idToMemeMap.containsKey(id);
  }

  public MemeImage getMeme(final Integer id)
  {
    return idToMemeMap.get(id);
  }

  public Map<Integer, MemeImage> getIdToMemeMap()
  {
    return idToMemeMap;
  }

  public void setIdToMemeMap(final Map<Integer, MemeImage> map)
  {
    this.idToMemeMap = map;
  }

  public Map<String, MemeImage> getFileNameToMemeMap()
  {
    return fileNameToMemeMap;
  }

  public void setFileNameToMemeMap(final Map<String, MemeImage> map)
  {
    this.fileNameToMemeMap = map;
  }
}
