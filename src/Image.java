import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Paths;

public class Image implements Drawable
{
    private Sprite image = null;
    private Vector2f position;

    /**
     * Creates a new image object
     *
     * @param x (int) X coordinate of the top left of the image
     * @param y (int) Y coordinate of the top left of the image
     */
    public Image(int x, int y, String filename)
    {
        position = new Vector2f(x, y);
        if (Settings.LOADED_IMAGES.containsKey(filename)) {
            this.loadImageFromTexture(Settings.LOADED_IMAGES.get(filename));
        } else {
            this.loadImageFromFile(filename);
        }
    }

    /**
     * Load an image file from the file system
     *
     * @param filename (String) Image name to load
     */
    public void loadImageFromFile(String filename)
    {
        Texture imageTexture = new Texture();
        try {
            imageTexture.loadFromFile(Paths.get(filename));
            imageTexture.setSmooth(true);
            Settings.LOADED_IMAGES.put(filename, imageTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }

        image = new Sprite(imageTexture);
        image.setPosition(position);
    }

    public void loadImageFromTexture(Texture texture)
    {
        image = new Sprite(texture);
        image.setPosition(position);
    }

    public void setScale(Vector2f scaleFactor)
    {
        image.setScale(scaleFactor);
    }

    public FloatRect getGlobalBounds()
    {
        return image.getGlobalBounds();
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        if (image == null) {
            throw new NullPointerException("No image has been loaded");
        }
        renderTarget.draw(this.image);
    }
}
