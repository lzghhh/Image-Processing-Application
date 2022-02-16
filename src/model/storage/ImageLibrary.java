package model.storage;

import java.util.Map;

import model.image.ImageModel;

/**
 * This interface represents the operations offered by an image storage.
 * One object of the model represents one storage of image stored in the program.
 */
public interface ImageLibrary {

  /**
   * Put an image into the hashmap to store it.
   * @param key represents the name of the image.
   * @param model represents the model of the image.
   */
  void put(String key, ImageModel model);

  /**
   * Check if the storage contain the given image and return its model.
   * @param name represents the name of the image
   * @return the given image model stored
   */
  ImageModel contain(String name);

  /**
   * Return this storage.
   */
  Map<String, ImageModel> returnStorage();
}
