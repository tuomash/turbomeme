package com.turbomeme.image;

/**
 * Global constants.
 *
 * @author Tuomas Hynninen (tuomas.hynninen@gmail.com)
 */
public interface Constants
{
  String BACKEND_IMAGE_DATA_JSON_FILE_NAME = "image-data.json";
  String BACKEND_IMAGE_ORDER_JSON_FILE_NAME = "image-order.json";
  String BACKEND_IMAGE_SHEET_FILE_NAME = "image-sheet.jpg";
  String BACKEND_IMAGE_MAP_FILE_NAME = "image-map.json";
  String FRONTEND_IMAGE_DATA_JSON_FILE_NAME = "image-data.js";
  String FRONTEND_IMAGE_SHEET_FILE_NAME = "image-sheet.jpg";
  String FRONTEND_IMAGE_MAP_FILE_NAME = "image-map.js";
  Integer MAX_IMAGE_WIDTH = 600;
  Integer IMAGE_SHEET_WIDTH = 800;
  Integer IMAGE_SHEET_ICON_WIDTH = 100;
  Integer IMAGE_SHEET_ICON_HEIGHT = 100;
  String WWW_PATH = "img/memes/";
  Integer MEME_HASH_LENGTH = 8;
  Integer MEME_HASH_WITH_SLASH_LENGTH = MEME_HASH_LENGTH + 1;
}