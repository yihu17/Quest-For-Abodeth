package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

/**
 * The pickup that will end the game. By default this does nothing
 * as it is a purely symbolic pickup
 */
public class TheAbodeth extends Pickup
{
    /**
     * Creates a new Abodeth pickup
     *
     * @param xPos          (int) X position of the abodeth
     * @param yPos          (int) Y position of the abodeth
     * @param imageFilePath (String) Image file to load
     */
    public TheAbodeth(float xPos, float yPos, String imageFilePath)
    {
        super(xPos, yPos, "res/assets/pickups/abodeth.png");
    }

    /**
     * Does nothing as this is purely a symbolic pickup
     * @param p (Player) The player object
     */
    @Override
    public void applyBuff(Player p)
    {

    }

    /**
     * Does nothing as this is purely a symbolic pickup
     * @param p (Player) The player object
     */
    @Override
    public void removeBuff(Player p)
    {

    }
}
