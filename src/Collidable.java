/**
 * Any object that can be hit by another object must implement this class
 */
public interface Collidable
{
    /**
     * Returns the top left X coordinate of the object
     *
     * @return (float) X coordinate
     */
    float getX();

    /**
     * Returns the top left Y coordinate of the object
     * @return (float) Y coordinate
     */
    float getY();

    /**
     * Returns the height of the object
     * @return (float) Object height
     */
    float getHeight();

    /**
     * Returns the width of the object
     * @return (float) Object width
     */
    float getWidth();
}
