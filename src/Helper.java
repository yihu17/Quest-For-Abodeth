import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import java.util.List;

public class Helper
{
    /**
     * Checks the given event to check whether or not the user is interacting with any of the button on the screen
     *
     * @param e       (Event) Generated event object
     * @param buttons (List) List of buttons to check
     */
    public static void checkButtons(Event e, List<Clickable> buttons)
    {
        Vector2f mousePosition;
        if (e.type == Event.Type.MOUSE_BUTTON_PRESSED) {
            Vector2i pos = e.asMouseEvent().position;
            mousePosition = new Vector2f((float) pos.x, (float) pos.y);
            buttons.forEach(button -> button.press(mousePosition));
        } else if (e.type == Event.Type.MOUSE_BUTTON_RELEASED) {
            Vector2i pos = e.asMouseEvent().position;
            mousePosition = new Vector2f((float) pos.x, (float) pos.y);
            buttons.forEach(button -> button.release(mousePosition));
        }
    }

    /**
     * Checks the event for any form of close operation and closes the window
     * @param e (Event) Generated event object
     * @param window (RenderWindow) Window object to close
     */
    public static void checkCloseEvents(Event e, RenderWindow window)
    {
        if (e.type == Event.Type.CLOSED) {
            window.close();
        } else if (e.type == Event.Type.KEY_PRESSED) {
            if (e.asKeyEvent().key == Keyboard.Key.F4) {
                window.close();
            }
        }
    }

    /**
     * Takes the width and height of an image and calculates the scale
     * factor required to make it the same size as the screen
     *
     * @param width  (float) Width of the image
     * @param height (float) Height of the image
     * @return (Vector2f) Scale factor to be applied
     */
    public static Vector2f getScaleValueToWindowSize(float width, float height)
    {
        return new Vector2f(
                Settings.WINDOW_WIDTH / width,
                Settings.WINDOW_HEIGHT / height
        );
    }

    /**
     * Takes the width and height of an image and calculates the scale
     * factor required to make it the same size as the given dimensions
     *
     * @param width        (float) Width of the image
     * @param height       (float) Height of the image
     * @param targetWidth  (float) The target width of the image
     * @param targetHeight (float) The target height of the image
     * @return (Vector2f) Scale factor to be applied
     */
    public static Vector2f getScaleValue(float width, float height, float targetWidth, float targetHeight)
    {
        return new Vector2f(
                targetWidth / width,
                targetHeight / height
        );
    }

    /**
     * Print method that takes a 2D array of objects and prints them to the console
     *
     * @param object (Object[][]) The object to print
     */
    public static void printMatrix(Object[][] object)
    {
        for (int i = 0; i < object.length; i++) {
            Object[] row = object[i];
            for (int j = 0; j < row.length; j++) {
                System.out.print(object[i][j] + " ");
            }
            System.out.println("\n");
        }
    }

    /**
     * Checks whether or not 2 character objects are overlapping
     *
     * @param o1 (Character) The character to check against other characters
     * @param o2 (Character) The character to check against
     * @return (boolean) Whether or no the characters are overlapping
     */
    public static boolean checkOverlap(Character o1, Character o2)
    {
        return false;
    }
}
