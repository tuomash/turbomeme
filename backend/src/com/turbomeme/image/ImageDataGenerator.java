package com.turbomeme.image;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;

/**
 * Creates the image data file used by the backend. See resource/image-data.json.
 *
 * @author Tuomas Hynninen (tuomas.hynninen@gmail.com)
 */
public final class ImageDataGenerator implements Constants, Paths
{
  private static final Logger log = LoggerFactory.getLogger(ImageDataGenerator.class);

  public static void main(final String[] args)
  {
    if (doesImageDataJsonExist())
    {
      appendImageDataJson();
    }
    else
    {
      createImageDataJson();
    }
  }

  private static boolean doesImageDataJsonExist()
  {
    final File jsonFile = new File(BACKEND_RESOURCE_DIR + BACKEND_IMAGE_DATA_JSON_FILE_NAME);
    return jsonFile.exists();
  }

  private static void appendImageDataJson()
  {
    final MemeBank bank = loadMemeBank(BACKEND_RESOURCE_DIR + BACKEND_IMAGE_DATA_JSON_FILE_NAME);
    final int originalSize = bank.getIdToMemeMap().size();

    for (final ImageWrapper wrapper : loadImageWrappers())
    {
      // New meme
      if (!bank.getFileNameToMemeMap().containsKey(wrapper.getFileName()))
      {
        log.info("Creating new meme: " + wrapper.getFileName());
        createMemeImage(bank.getIdToMemeMap(), wrapper);
      }
    }

    final int newSize = bank.getIdToMemeMap().size();
    final int diff = -(originalSize - newSize);

    if (diff > 0)
    {
      log.info("Added " + diff + " new memes");
      writeImageDataJsonFile(new ArrayList<>(bank.getIdToMemeMap().values()));
    }
    else
    {
      log.info("No new memes");
    }
  }

  public static MemeBank loadMemeBank(final String path)
  {
    final File jsonFile = new File(path);
    final MemeBank memeBank = new MemeBank();

    try
    {
      final JSONParser parser = new JSONParser();
      final Object obj = parser.parse(new FileReader(jsonFile));
      final JSONArray array = (JSONArray) obj;
      final Map<Integer, MemeImage> idToMemeMap = new HashMap<>();
      final Map<String, MemeImage> fileNameToMemeMap = new HashMap<>();

      for (int i = 0; i < array.size(); i++)
      {
        final JSONObject json = (JSONObject) array.get(i);
        final MemeImage image = MemeImage.fromJSON(json);
        idToMemeMap.put(image.getId(), image);
        fileNameToMemeMap.put(image.getFileName(), image);
      }

      memeBank.setIdToMemeMap(idToMemeMap);
      memeBank.setFileNameToMemeMap(fileNameToMemeMap);
    }
    catch (final Exception e)
    {
      throw new IllegalArgumentException("Couldn't load meme bank! [path=" + path + "]", e);
    }

    return memeBank;
  }

  private static void createImageDataJson()
  {
    final Map<Integer, MemeImage> memeImageMap = new HashMap<>();

    for (final ImageWrapper wrapper : loadImageWrappers())
    {
      createMemeImage(memeImageMap, wrapper);
    }

    writeImageDataJsonFile(new ArrayList<>(memeImageMap.values()));
  }

  private static void writeImageDataJsonFile(final List<MemeImage> memes)
  {
    Collections.sort(memes);
    final JSONArray data = new JSONArray();

    for (final MemeImage image : memes)
    {
      data.add(image.toJSON());
    }

    try
    {
      // Write backend file
      FileWriter file = new FileWriter(BACKEND_RESOURCE_DIR + BACKEND_IMAGE_DATA_JSON_FILE_NAME);
      final Gson gson = new GsonBuilder().setPrettyPrinting().create();
      file.write(gson.toJson(data));
      file.flush();
      file.close();

      // Write frontend file
      file = new FileWriter(FRONTEND_DIR + FRONTEND_IMAGE_DATA_JSON_FILE_NAME);
      file.write("var imageData = " + data.toJSONString());
      file.flush();
      file.close();
    }
    catch (final IOException e)
    {
      e.printStackTrace();
    }
  }

  private static void createMemeImage(final Map<Integer, MemeImage> memes, final ImageWrapper wrapper)
  {
    final Random random = new Random();
    final MemeImage image = new MemeImage();
    Integer id = random.nextInt(1000000000) + 1;

    while (memes.containsKey(id))
    {
      id = random.nextInt(1000000000) + 1;
    }

    image.setId(id);
    image.setDescription("");
    image.setWidth(wrapper.getImage().getWidth());
    image.setHeight(wrapper.getImage().getHeight());
    image.setFileName(wrapper.getFileName());
    image.setWwwPath(WWW_PATH + wrapper.getFileName());
    memes.put(image.getId(), image);
  }

  public static List<ImageWrapper> loadImageWrappers()
  {
    final File imagesDir = new File(IMAGES_DIR);
    final List<ImageWrapper> wrappers = new ArrayList<>();

    for (final File file : imagesDir.listFiles())
    {
      log.info("Reading: " + file.getAbsolutePath());

      try
      {
        wrappers.add(new ImageWrapper(file.getName(), ImageIO.read(file)));
      }
      catch (final IOException e)
      {
        throw new IllegalStateException("Couldn't load image!", e);
      }
    }

    return wrappers;
  }
}