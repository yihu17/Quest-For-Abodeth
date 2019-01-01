import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.io.File;
import java.io.IOException;

public class Button implements Drawable
{
    public static final Font ARIAL = new Font();
    static {
        File f = new File("res/arial.ttf");
        try {
            ARIAL.loadFromFile(f.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private RectangleShape rectangleShape;
    private Text text;

    public Button(int width, int height, int x, int y)
    {
        rectangleShape = new RectangleShape();
        rectangleShape.setSize(new Vector2f(width, height));
        rectangleShape.setPosition(new Vector2f(x, y));
        rectangleShape.setOutlineColor(Color.BLUE);
        rectangleShape.setOutlineThickness(5);

        text = new Text("", ARIAL);
        text.setPosition(rectangleShape.getPosition());
        text.setColor(Color.BLACK);
    }

    public void setText(String t)
    {
        text.setString(t);
    }

    public void setColor(Color c)
    {
        rectangleShape.setFillColor(c);
    }

    public void setTextColor(Color c)
    {
        text.setColor(c);
    }

    public void setTextXOffset(int offset)
    {
        Vector2f f = text.getPosition();
        text.setPosition(new Vector2f(f.x + offset, f.y));
    }

    public void setTextYOffset(int offset)
    {
        Vector2f f = text.getPosition();
        text.setPosition(new Vector2f(f.x, f.y + offset));
    }

    public FloatRect getGlobalBounds()
    {
        return rectangleShape.getGlobalBounds();
    }

    public void press()
    {
        System.out.println("Pressed button: " + this);
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        rectangleShape.draw(renderTarget, renderStates);
        text.draw(renderTarget, renderStates);
    }
}
