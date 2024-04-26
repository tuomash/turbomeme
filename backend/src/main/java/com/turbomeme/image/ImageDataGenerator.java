package main.java.com.turbomeme.image;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.java.com.turbomeme.util.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * Command line utility.
 * Creates the image data file used by the backend. See resource/image-data.json.
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

    for (final RawImage wrapper : loadImages())
    {
      // Didn't find from bank which means this is a new meme
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
      final JSONArray array = new JSONArray(Files.readString(jsonFile.toPath(), StandardCharsets.UTF_8));
      final Map<Integer, MemeImage> idToMemeMap = new HashMap<>();
      final Map<String, MemeImage> fileNameToMemeMap = new HashMap<>();

      for (int i = 0; i < array.length(); i++)
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

    for (final RawImage wrapper : loadImages())
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
      data.put(image.toJSON());
    }

    // Write backend file
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    FileUtils.writeFile(BACKEND_RESOURCE_DIR + BACKEND_IMAGE_DATA_JSON_FILE_NAME, gson.toJson(data));

    // Write frontend file
    FileUtils.writeFile(FRONTEND_DIR + FRONTEND_IMAGE_DATA_JSON_FILE_NAME, "var imageData = " + data);
  }

  private static void createMemeImage(final Map<Integer, MemeImage> memes, final RawImage wrapper)
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

  protected static List<RawImage> loadImages()
  {
    final File imagesDir = new File(IMAGES_DIR);
    final List<RawImage> images = new ArrayList<>();

    for (final File file : imagesDir.listFiles())
    {
      log.info("Reading file: " + file.getAbsolutePath());

      try
      {
        images.add(new RawImage(file.getName(), ImageIO.read(file)));
      }
      catch (final IOException e)
      {
        throw new IllegalStateException("Couldn't load image! [path=" + file.getAbsolutePath() + "]", e);
      }
    }

    return images;
  }
}
