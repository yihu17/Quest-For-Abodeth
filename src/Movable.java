import org.jsfml.graphics.Drawable;

/**
 * Any object that can be moved should implement this interface
 * so that all objects on the screen can be moved as part of
 * one single loop in the main class
 */
public interface Movable extends Drawable, Collidable
{
    /**
     * Moves the object
     */
    void move();
}
