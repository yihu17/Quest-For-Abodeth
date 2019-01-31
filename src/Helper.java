import org.jsfml.graphics.FloatRect;
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
                System.exit(1);
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
    public static boolean checkOverlap(Collidable o1, Collidable o2)
    {
        FloatRect o1Bound = new FloatRect(o1.getX() - o1.getWidth(), o1.getY() - o1.getHeight(), 3 * o1.getWidth(), 3 * o1.getHeight());
        Vector2f topleft = new Vector2f(o2.getX(), o2.getHeight());
        Vector2f topright = new Vector2f(o2.getX() + o2.getWidth(), o2.getY());
        Vector2f bottomleft = new Vector2f(o2.getX(), o2.getY() + o2.getHeight());
        Vector2f bottomright = new Vector2f(o2.getX() + o2.getWidth(), o2.getY() + o2.getHeight());

        System.out.println(o1Bound);
        System.out.println(topleft + " | " + topright + " | " + bottomleft + " | " + bottomright);

        if (!o1Bound.contains(topleft) && !o1Bound.contains(topright) && !o1Bound.contains(bottomleft) && !o1Bound.contains(bottomright)) {
            return false;
        }

        return true;
    }

    /**
     * Calculate the vector between 2 vector points in the window.
     * Returns an angle clockwise about the vertical
     *
     * @param p1 (Vector2i) Point 1
     * @param p2 (Vector2i) Point 2
     * @return (float) Angle about the vertical
     */
    public static double getAngleBetweenPoints(Vector2i p1, Vector2i p2)
    {
        double xDiff = p1.x - p2.x;     // = 691 - 200 = 491
        double yDiff = p1.y - p2.y;     // = 200 - 393 = -193
        int quadrant;
        if (xDiff < 0 && 0 <= yDiff) {
            quadrant = 1; // Top right quadrant
        } else if (xDiff < 0 && yDiff < 0) {
            quadrant = 2;
        } else if (0 <= xDiff && 0 <= yDiff) {
            quadrant = 4;
        } else if (0 <= xDiff && yDiff < 0) {
            quadrant = 3;
        } else {
            quadrant = 5;
        }

        double xBar = Math.abs(xDiff);
        double yBar = Math.abs(yDiff);
        switch (quadrant) {
            case 1:
                return Math.toDegrees(Math.atan2(xBar, yBar));
            case 2:
                return 180 - Math.toDegrees(Math.atan2(xBar, yBar));
            case 3:
                return 180 + Math.toDegrees(Math.atan2(xBar, yBar));
            case 4:
                return 360 - Math.toDegrees(Math.atan2(xBar, yBar));
            default:
                throw new AssertionError(quadrant + " is not a valid quadrant");
        }


    }
}
