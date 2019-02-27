package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.interfaces.Interactable;

/**
 * Any environment object that is interactable ust extend this class and
 * override the interact method. This allows different types of object to
 * provide different effects on the player
 */
public abstract class InteractableEnvironment extends Environment implements Interactable
{
    /**
     * Creates a new interactable environment object
     *
     * @param xPos          (int) X position of this object
     * @param yPos          (int) Y position of this object
     * @param imageFilePath (String) Image file to load
     */
    public InteractableEnvironment(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath, false, true);
    }

    /**
     * The effect that is applied to the player by this interactable
     * environment object
     * @param p (Player) The player object
     */
    public abstract void interact(Player p);
}