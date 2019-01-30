import org.jsfml.graphics.Drawable;

/**
 * Any object that can be moved should implement this interface
 * so that all objects on the screen can be moved as part of
 * one single loop in the main class
 */
public interface Movable extends Drawable
{
    /**
     * Moves the object
     */
    void move();

    /**
     * Returns the top left X coordinate of the object
     *
     * @return (float) X coordinate
     */
    float getX();

    /**
     * Returns the top left Y coordinate of the object
     *
     * @return (float) Y coordinate
     */
    float getY();

    /**
     * Returns the height of the object
     *
     * @return (float) Object height
     */
    float getHeight();

    /**
     * Returns the width of the object
     *
     * @return (float) Object width
     */
    float getWidth();
}
