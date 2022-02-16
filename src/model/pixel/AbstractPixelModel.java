package model.pixel;

import java.util.Collections;
import java.util.List;

/**
 * This class is an abstract implementation of the Pixel interface. This class
 * contains methods for pixels operations and any related helper methods. Also,
 * this abstract class helps remove any redundancy in any of the other models for other types
 * of pixels.
 */
public abstract class AbstractPixelModel implements Pixel {
  private final List<Integer> rgb;

  /**
   * A constructor taking in the structure of the pixel (its list of RGB values).
   *
   * @param rgb represent the list of RGB values.
   */
  public AbstractPixelModel(List<Integer> rgb) {
    if (rgb == null) {
      throw new IllegalArgumentException("Null RGB values");
    } else if (rgb.size() > 4 || rgb.size() < 3) {
      throw new IllegalArgumentException("There should be three numbers indicating the" +
              "level of red, green, and blue!");
    }
    this.rgb = rgb;
  }

  /**
   * Get the rgb representation of the pixel.
   *
   * @return the rgb representation of the pixel.
   */
  @Override
  public List<Integer> getColor() {
    return this.rgb;
  }

  /**
   * Get the maximum value of the three components for the pixel.
   *
   * @return the maximum value of the three components for the pixel.
   */
  @Override
  public int maxVal() {
    return Collections.max(this.rgb);
  }

  /**
   * Get the average of the three components for the pixel.
   *
   * @return the average of the three components the pixel.
   */
  @Override
  public int intensity() {
    return (this.rgb.get(0) + this.rgb.get(1) + this.rgb.get(2)) / 3;
  }

  /**
   * Get the luma (the levels of brightness) of the pixel.
   *
   * @return the luma of the pixel.
   */
  @Override
  public int luma() {
    return (int) Math.round(0.2126 * (this.rgb.get(0))
            + 0.7152 * (this.rgb.get(1)) + 0.0722 * (this.rgb.get(2)));
  }

  /**
   * Change all rgb values of a pixel to the luma value, the intensity value, max value of RGB,
   * the red component, the blue component, or the green component.
   */
  @Override
  public void setRGB(String type) {
    int value = 0;
    if (type.equals("luma")) {
      value = this.luma();
    } else if (type.equals("intensity")) {
      value = this.intensity();
    }
    for (int i = 0; i < 3; i++) {
      switch (type) {
        case "luma": //continue to the intensity case because they have the same operations.
        case "intensity":
          this.rgb.set(i, value);
          break;
        case "max":
          this.rgb.set(i, this.maxVal());
          break;
        case "red":
          int red = this.rgb.get(0);
          this.rgb.set(i, red);
          break;
        case "green":
          int green = this.rgb.get(1);
          this.rgb.set(i, green);
          break;
        case "blue":
          int blue = this.rgb.get(2);
          this.rgb.set(i, blue);
          break;
        default:
          throw new IllegalArgumentException("Invalid argument!");
          //throw an Illegal Argument Exception since the argument is invalid.
      }
    }
  }

  /**
   * Copy the pixel and return its copy.
   *
   * @return the copy of the pixel.
   */
  @Override
  public abstract Pixel copy();
}
