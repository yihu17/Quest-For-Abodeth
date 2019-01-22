import org.jsfml.graphics.Drawable;
import org.jsfml.system.Vector2f;

public interface Clickable extends Drawable
{
    /**
     * Bind a function to the button so that it runs when this button
     * is pressed
     *
     * @param handler (EventHandler) Function to run on click
     */
    void setOnPress(EventHandler handler);

    /**
     * Determines whether or not the object is being pressed. If the mouse is within
     * the bounds of the objects rectangle object then the bound EventHandler is run
     *
     * @param mousePosition (Vector2f) Position of the mouse
     */
    void press(Vector2f mousePosition);

    /**
     * If the button has been pressed, the background clor has changed and so
     * when the mouse is released, change it back to the original color
     *
     * @param mousePosition (Vector2f) Position of the mouse
     */
    void release(Vector2f mousePosition);
}
