
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import controller.ImageController;
import controller.ImageControllerImpl;
import model.image.IOModel;
import model.image.ImageModel;
import model.image.PPMModel;
import model.pixel.EightBitPixelModel;
import model.pixel.Pixel;
import model.storage.ImageLibrary;
import model.storage.ImageStorage;
import view.ImageView;
import view.ImageViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class contains tests to check all methods related to the ImageControllerImpl Class.
 */
public class ImageControllerImplTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();


  /**
   * Setting up streams before testing.
   */
  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
  }

  /**
   * Testing for appendable error.
   */
  @Test(expected = IllegalStateException.class)
  public void errorAppendableTest() {
    Appendable ap = new FakeAppendable();
    Readable in = new StringReader("load " +
            "C:\\Users\\admin\\Pictures\\Camera Roll\\koala.ppm koala");

    Pixel[][] pix = new Pixel[2][2];
    pix[0][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(1, 1, 3)));
    pix[0][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(8, 1, 1)));
    pix[1][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(2, 1, 4)));
    pix[1][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(5, 4, 3)));
    ImageModel test = new PPMModel(2, 2, 255, pix);
    ImageLibrary lib = new ImageStorage();
    ImageView view = new ImageViewImpl(lib, ap);
    ImageController controller = new ImageControllerImpl(lib, view, in);
    controller.process();
  }

  /**
   * Test if inputs are passed in.
   */
  @Test
  public void testInputs() {
    Readable in = new StringReader("brighten 10 mock mock1");
    Appendable dontCareOutput = new StringBuilder();
    ImageLibrary lib = new ImageStorage();
    ImageView view = new ImageViewImpl(lib, dontCareOutput);
    StringBuilder log = new StringBuilder();
    ImageModel mock = new MockImageModel(log);
    lib.put("mock", mock);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals("amount = 10", log.toString());
  }


  /**
   * Test ppm image is loaded successfully.
   */
  @Test
  public void testPPMImageLoaded() {
    Readable in = new StringReader("load " +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.ppm" + " koala");
    Appendable output = new StringBuilder();
    ImageLibrary lib = new ImageStorage();
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Action Completed! \n");
    assertEquals(outContent.toString(), "Input File Type: ppm" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.ppm has been loaded successfully as koala"
            + System.lineSeparator());
    assertNotNull(lib.contain("koala")); //check if the object is != null
    //check if the library has the image and returned the model of the class PPMModel.
    assertTrue(lib.contain("koala") instanceof PPMModel);
    ImageModel loaded = lib.contain("koala");
    //compare it with the real info of the image loaded from the examples
    //example is modified so that height = 2, width = 2,
    //and the pixels is the same as the mock defined below
    Pixel[][] mock = new Pixel[2][2];
    mock[0][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(1, 1, 3)));
    mock[0][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(8, 1, 1)));
    mock[1][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(2, 1, 4)));
    mock[1][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(5, 4, 3)));
    assertEquals(loaded.getHeight(), 2);
    assertEquals(loaded.getWidth(), 2);
    String mockS = "";
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        mockS += (mock[i][j].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    String loadS = "";
    for (int k = 0; k < 2; k++) {
      for (int l = 0; l < 2; l++) {
        loadS += (loaded.getPixels()[k][l].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    assertEquals(mockS, loadS);
  }

  /**
   * Test png image is loaded successfully.
   */
  @Test
  public void testPNGImageLoaded() {
    Readable in = new StringReader("load " +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.png" + " koalap");
    Appendable output = new StringBuilder();
    ImageLibrary lib = new ImageStorage();
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Action Completed! \n");
    assertEquals(outContent.toString(), "Input File Type: png" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.png has been loaded successfully as koalap"
            + System.lineSeparator());
    assertNotNull(lib.contain("koalap")); //check if the object is != null
    //check if the library has the image and returned the model of the class IOModel.
    assertTrue(lib.contain("koalap") instanceof IOModel);
    ImageModel loaded = lib.contain("koalap");
    //compare it with the real info of the image loaded from the examples
    //example is modified so that height = 2, width = 2,
    //and the pixels is the same as the mock defined below
    Pixel[][] mock = new Pixel[2][2];
    mock[0][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(1, 1, 3, 255)));
    mock[0][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(8, 1, 1, 255)));
    mock[1][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(2, 1, 4, 255)));
    mock[1][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(5, 4, 3, 255)));
    assertEquals(loaded.getHeight(), 2);
    assertEquals(loaded.getWidth(), 2);
    String mockS = "";
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        mockS += (mock[i][j].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    String loadS = "";
    for (int k = 0; k < 2; k++) {
      for (int l = 0; l < 2; l++) {
        loadS += (loaded.getPixels()[k][l].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    assertEquals(mockS, loadS); //check if loaded image has correct information
  }

  /**
   * Test bmp image is loaded successfully.
   */
  @Test
  public void testBMPImageLoaded() {
    Readable in = new StringReader("load " +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.bmp" + " koalap");
    Appendable output = new StringBuilder();
    ImageLibrary lib = new ImageStorage();
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Action Completed! \n");
    assertEquals(outContent.toString(), "Input File Type: bmp" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.bmp has been loaded successfully as koalap"
            + System.lineSeparator());
    assertNotNull(lib.contain("koalap")); //check if the object is != null
    //check if the library has the image and returned the model of the class IOModel.
    assertTrue(lib.contain("koalap") instanceof IOModel);
    ImageModel loaded = lib.contain("koalap");
    //compare it with the real info of the image loaded from the examples
    //example is modified so that height = 2, width = 2,
    //and the pixels is the same as the mock defined below
    Pixel[][] mock = new Pixel[2][2];
    mock[0][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(1, 1, 3, 255)));
    mock[0][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(8, 1, 1, 255)));
    mock[1][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(2, 1, 4, 255)));
    mock[1][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(5, 4, 3, 255)));
    assertEquals(loaded.getHeight(), 2);
    assertEquals(loaded.getWidth(), 2);
    String mockS = "";
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        mockS += (mock[i][j].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    String loadS = "";
    for (int k = 0; k < 2; k++) {
      for (int l = 0; l < 2; l++) {
        loadS += (loaded.getPixels()[k][l].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    assertEquals(mockS, loadS); //check if loaded image has correct information
  }

  /**
   * Test jpeg image is loaded successfully.
   */
  @Test
  public void testJPEGImageLoaded() {
    Readable in = new StringReader("load " +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.jpeg" + " koalap");
    Appendable output = new StringBuilder();
    ImageLibrary lib = new ImageStorage();
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Action Completed! \n");
    assertEquals(outContent.toString(), "Input File Type: jpeg" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.jpeg has been loaded successfully as koalap"
            + System.lineSeparator());
    assertNotNull(lib.contain("koalap")); //check if the object is != null
    //check if the library has the image and returned the model of the class IOModel.
    assertTrue(lib.contain("koalap") instanceof IOModel);
    //can't check the details of the image because JPEG performs a lossy compression, so RGB values
    //will not be the same.
    assertEquals(lib.contain("koalap").getHeight(), 2);
    assertEquals(lib.contain("koalap").getWidth(), 2);
  }

  /**
   * Test jpg image is loaded successfully.
   */
  @Test
  public void testJPGImageLoaded() {
    Readable in = new StringReader("load " +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.jpg" + " koalap");
    Appendable output = new StringBuilder();
    ImageLibrary lib = new ImageStorage();
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Action Completed! \n");
    assertEquals(outContent.toString(), "Input File Type: jpg" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.jpg has been loaded successfully as koalap"
            + System.lineSeparator());
    assertNotNull(lib.contain("koalap")); //check if the object is != null
    //check if the library has the image and returned the model of the class IOModel.
    assertTrue(lib.contain("koalap") instanceof IOModel);
    //can't check the details of the image because jpg performs a lossy compression, so RGB values
    //will not be the same.
    assertEquals(lib.contain("koalap").getHeight(), 2);
    assertEquals(lib.contain("koalap").getWidth(), 2);
  }


  /**
   * Test export PPMFile successfully.
   */
  @Test
  public void testExport() {
    Readable in = new StringReader("save C:\\Users\\admin\\OneDrive\\Public\\mock.ppm mock");
    Appendable output = new StringBuilder();

    ImageLibrary lib = new ImageStorage();

    Pixel[][] pix = new Pixel[2][2];
    pix[0][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(1, 1, 3)));
    pix[0][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(8, 1, 1)));
    pix[1][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(2, 1, 4)));
    pix[1][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(5, 4, 3)));
    ImageModel mock = new PPMModel(2, 2, 255, pix);
    lib.put("mock", mock);
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Action Completed! \n");
    assertEquals(outContent.toString(), "mock has been saved successfully"
            + System.lineSeparator());

    //new storage and controller and view to load the saved file.
    ImageLibrary storage = new ImageStorage();
    Readable rd = new StringReader("load C:\\Users\\admin\\OneDrive\\Public\\mock.ppm mock");
    Appendable ap = new StringBuilder();
    ImageView view1 = new ImageViewImpl(storage, ap);
    ImageController controller = new ImageControllerImpl(storage, view1, rd);
    controller.process();
    assertEquals(ap.toString(), "Action Completed! \n");
    assertEquals(outContent.toString(), "mock has been saved successfully"
            + System.lineSeparator()
            + "Input File Type: ppm" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\mock.ppm has been loaded successfully as mock"
            + System.lineSeparator());

    String original = "";
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        original += (mock.getPixels()[i][j].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    String loadedAfterSave = "";
    for (int k = 0; k < 2; k++) {
      for (int l = 0; l < 2; l++) {
        loadedAfterSave += (storage.contain("mock").getPixels()[k][l].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    //compare rgb values of pixels between the original mock defined at the start
    //and the exported mock ppm file loaded from the new controller
    assertEquals(original, loadedAfterSave);
    //compare height, width
    assertEquals(mock.getHeight(), storage.contain("mock").getHeight());
    assertEquals(mock.getWidth(), storage.contain("mock").getWidth());
  }

  /**
   * Test export PNGFile successfully.
   */
  @Test
  public void testExportPNG() {
    Readable in = new StringReader("save C:\\Users\\admin\\OneDrive\\Public\\mock.png mock");
    Appendable output = new StringBuilder();

    ImageLibrary lib = new ImageStorage();

    Pixel[][] pix = new Pixel[2][2];
    pix[0][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(1, 1, 3, 255)));
    pix[0][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(8, 1, 1, 255)));
    pix[1][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(2, 1, 4, 255)));
    pix[1][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(5, 4, 3, 255)));
    ImageModel mock = new IOModel(2, 2, 255, pix);
    lib.put("mock", mock);
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Action Completed! \n");
    assertEquals(outContent.toString(), "mock has been saved successfully"
            + System.lineSeparator());

    //new storage and controller and view to load the saved file.
    ImageLibrary storage = new ImageStorage();
    Readable rd = new StringReader("load C:\\Users\\admin\\OneDrive\\Public\\mock.png mock");
    Appendable ap = new StringBuilder();
    ImageView view1 = new ImageViewImpl(storage, ap);
    ImageController controller = new ImageControllerImpl(storage, view1, rd);
    controller.process();
    assertEquals(ap.toString(), "Action Completed! \n");
    assertEquals(outContent.toString(), "mock has been saved successfully"
            + System.lineSeparator()
            + "Input File Type: png" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\mock.png has been loaded successfully as mock"
            + System.lineSeparator());

    String original = "";
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        original += (mock.getPixels()[i][j].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    String loadedAfterSave = "";
    for (int k = 0; k < 2; k++) {
      for (int l = 0; l < 2; l++) {
        loadedAfterSave += (storage.contain("mock").getPixels()[k][l].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    //compare rgb values of pixels between the original mock defined at the start
    //and the exported mock png file loaded from the new controller
    assertEquals(original, loadedAfterSave);
    //compare height, width
    assertEquals(mock.getHeight(), storage.contain("mock").getHeight());
    assertEquals(mock.getWidth(), storage.contain("mock").getWidth());
  }

  /**
   * Test export BMPFile successfully.
   */
  @Test
  public void testExportBMP() {
    Readable in = new StringReader("save C:\\Users\\admin\\OneDrive\\Public\\mock.bmp mock");
    Appendable output = new StringBuilder();

    ImageLibrary lib = new ImageStorage();

    Pixel[][] pix = new Pixel[2][2];
    pix[0][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(1, 1, 3, 255)));
    pix[0][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(8, 1, 1, 255)));
    pix[1][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(2, 1, 4, 255)));
    pix[1][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(5, 4, 3, 255)));
    ImageModel mock = new IOModel(2, 2, 255, pix);
    lib.put("mock", mock);
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Action Completed! \n");
    assertEquals(outContent.toString(), "mock has been saved successfully"
            + System.lineSeparator());

    //new storage and controller and view to load the saved file.
    ImageLibrary storage = new ImageStorage();
    Readable rd = new StringReader("load C:\\Users\\admin\\OneDrive\\Public\\mock.bmp mock");
    Appendable ap = new StringBuilder();
    ImageView view1 = new ImageViewImpl(storage, ap);
    ImageController controller = new ImageControllerImpl(storage, view1, rd);
    controller.process();
    assertEquals(ap.toString(), "Action Completed! \n");
    assertEquals(outContent.toString(), "mock has been saved successfully"
            + System.lineSeparator()
            + "Input File Type: bmp" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\mock.bmp has been loaded successfully as mock"
            + System.lineSeparator());

    String original = "";
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        original += (mock.getPixels()[i][j].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    String loadedAfterSave = "";
    for (int k = 0; k < 2; k++) {
      for (int l = 0; l < 2; l++) {
        loadedAfterSave += (storage.contain("mock").getPixels()[k][l].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    //compare rgb values of pixels between the original mock defined at the start
    //and the exported mock bmp file loaded from the new controller
    assertEquals(original, loadedAfterSave);
    //compare height, width
    assertEquals(mock.getHeight(), storage.contain("mock").getHeight());
    assertEquals(mock.getWidth(), storage.contain("mock").getWidth());
  }

  /**
   * Test to see if the script from file is parsed in correctly.
   */
  @Test
  public void testParseScript() {
    ImageProcess.main(new String[]{"-file", "C:\\Users\\admin\\OneDrive\\Public\\script.txt"});

    assertEquals(outContent.toString(), "Input File Type: ppm" +
            System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.ppm has been loaded successfully as ood" +
            System.lineSeparator() +
            "Action Completed! \nood has been brighten with an amount of 30 as ood-brighter" +
            System.lineSeparator() +
            "Action Completed! \nood has been changed successfully as red component ood-red" +
            System.lineSeparator() +
            "Action Completed! \nood-brighter has been changed " +
            "successfully as blue component ood-b-blue" +
            System.lineSeparator() +
            "Action Completed! \n" +
            "ood-red has been changed successfully as green component ood-green"
            + System.lineSeparator() +
            "Action Completed! \nood has been changed successfully as luma component ood-luma" +
            System.lineSeparator() +
            "Action Completed! \nood has been changed successfully as max component ood-max" +
            System.lineSeparator() +
            "Action Completed! \nood has been changed successfully as intensity component " +
            "ood-intensity" +
            System.lineSeparator() +
            "Action Completed! \nood has been horizontally flipped as ood-h" +
            System.lineSeparator() +
            "Action Completed! \nood has been vertically flipped as ood-v" +
            System.lineSeparator() +
            "Action Completed! \nood-b-blue has been horizontally flipped as ood-b-blue-h"
            + System.lineSeparator() +
            "Action Completed! \nood-b-blue-h has been saved successfully"
            + System.lineSeparator() +
            "Action Completed! \nood-red has been saved successfully" + System.lineSeparator() +
            "Action Completed! \nood-luma has been saved successfully" + System.lineSeparator() +
            "Action Completed! \n");
  }

  /**
   * Test export as different types.
   */
  @Test
  public void testMultiExport() {
    Readable in = new StringReader("load C:\\Users\\admin\\OneDrive\\Public\\mock.ppm mock" +
            " save C:\\Users\\admin\\OneDrive\\Public\\mockpng.png mock" +
            " save C:\\Users\\admin\\OneDrive\\Public\\mockjpg.jpg mock" +
            " save C:\\Users\\admin\\OneDrive\\Public\\mockjpeg.jpeg mock" +
            " save C:\\Users\\admin\\OneDrive\\Public\\mockbmp.bmp mock");
    Appendable output = new StringBuilder();

    ImageLibrary lib = new ImageStorage();

    Pixel[][] pix = new Pixel[2][2];
    pix[0][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(1, 1, 3, 255)));
    pix[0][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(8, 1, 1, 255)));
    pix[1][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(2, 1, 4, 255)));
    pix[1][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(5, 4, 3, 255)));
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n");
    assertEquals(outContent.toString(), "Input File Type: ppm" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\mock.ppm has been loaded successfully as mock" +
            System.lineSeparator() +
            "mock has been saved successfully" + System.lineSeparator() +
            "mock has been saved successfully" + System.lineSeparator() +
            "mock has been saved successfully" + System.lineSeparator() +
            "mock has been saved successfully"
            + System.lineSeparator());
    ImageModel mock = lib.contain("mock"); //get original file

    //new storage and controller and view to load the saved file.
    ImageLibrary storage = new ImageStorage();
    Readable rd = new StringReader("load C:\\Users\\admin\\OneDrive\\Public\\mockbmp.bmp mockbmp"
            + " load C:\\Users\\admin\\OneDrive\\Public\\mockpng.png mockpng"
            + " load C:\\Users\\admin\\OneDrive\\Public\\mockjpg.jpg mockjpg"
            + " load C:\\Users\\admin\\OneDrive\\Public\\mockjpeg.jpeg mockjpeg");
    Appendable ap = new StringBuilder();
    ImageView view1 = new ImageViewImpl(storage, ap);
    ImageController controller = new ImageControllerImpl(storage, view1, rd);
    controller.process();
    assertEquals(ap.toString(), "Action Completed! \nAction Completed! \n" +
            "Action Completed! \nAction Completed! \n");
    assertEquals(outContent.toString(), "Input File Type: ppm" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\mock.ppm has been loaded successfully as mock" +
            System.lineSeparator() +
            "mock has been saved successfully" + System.lineSeparator() +
            "mock has been saved successfully" + System.lineSeparator() +
            "mock has been saved successfully" + System.lineSeparator() +
            "mock has been saved successfully" + System.lineSeparator() +
            "Input File Type: bmp" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\mockbmp.bmp" +
            " has been loaded successfully as mockbmp"
            + System.lineSeparator() + "Input File Type: png" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\mockpng.png" +
            " has been loaded successfully as mockpng" + System.lineSeparator() +
            "Input File Type: jpg" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\mockjpg.jpg" +
            " has been loaded successfully as mockjpg" + System.lineSeparator() +
            "Input File Type: jpeg" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\mockjpeg.jpeg" +
            " has been loaded successfully as mockjpeg" + System.lineSeparator());

    String original = "";
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        original += (mock.getPixels()[i][j].getColor().stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    String loadedpng = "";
    for (int k = 0; k < 2; k++) {
      for (int l = 0; l < 2; l++) {
        loadedpng += (storage.contain("mockpng").getPixels()[k][l].getColor().stream()
                .map(n -> String.valueOf(n)).filter(x -> !x.contains("255"))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }

    //compare rgb values of pixels between the original mock defined at the start
    //and the exported mock png file loaded from the new controller
    assertEquals(original, loadedpng);
    //compare height, width
    assertEquals(mock.getHeight(), storage.contain("mockpng").getHeight());
    assertEquals(mock.getWidth(), storage.contain("mockpng").getWidth());
    //this time for bmp
    String loadedbmp = "";
    for (int k = 0; k < 2; k++) {
      for (int l = 0; l < 2; l++) {
        loadedbmp += (storage.contain("mockbmp").getPixels()[k][l].getColor().stream()
                .map(n -> String.valueOf(n)).filter(x -> !x.contains("255"))
                .collect(Collectors.joining(", ", "(", ")")));
      }
    }
    //compare rgb values of pixels between the original mock defined at the start
    //and the exported mock png file loaded from the new controller
    assertEquals(original, loadedbmp);
    //compare height, width
    assertEquals(mock.getHeight(), storage.contain("mockbmp").getHeight());
    assertEquals(mock.getWidth(), storage.contain("mockbmp").getWidth());

    //lossy compression can't check pixels
    assertEquals(storage.contain("mockjpeg").getHeight(), 2);
    assertTrue(storage.contain("mockjpeg") instanceof IOModel);
    assertEquals(storage.contain("mockjpeg").getWidth(), 2);
    assertEquals(storage.contain("mockjpg").getHeight(), 2);
    assertTrue(storage.contain("mockjpg") instanceof IOModel);
    assertEquals(storage.contain("mockjpg").getWidth(), 2);

  }

  /**
   * Test to see if the script has an error in commands.
   */
  @Test
  public void testParseScriptError() {
    ImageProcess.main(new String[]{"-file", "C:\\Users\\admin\\OneDrive\\Public\\errorscript.txt"});
    assertEquals(outContent.toString(), "Input File Type: ppm" + System.lineSeparator() +
            "C:\\Users\\admin\\OneDrive\\Public\\koala.ppm has been loaded successfully as ood" +
            System.lineSeparator() +
            "Action Completed! \n" +
            "Action failed \n" +
            "Action failed \n" +
            "Action failed \n" +
            "Action failed \n" +
            "ood has been changed successfully as red component ood-red" + System.lineSeparator() +
            "Action Completed! \n" +
            "Cannot find image/masked image (Name cannot be the command's name)" +
            System.lineSeparator() +
            "Action Completed! \n" +
            "ood-red has been changed successfully as green component ood-green" +
            System.lineSeparator() +
            "Action Completed! \n" +
            "ood has been changed successfully as luma component ood-luma" +
            System.lineSeparator() +
            "Action Completed! \n" +
            "ood has been changed successfully as max component ood-max" +
            System.lineSeparator() +
            "Action Completed! \n" +
            "ood has been changed successfully as intensity component ood-intensity" +
            System.lineSeparator() +
            "Action Completed! \n" +
            "ood has been horizontally flipped as ood-h" + System.lineSeparator() +
            "Action Completed! \n" +
            "ood has been vertically flipped as ood-v" + System.lineSeparator() +
            "Action Completed! \n" +
            "Cannot find image (Name cannot be the command's name)" +  System.lineSeparator() +
            "Action Completed! \n" +
            "Cannot find image" + System.lineSeparator() +
            "Action Completed! \n" +
            "ood-red has been saved successfully" + System.lineSeparator() +
            "Action Completed! \n" +
            "ood-luma has been saved successfully" + System.lineSeparator() +
            "Action Completed! \n" );
  }

  /**
   * Test operations works, and when operations fails when the command is unknown (invalid).
   */
  @Test
  public void testOperations() {
    Readable in = new StringReader("brighten 10 mock mock-brighter " +
            "vertical-flip mock-brighter mock1"
            + " horizontal-flip mock1 mock2" + " brighten -1 mock2 mock3" +
            " value-component mock3 mock4" + " luma-component mock4 mock5"
            + " intensity-component mock5 mock6" + " red-component mock6 mock7"
            + " blue-component mock7 mock8" + " green-component mock8 mock9" +
            " sepia mock9 mock10" +
            " dsfsefsdef" + "324234" + "/$#%$@" + "brighten wred mock9 mockerr"
            + " blur mock10 mock11" + " blur mock11 mock12" + " sharpen mock12 mock13" +
            " sharpen mock13 mock14" + " greyscale mock14 mock15"
            + " grey-component 32323" + " brighten333" + "q");
    Appendable output = new StringBuilder();

    ImageLibrary lib = new ImageStorage();

    Pixel[][] pix = new Pixel[2][2];
    pix[0][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(1, 1, 3)));
    pix[0][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(8, 1, 1)));
    pix[1][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(2, 1, 4)));
    pix[1][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(5, 4, 3)));
    ImageModel mock = new PPMModel(2, 2, 255, pix);
    lib.put("mock", mock);
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action failed \n" +
            "Action failed \n" +
            "Action failed \n" +
            "Action failed \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action Completed! \n" +
            "Action failed \nAction failed \nAction failed \n");
    assertEquals(outContent.toString(), "mock has been brighten with an amount of " +
            "10 as mock-brighter" + System.lineSeparator() +
            "mock-brighter has been vertically flipped as mock1" +
            System.lineSeparator() +
            "mock1 has been horizontally flipped as mock2" +
            System.lineSeparator() +
            "mock2 has been brighten with an amount of -1 as mock3" +
            System.lineSeparator() +
            "mock3 has been changed successfully as max component mock4" +
            System.lineSeparator() +
            "mock4 has been changed successfully as luma component mock5" +
            System.lineSeparator() +
            "mock5 has been changed successfully as intensity component mock6" +
            System.lineSeparator() +
            "mock6 has been changed successfully as red component mock7" +
            System.lineSeparator() +
            "mock7 has been changed successfully as blue component mock8" +
            System.lineSeparator() +
            "mock8 has been changed successfully as green component mock9"
            + System.lineSeparator() +
            "mock9 has been sepia-toned as mock10" + System.lineSeparator() +
            "mock10 has been blurred as mock11" + System.lineSeparator() +
            "mock11 has been blurred as mock12" + System.lineSeparator() +
            "mock12 has been sharpened as mock13" + System.lineSeparator() +
            "mock13 has been sharpened as mock14" + System.lineSeparator() +
            "mock14 has been greyscale as mock15" + System.lineSeparator());
  }


  /**
   * Test quit.
   */
  @Test
  public void testQuit() {
    Readable in = new StringReader("q Q");
    Appendable output = new StringBuilder();

    ImageLibrary lib = new ImageStorage();

    Pixel[][] pix = new Pixel[2][2];
    pix[0][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(1, 1, 3)));
    pix[0][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(8, 1, 1)));
    pix[1][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(2, 1, 4)));
    pix[1][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(5, 4, 3)));
    ImageModel mock = new PPMModel(2, 2, 255, pix);
    lib.put("mock", mock);
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Application closed");
    assertEquals(outContent.toString(), "");
  }

  /**
   * Test quit with words.
   */
  @Test
  public void testQuit2() {
    Readable in = new StringReader("QUIT quit");
    Appendable output = new StringBuilder();

    ImageLibrary lib = new ImageStorage();

    Pixel[][] pix = new Pixel[2][2];
    pix[0][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(1, 1, 3)));
    pix[0][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(8, 1, 1)));
    pix[1][0] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(2, 1, 4)));
    pix[1][1] = new EightBitPixelModel(new ArrayList<>(Arrays.asList(5, 4, 3)));
    ImageModel mock = new PPMModel(2, 2, 255, pix);
    lib.put("mock", mock);
    ImageView view = new ImageViewImpl(lib, output);
    ImageController controller5 = new ImageControllerImpl(lib, view, in);
    controller5.process();
    assertEquals(output.toString(), "Application closed");
    assertEquals(outContent.toString(), "");
  }

  /**
   * Test null storage.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullLib() {
    Readable in = new StringReader("QUIT quit");
    ImageLibrary lib = new ImageStorage();
    ImageView view = new ImageViewImpl(lib);
    new ImageControllerImpl(null, view, in);
  }

  /**
   * Test null view.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullView() {
    Readable in = new StringReader("QUIT quit");
    ImageLibrary lib = new ImageStorage();
    ImageView view = new ImageViewImpl(lib);
    new ImageControllerImpl(lib, null, in);
  }

  /**
   * Test null readable.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullReadable() {
    Readable in = new StringReader("QUIT quit");
    ImageLibrary lib = new ImageStorage();
    ImageView view = new ImageViewImpl(lib);
    new ImageControllerImpl(lib, view, null);
  }


}