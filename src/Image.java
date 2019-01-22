import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.nio.file.Paths;

public class Image implements Drawable
{
    private Sprite image;
    private Vector2f position;

    public Image(int x, int y, int w, int h, String filename)
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
        image.setScale(Helper.getScaleValue(3840.0f, 2160.0f));
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(this.image);
    }
}
