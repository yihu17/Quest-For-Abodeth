package main.java.questfortheabodeth.sprites;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Creates a wrapper for a Sprite that has the texture of an image
 * If the image has not already been loaded it is added to a static array
 * in the settings file to save on memory when loading again
 */
public class Image implements Drawable
{
    private Sprite image = null;
    private Vector2f position;

    /**
     * Creates a new image object.
     *
     * @param x        (int) X coordinate of the top left of the image
     * @param y        (int) Y coordinate of the top left of the image
     * @param filename (String) Filename of the image to load
     */
    public Image(int x, int y, String filename)
    {
        if (filename.equals("")) {
            return;
        }
        position = new Vector2f(x, y);
        if (Settings.LOADED_IMAGES.containsKey(filename)) {
            this.loadImageFromTexture(Settings.LOADED_IMAGES.get(filename)); //if having problems remove this line
        } else {
            this.loadImageFromFile(filename);
        }
    }

    public void flipHorizontal()
    {
        this.image.setTexture(Helper.flipTexture(this.image.getTexture()));
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

    /**
     * Load the image from a pre loaded texture
     *
     * @param texture (Texture) The loaded image
     */
    private void loadImageFromTexture(Texture texture)
    {
        image = new Sprite(texture);
        image.setPosition(position);
    }

    /**
     * Adjust the scale of the image
     *
     * @param scaleFactor (Vector2f) A vector of the scale factors
     */
    public void setScale(Vector2f scaleFactor)
    {
        image.setScale(scaleFactor);
    }

    /**
     * Returns the bounds of the image object
     *
     * @return (FloatRect) main.java.questfortheabodeth.sprites.Image bounds
     */
    public FloatRect getGlobalBounds()
    {
        return image.getGlobalBounds();
    }

    /**
     * Draws the image on screen.
     *
     * @param renderTarget (RenderTarget) The window to draw to
     * @param renderStates (RenderStates) Still don't know what these are
     * @throws NullPointerException Thrown if the image failed to load correctly
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        if (image == null) {
            throw new NullPointerException("No image has been loaded");
        }
        renderTarget.draw(this.image);
    }

    /**
     * Updates only the X position of the image
     *
     * @param x (float) New X position
     */
    public void setXPosition(float x)
    {
        float y = this.image.getPosition().y;
        this.image.setPosition(x, y);
    }

    /**
     * Updates only the Y position of the image
     *
     * @param y (float) New Y position
     */
    public void setYPosition(float y)
    {
        float x = this.image.getPosition().x;
        this.image.setPosition(x, y);
    }

    /**
     * Updates the position of the image
     *
     * @param x (float) New X position
     * @param y (float) New Y position
     */
    public void setPosition(float x, float y)
    {
        this.image.setPosition(x, y);
    }
}
