import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Paths;

public class Image implements Drawable
{
    private Sprite image;
    private Vector2f position;

    /**
     * Creates a new image object
     *
     * @param x        (int) X coordinate of the top left of the image
     * @param y        (int) Y coordinate of the top left of the image
     * @param filename (String) Image name to load
     */
    public Image(int x, int y, String filename)
    {
        position = new Vector2f(x, y);

        Texture imageTexture = new Texture();
        try {
            imageTexture.loadFromFile(Paths.get(filename));
            imageTexture.setSmooth(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        image = new Sprite(imageTexture);
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
        renderTarget.draw(this.image);
    }
}
