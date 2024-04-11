import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class ImageLoader {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src\\java\\config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Retrieve image paths dynamically
        String image1Path = properties.getProperty("logoimage");

        // You can use these image paths as needed
        System.out.println("Image 1 Path: " + image1Path);

        // You can now use these paths to load or display the images in your application.
    }
}