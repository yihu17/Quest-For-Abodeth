import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;


/**
 * Creates a rectangle that acts like a button.
 * Binding an EventHandler to a button allows it to perform
 * an action when clicked
 */
public class Button extends RectangleShape implements Clickable
{
    private Color normalColor = Settings.LIGHT_GREY;
    private Color outlineColor = Settings.DARK_GREY;
    private Color onPressColor = Settings.GREY;
    private int borderThickness = 5;

    private final Text text;
    private EventHandler onPress = null;

    public Button(int width, int height, int x, int y)
    {
        this(width, height, x, y, "");
    }

    public Button(int width, int height, int x, int y, String text)
    {
        // Create the button background
        super();
        this.setSize(new Vector2f(width, height));
        this.setPosition(new Vector2f(x, y));

        this.setFillColor(normalColor);
        this.setOutlineColor(outlineColor);
        this.setOutlineThickness(borderThickness);


        // Create the text to go inside the button
        this.text = new Text(text, Settings.ARIAL);
        this.text.setPosition(this.getPosition());
        this.text.setColor(Color.BLACK);
    }

    /**
     * Changes the color of the Text inside the button
     *
     * @param c (Color) Color of the text
     */
    public void setTextColor(Color c)
    {
        this.text.setColor(c);
    }

    /**
     * Set the text inside the button
     *
     * @param t (String) Text
     */
    public void setText(String t)
    {
        text.setString(t);
    }

    /**
     * Sets the background color of the button
     *
     * @param color (Color) New background color
     */
    public void setColor(Color color)
    {
        this.normalColor = color;
        this.setFillColor(normalColor);
    }

    /**
     * Sets the color of the border around teh button
     *
     * @param color (Color) New border color
     */
    @Override
    public void setOutlineColor(Color color)
    {
        this.outlineColor = color;
        super.setOutlineColor(outlineColor);
    }

    /**
     * Sets the color of the button when it is down (being pressed)
     *
     * @param color (Color) New down color
     */
    public void setOnPressColor(Color color)
    {
        this.onPressColor = color;
    }

    /**
     * Used to reposition the text within a button to make it
     * centered rather than top-left
     * @param offset (int) Amount to move the text left by
     */
    public void setTextXOffset(int offset)
    {
        Vector2f f = text.getPosition();
        text.setPosition(new Vector2f(f.x + offset, f.y));
    }

    /**
     * Used to reposition the text within a button to make it
     * centered rather than top-left
     * @param offset (int) Amount to move the text down by
     */
    public void setTextYOffset(int offset)
    {
        Vector2f f = text.getPosition();
        text.setPosition(new Vector2f(f.x, f.y + offset));
    }

    @Override
    public void setOnPress(EventHandler handle)
    {
        this.onPress = handle;
    }

    @Override
    public void press(Vector2f mousePos)
    {
        if (this.getGlobalBounds().contains(mousePos)) {
            this.setFillColor(onPressColor);
            if (onPress != null) {
                onPress.run();
            }
        }
    }

    @Override
    public void release(Vector2f mousePos)
    {
        if (this.getGlobalBounds().contains(mousePos))
            this.setFillColor(normalColor);
    }

    /**
     * Allows the button to be drawn to the screen with the default draw method
     * @param renderTarget (RenderTarget) Where to draw to (usually the window)
     * @param renderStates (RenderStates) I have no idea what these are...
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        super.draw(renderTarget, renderStates);
        text.draw(renderTarget, renderStates);
    }

    /**
     * Returns the text on this button object
     * @return (String) Button text
     */
    public String getText()
    {
        return this.text.getString();
    }
}
