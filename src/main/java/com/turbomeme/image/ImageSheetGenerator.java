package com.turbomeme.image;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.turbomeme.util.FileUtils;
import com.turbomeme.util.JSONUtils;
import org.json.JSONArray;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command line utility.
 * Creates the image sheet related files used by the backend and frontend. See src/main/java/resources/image-sheet.jpg,
 * src/main/java/resources/image-map.json and src/main/java/resources/image-order.json.
 */
public final class ImageSheetGenerator implements Constants, Paths
{
  public static void main(final String[] args)
  {
    try
    {
      final MemeBank bank = ImageDataGenerator.loadMemeBank(BACKEND_RESOURCE_DIR + BACKEND_IMAGE_DATA_JSON_FILE_NAME);
      final BufferedImage imageSheet = createBlankImageSheet(bank);
      final List<List<Integer>> imageIdRows = new ArrayList<>();
      fillImageSheet(bank, imageSheet, imageIdRows);

      ImageIO.write(imageSheet, "jpg", new File(BACKEND_RESOURCE_DIR + BACKEND_IMAGE_SHEET_FILE_NAME));
      ImageIO.write(imageSheet, "jpg", new File(UI_DIR + FRONTEND_IMAGE_SHEET_FILE_NAME));

      // Write backend file
      final Gson gson = new GsonBuilder().setPrettyPrinting().create();
      FileUtils.writeFile(BACKEND_RESOURCE_DIR + BACKEND_IMAGE_MAP_FILE_NAME, gson.toJson(imageIdRows));

      // Write frontend file
      FileUtils.writeFile(FRONTEND_DIR + FRONTEND_IMAGE_MAP_FILE_NAME, "var imageMap = " + imageIdRows);
    }
    catch (final Exception e)
    {
      throw new IllegalStateException("Couldn't create image sheet!", e);
    }
  }

  private static BufferedImage createBlankImageSheet(final MemeBank bank)
  {
    // Calculate image sheet height based on how many memes there are
    double temp = (double) bank.getIdToMemeMap().size() * (double) IMAGE_SHEET_ICON_WIDTH / (double) IMAGE_SHEET_WIDTH;
    temp = Math.ceil(temp);
    int height = (int) (temp);
    height = height * IMAGE_SHEET_ICON_HEIGHT;
    final BufferedImage imageSheet = new BufferedImage(IMAGE_SHEET_WIDTH, height, BufferedImage.TYPE_INT_RGB);

    // Fill the image sheet background with white
    final Graphics2D graphics2D = (Graphics2D) imageSheet.getGraphics();
    graphics2D.setColor(Color.WHITE);
    graphics2D.fillRect(0, 0, IMAGE_SHEET_WIDTH, height);
    return imageSheet;
  }

  private static void fillImageSheet(final MemeBank bank, final BufferedImage imageSheet, final List<List<Integer>> imageMap)
  {
    final Map<String, BufferedImage> fileNameToImageMap = new HashMap<>();

    for (final RawImage wrapper : ImageDataGenerator.loadImages())
    {
      fileNameToImageMap.put(wrapper.getFileName(), wrapper.getImage());
    }

    // Get the image order json: this file is manually constructed to show the memes in preferred order
    final JSONArray imageOrderJson;

    try
    {
      final String contents = org.apache.commons.io.FileUtils.readFileToString(new File(BACKEND_RESOURCE_DIR + BACKEND_IMAGE_ORDER_JSON_FILE_NAME), Charset.defaultCharset());
      imageOrderJson = JSONUtils.parseArray(contents);
    }
    catch (final Exception e)
    {
      throw new IllegalStateException("Couldn't read image order json file!", e);
    }

    List<Integer> column = new ArrayList<>();
    imageMap.add(column);

    // Setup graphics context for rendering images
    final Graphics2D graphics2D = (Graphics2D) imageSheet.getGraphics();
    int x = 0;
    int y = 0;

    for (int i = 0; i < imageOrderJson.length(); i++)
    {
      final String fileName = (String) imageOrderJson.get(i);
      final BufferedImage memeImage = fileNameToImageMap.get(fileName);

      graphics2D.drawImage(memeImage, x, y, IMAGE_SHEET_ICON_WIDTH, IMAGE_SHEET_ICON_HEIGHT, null);
      x = x + IMAGE_SHEET_ICON_WIDTH;
      column.add(bank.getFileNameToMemeMap().get(fileName).getId());

      // End of row
      if (x >= IMAGE_SHEET_WIDTH)
      {
        x = 0;
        y = y + IMAGE_SHEET_ICON_HEIGHT;
        column = new ArrayList<>();
        imageMap.add(column);
      }
    }
  }
}
