package model.storage;

import java.util.HashMap;
import java.util.Map;


import model.image.ImageModel;

/**
 * The class that implements the ImageLibrary model.
 * The class represents the storage for images.
 * You can perform operations related to methods that manipulate the storage.
 */
public class ImageStorage implements ImageLibrary {
  private final Map<String, ImageModel> storage;

  /**
   * A constructor to initiates a hashmap that acts like a storage for images.
   */
  public ImageStorage() {
    this.storage = new HashMap<>();
  }

  /**
   * Put an image into the hashmap to store it.
   *
   * @param key   represents the name of the image.
   * @param model represents the model of the image.
   */
  @Override
  public void put(String key, ImageModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Null model");
    }
    this.storage.put(key, model);
  }

  /**
   * Check if the storage contain the given image and return its model and return it.
   *
   * @param name represents the name of the image
   * @return the given image model stored
   */
  @Override
  public ImageModel contain(String name) {
    ImageModel copy;
    copy = (this.storage.getOrDefault(name, null));
    if (copy == null) {
      return null;
    }
    return copy.clone();
  }

  /**
   * Return this storage.
   */
  @Override
  public Map<String, ImageModel> returnStorage() {
    return this.storage;
  }

}
