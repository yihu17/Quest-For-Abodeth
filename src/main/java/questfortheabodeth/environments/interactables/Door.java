package main.java.questfortheabodeth.environments.interactables;

import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;
import org.jsfml.graphics.FloatRect;


/**
 * The Door class. Provides a way of typing the current
 * environment object to link doors together
 */
public class Door extends InteractableEnvironment
{
    private int linkedDoor;

    /**
     * Creates a new door. Each door has a integer value and a linked value.
     * - Doors on the left have a value of -1 and the linked door on the
     * right has a value of -3
     * - This is because when the player leaves through the left door they
     * enter the next room through the right door
     *
     * @param xPos          (int) X position of the door
     * @param yPos          (int) Y position of the door
     * @param imageFilePath (String) Image to load
     * @param linkedDoor    (int) The door this door is linked to
     */
    public Door(int xPos, int yPos, String imageFilePath, int linkedDoor)
    {
        super(xPos, yPos, imageFilePath);
        this.linkedDoor = linkedDoor;
    }

    /**
     * Does nothing because its a door
     * @param p (Player) The player
     */
    @Override
    public void interact(Player p)
    {

    }

    /**
     * Does nothing because its a door
     * @param p (Player) The player
     */
    @Override
    public void remove(Player p)
    {

    }

    /**
     * Does nothing because its a door
     * @param e (Enemy) The enemy
     */
    @Override
    public void buffEnemy(Enemy e)
    {

    }

    /**
     * Does nothing because its a door
     * @param e (Enemy) The enemy
     */
    @Override
    public void removeEnemyBuff(Enemy e)
    {

    }

    /**
     * Returns a bounds of 3 times the width and 3 times the height of the door so
     * that a player can interact with without having to overlap it
     *
     * @return (FloatRect) Door bounds
     */
    @Override
    public FloatRect getGlobalBounds()
    {
        return new FloatRect(
                getX() - getWidth(),
                getY() - getHeight(),
                3 * getWidth(),
                3 * getHeight()
        );
    }

    /**
     * Returns the door that this door is linked to
     * @return (int) The linked door number
     */
    public int getLinkedDoor()
    {
        return linkedDoor;
    }
}
