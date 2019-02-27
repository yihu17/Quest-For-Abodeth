package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Interactable;
import org.jsfml.graphics.FloatRect;

/**
 * An environment object that can be interacted with by the player or enemies but not allow
 * them to pass through it as it is collidable
 */
public class CollidableInteractableEnvironment extends Environment implements Collidable, Interactable
{
    /**
     * Creates a new Collidable and Interactable environment object
     *
     * @param xPos          (int) X position of the object
     * @param yPos          (int) Y position of the object
     * @param imageFilePath (String) Image path to load
     */
    public CollidableInteractableEnvironment(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath, true, true);
    }

    /**
     * Returns the X coordinate of this environment object.
     * X coordinate is the top left corner of the object
     * @return (float) X position of this object
     */
    @Override
    public float getX()
    {
        return super.getX();
    }

    /**
     * Returns the Y coordinate of this environment object.
     * Y coordinate is the top left corner of the object
     * @return (float) Y position of this object
     */
    @Override
    public float getY()
    {
        return super.getY();
    }

    /**
     * Returns the height of this environment object.
     * @return (float) Height of this object
     */
    @Override
    public float getHeight()
    {
        return super.getHeight();
    }

    /**
     * Returns the width of this environment object.
     * @return (float) Width of this object
     */
    @Override
    public float getWidth()
    {
        return super.getWidth();
    }

    /**
     * Returns the global bounds of this environment object
     * @see FloatRect
     * @return (FloatRect) Global bounds of this object
     */
    @Override
    public FloatRect getGlobalBounds()
    {
        return super.getGlobalBounds();
    }

    /**
     * The default Environment object has no interacts with the player
     * @param p (Player) The player object
     */
    @Override
    public void interact(Player p)
    {

    }

    /**
     * The default Environment object has no interacts with the player
     * @param p (Player) The player object
     */
    @Override
    public void remove(Player p)
    {

    }

    /**
     * The default Environment object has no interacts with enemies
     * @param e (Enemy) The Enemy object
     */
    @Override
    public void buffEnemy(Enemy e)
    {

    }

    /**
     * The default Environment object has no interacts with enemies
     * @param e (Enemy) The Enemy object
     */
    @Override
    public void removeEnemyBuff(Enemy e)
    {

    }
}