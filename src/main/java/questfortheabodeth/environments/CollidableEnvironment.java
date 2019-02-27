package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.interfaces.Collidable;

/**
 * An environment object that is collidable, allowing movable objects
 * to hit this object
 */
public class CollidableEnvironment extends Environment implements Collidable
{
    private String filename;

    /**
     * Creates a new Colliable object
     *
     * @param xPos          (int) X position
     * @param yPos          (int) Y position
     * @param imageFilePath (String) Image file to load
     */
    public CollidableEnvironment(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath, true, false);
        this.filename = imageFilePath.split("/")[imageFilePath.split("/").length - 1];
    }

    /**
     * Provides a more helpful representation of this object
     * @return (String) Object information
     */
    @Override
    public String toString()
    {
        return "<CollidableEnvironment " + filename + " @ " + getGlobalBounds() + ">";
    }
}
