package main.java.questfortheabodeth;

import main.java.questfortheabodeth.interfaces.Clickable;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.weapons.Melee;
import main.java.questfortheabodeth.weapons.OneHandedWeapon;
import main.java.questfortheabodeth.weapons.TwoHandedWeapon;
import main.java.questfortheabodeth.weapons.Weapon;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.List;
import java.util.Set;

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
     *
     * @param e      (Event) Generated event object
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
                System.out.print(String.format("%-35s", object[i][j]) + " ");
            }
            System.out.println("\n");
        }
    }

    /**
     * Checks whether or not 2 character objects are overlapping and where the overlap has
     * occurred
     *
     * @param o1 (Collidable) The character to check against other main.java.questfortheabodeth.characters
     * @param o2 (Collidable) The character to check against
     * @return (int) Whether or no the characters are overlapping
     */
    public static int checkOverlap(Collidable o1, Collidable o2)
    {
        FloatRect o1Bound = new FloatRect(o1.getX() - 5, o1.getY() - 5, o1.getWidth() + 10, o1.getHeight() + 10);
        // Generate information about object 2
        FloatRect o2Bound = new FloatRect(o2.getX(), o2.getY(), o2.getWidth(), o2.getHeight());

        return checkOverlap(o1Bound, o2Bound);
    }

    public static int checkOverlap(Collidable o1, FloatRect o2Bound)
    {
        FloatRect o1Bound = new FloatRect(o1.getX() - 5, o1.getY() - 5, o1.getWidth() + 10, o1.getHeight() + 10);
        // Generate information about object 2

        return checkOverlap(o1Bound, o2Bound);
    }

    public static int checkOverlap(FloatRect o1Bound, FloatRect o2Bound)
    {
        Vector2i o1Center = new Vector2i(
                (int) (o1Bound.left + (o1Bound.width / 2)),
                (int) (o1Bound.top + (o1Bound.height / 2))
        );
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

        if ((320 <= angle && angle <= 360) || (0 <= angle && angle <= 40)) {
            return 1;
        } else if (50 <= angle && angle <= 130) {
            return 2;
        } else if (140 <= angle && angle <= 220) {
            return 4;
        } else if (230 <= angle && angle <= 310) {
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

    /**
     * Takes a sprites texture and flips it horizontally
     * If the transform fails the original texture is returned
     *
     * @param t (ConstTexture) Texture to flip
     * @return (Texture) Flipped texture
     */
    public static Texture flipTexture(ConstTexture t)
    {
        org.jsfml.graphics.Image i = t.copyToImage();
        i.flipHorizontally();
        Texture n = new Texture();
        try {
            n.loadFromImage(i);
            return n;
        } catch (TextureCreationException e) {
            e.printStackTrace();
            return (Texture) t;
        }

    }

    public static Weapon stringToWeapon(String weapon)
    {
        switch (weapon) {
            case "machete":
                return new Melee("machete", 50, 5);
            case "revolver":
                return new OneHandedWeapon("revolver", 1, 50, 333, 5);
            case "shotgun":
                return new TwoHandedWeapon("shotgun", 5, 20, 1000, 5);
            case "ar15":
                return new TwoHandedWeapon("ar15", 1, 25, 500, 50);
            case "uzi":
                return new OneHandedWeapon("uzi", 3, 60, 75, 5);
        }
        throw new AssertionError("Unknown weapon encountered: " + weapon);
    }

    public static void playAudio(String sound)
    {
        if (Settings.AUDIO_ON) {
            Clip AudioPlayer;
            try {
                File soundFile = new File("res/assets/audio/" + Settings.AUDIO_KEYS.get(sound) + ".wav");
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                AudioPlayer = AudioSystem.getClip();
                AudioPlayer.open(audioInputStream);
                AudioPlayer.start();
                Settings.AUDIO_STREAMERS.add(AudioPlayer);
            } catch (Exception e) {
                System.out.println("Audio error");
                e.printStackTrace();
            }
        }
    }

    public static double getLengthOfAudioFile(String sound)
    {
        try {
            File file = new File("res/assets/audio/" + Settings.AUDIO_KEYS.get(sound) + ".wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double length  = (frames + 0.0) / format.getFrameRate() * 1000;
            return length;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void stopAllAudio() {
        try {
            for (int i = 0; i < Settings.AUDIO_STREAMERS.size(); i++) {
                Settings.AUDIO_STREAMERS.get(i).stop();
                Settings.AUDIO_STREAMERS.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Settings.BACKGROUND_AUDIO_PLAYING = false;
    }
}