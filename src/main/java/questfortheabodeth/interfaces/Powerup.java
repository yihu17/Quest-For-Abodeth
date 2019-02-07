package main.java.questfortheabodeth.interfaces;

import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Drawable;

/**
 * All main.java.questfortheabodeth.powerups implement this so that their special effect can be applied
 * to the main.java.questfortheabodeth.characters
 */
public interface Powerup extends Drawable, Collidable
{
    /**
     * Applies this main.java.questfortheabodeth.powerups special effect to the given
     * main.java.questfortheabodeth.characters object
     *
     * @param p (main.java.questfortheabodeth.characters.Player) Current main.java.questfortheabodeth.characters
     */
    void applyBuff(Player p);

    /**
     * Removes this main.java.questfortheabodeth.powerups special effect to the given
     * main.java.questfortheabodeth.characters object
     * @param p (main.java.questfortheabodeth.characters.Player) Current main.java.questfortheabodeth.characters
     */
    void removeBuff(Player p);

    /**
     * Returns the main.java.questfortheabodeth.sprites.Image object attached to this powerup so that it can be
     * drawn to the screen
     * @return (main.java.questfortheabodeth.sprites.Image) main.java.questfortheabodeth.interfaces.Powerup image
     */
    Image getImage();
}
