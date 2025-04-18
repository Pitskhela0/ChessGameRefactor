package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Utility class to manage loading of resources like images.
 */
public class ResourceManager {
    /**
     * Loads an image from the classpath resources.
     *
     * @param filename The name of the image file to load
     * @return The loaded BufferedImage, or null if loading failed
     */
    public static BufferedImage loadImage(String filename) {
        try {
            // Try to load directly from resources directory (not in images subfolder)
            InputStream stream = ResourceManager.class.getClassLoader().getResourceAsStream(filename);

            if (stream == null) {
                // If not found, try with class resource
                stream = ResourceManager.class.getResourceAsStream("/" + filename);
            }

            if (stream == null) {
                System.err.println("Resource not found: " + filename);
                return null;
            }

            return ImageIO.read(stream);
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Alternative method to load resources directly from the current package.
     * Useful when resources are stored alongside classes rather than in a separate resource folder.
     *
     * @param filename The name of the image file to load
     * @return The loaded BufferedImage, or null if loading failed
     */
    public static BufferedImage loadResourceFromPackage(String filename) {
        try {
            InputStream stream = ResourceManager.class.getResourceAsStream("/" + filename);
            if (stream == null) {
                System.err.println("Package resource not found: " + filename);
                return null;
            }
            return ImageIO.read(stream);
        } catch (Exception e) {
            System.err.println("Error loading package resource: " + e.getMessage());
            return null;
        }
    }
}