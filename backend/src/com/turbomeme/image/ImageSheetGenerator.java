package com.turbomeme.image;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public final class ImageSheetGenerator implements Constants
{
  public static void main(final String[] args)
  {
    final File jsonFile = new File(Paths.BACKEND_RESOURCE_DIR + BACKEND_IMAGE_ORDER_JSON_FILE_NAME);
    final JSONParser parser = new JSONParser();

    try
    {
      final MemeBank bank = ImageDataGenerator.loadMemeBank(Paths.BACKEND_RESOURCE_DIR + BACKEND_IMAGE_DATA_JSON_FILE_NAME);
      final Map<String, BufferedImage> fileNameToImageMap = new HashMap<>();

      for (final ImageWrapper wrapper : ImageDataGenerator.loadImageWrappers())
      {
        fileNameToImageMap.put(wrapper.getFileName(), wrapper.getImage());
      }

      double temp = (double) bank.getIdToMemeMap().size() * (double) IMAGE_SHEET_ICON_WIDTH / (double) IMAGE_SHEET_WIDTH;
      temp = Math.ceil(temp);
      int height = (int) (temp);
      height = height * IMAGE_SHEET_ICON_HEIGHT;
      final BufferedImage imageSheet = new BufferedImage(IMAGE_SHEET_WIDTH, height, BufferedImage.TYPE_INT_RGB);
      final Graphics2D graphics2D = (Graphics2D) imageSheet.getGraphics();
      graphics2D.setColor(Color.WHITE);
      graphics2D.fillRect(0, 0, IMAGE_SHEET_WIDTH, height);
      int x = 0;
      int y = 0;
      final Object obj = parser.parse(new FileReader(jsonFile));
      final JSONArray imageOrderJson = (JSONArray) obj;
      final JSONArray imageMap = new JSONArray();
      JSONArray column = new JSONArray();
      imageMap.add(column);

      for (int i = 0; i < imageOrderJson.size(); i++)
      {
        final String fileName = (String) imageOrderJson.get(i);
        final BufferedImage img = fileNameToImageMap.get(fileName);
        graphics2D.drawImage(img, x, y, IMAGE_SHEET_ICON_WIDTH, IMAGE_SHEET_ICON_HEIGHT, null);
        x = x + IMAGE_SHEET_ICON_WIDTH;
        column.add(bank.getFileNameToMemeMap().get(fileName).getId());

        if (x >= IMAGE_SHEET_WIDTH)
        {
          x = 0;
          y = y + IMAGE_SHEET_ICON_HEIGHT;
          column = new JSONArray();
          imageMap.add(column);
        }
      }

      ImageIO.write(imageSheet, "jpg", new File(Paths.BACKEND_RESOURCE_DIR + BACKEND_IMAGE_SHEET_FILE_NAME));
      ImageIO.write(imageSheet, "jpg", new File(Paths.UI_DIR + FRONTEND_IMAGE_SHEET_FILE_NAME));

      try
      {
        // Write backend file
        FileWriter file = new FileWriter(Paths.BACKEND_RESOURCE_DIR + BACKEND_IMAGE_MAP_FILE_NAME);
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        file.write(gson.toJson(imageMap));
        file.flush();
        file.close();

        // Write frontend file
        file = new FileWriter(Paths.FRONTEND_DIR + FRONTEND_IMAGE_MAP_FILE_NAME);
        file.write("var imageMap = " + imageMap.toJSONString());
        file.flush();
        file.close();
      }
      catch (final IOException e)
      {
        e.printStackTrace();
      }
    }
    catch (final FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (final ParseException e)
    {
      e.printStackTrace();
    }
    catch (final IOException e)
    {
      e.printStackTrace();
    }
  }
}