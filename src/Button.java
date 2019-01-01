import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;


/**
 * Creates a rectangle that acts like a button.
 * Binding an EventHandler to a button allows it to perform
 * an action when clicked
 */
public class Button implements Drawable
{
    private final RectangleShape rectangleShape;
    private Color normalColor = Settings.LIGHT_GREY;
    private Color outlineColor = Settings.DARK_GREY;
    private Color onPressColor = Settings.GREY;
    private final Text text;
    private EventHandler onPress = null;

    public Button(int width, int height, int x, int y)
    {
        // Create the button background
        rectangleShape = new RectangleShape();
        rectangleShape.setSize(new Vector2f(width, height));
        rectangleShape.setPosition(new Vector2f(x, y));

        rectangleShape.setFillColor(normalColor);
        rectangleShape.setOutlineColor(outlineColor);
        rectangleShape.setOutlineThickness(5);


        // Create the text to go inside the button
        text = new Text("", Settings.ARIAL);
        text.setPosition(rectangleShape.getPosition());
        text.setColor(Color.BLACK);
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
        rectangleShape.setFillColor(normalColor);
    }

    /**
     * Sets the color of the border around teh button
     *
     * @param color (Color) New border color
     */
    public void setOutlineColor(Color color)
    {
        this.outlineColor = color;
        rectangleShape.setOutlineColor(outlineColor);
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

    /**
     * Bind a function to the button so that it runs when this button
     * is pressed
     * @param handle (EventHandler) Function to run on click
     */
    public void setOnPress(EventHandler handle)
    {
        this.onPress = handle;
    }

    /**
     * Determines whether or not the button is being pressed. If the mouse is within
     * the bounds of the buttons rectangle object then the bound function is run
     *
     * @param mousePos (Vector2f) Position of the mouse
     */
    public void press(Vector2f mousePos)
    {
        if (rectangleShape.getGlobalBounds().contains(mousePos)) {
            rectangleShape.setFillColor(onPressColor);
            if (onPress != null) {
                onPress.run();
            }
        }
    }

    /**
     * If the button has been pressed, the background clor has changed and so
     * when the mouse is released, change it back to the original color
     *
     * @param mousePos (Vector2f) Position of the mouse
     */
    public void release(Vector2f mousePos)
    {
        if (rectangleShape.getGlobalBounds().contains(mousePos))
            rectangleShape.setFillColor(normalColor);
    }

    /**
     * Allows the button to be drawn to the screen with the default draw method
     * @param renderTarget (RenderTarget) Where to draw to (usually the window)
     * @param renderStates (RenderStates) I have no idea what these are...
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        rectangleShape.draw(renderTarget, renderStates);
        text.draw(renderTarget, renderStates);
    }
}
