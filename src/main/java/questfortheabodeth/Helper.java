package main.java.questfortheabodeth;

import main.java.questfortheabodeth.interfaces.Clickable;
import main.java.questfortheabodeth.interfaces.Collidable;
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
     * Checks whether or not 2 character objects are overlapping and where the overlap has
     * occurred
     * @param o1 (main.java.questfortheabodeth.interfaces.Collidable) The character to check against other main.java.questfortheabodeth.characters
     * @param o2 (main.java.questfortheabodeth.interfaces.Collidable) The character to check against
     * @return (int) Whether or no the main.java.questfortheabodeth.characters are overlapping
     */
    public static int checkOverlap(Collidable o1, Collidable o2)
    {
        // Generate information about object 1
        FloatRect o1Bound = new FloatRect(o1.getX() - 5, o1.getY() - 5, o1.getWidth() + 10, o1.getHeight() + 10);
        Vector2i o1Center = new Vector2i(
                (int) (o1Bound.left + (o1Bound.width / 2)),
                (int) (o1Bound.top + (o1Bound.height / 2))
        );

        // Generate information about object 2
        FloatRect o2Bound = new FloatRect(o2.getX(), o2.getY(), o2.getWidth(), o2.getHeight());
        Vector2i o2Center = new Vector2i(
                (int) (o2Bound.left + (o2Bound.width / 2)),
                (int) (o2Bound.top + (o2Bound.height / 2))
        );

        if (o1Bound.intersection(o2Bound) == null) {
            // If there is no intersection in the 2 collidables then they are not
            // overlapping
            return 0;
        }

        double angle = getAngleBetweenPoints(o1Center, o2Center);

        if ((325 <= angle && angle <= 360) || (0 <= angle && angle <= 35)) {
            return 1;
        } else if (55 <= angle && angle <= 125) {
            return 2;
        } else if (145 <= angle && angle <= 215) {
            return 4;
        } else if (235 <= angle && angle <= 305) {
            return 8;
        }

        return 0;
        //throw new AssertionError("Invalid angle (" + angle + ")");
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

        /*
            Depending on which quadrant the angle is in we need to add
            the offset
         */
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
