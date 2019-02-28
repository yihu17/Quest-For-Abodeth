package main.java.questfortheabodeth.interfaces;

import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.weapons.Bullet;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * A trrap zone is a trap that has a larger than normal
 * collision area to simulate the players presence activating
 * the trap
 */
public interface TrapZone extends Trap
{
    /**
     * Returns the traps new FloatRect to simulate a "range" of the trap
     *
     * @return (FloatRect) Bounds of the trap
     */
    FloatRect getGlobalBounds();

    /**
     * Triggers the trap. By passing in these lists the trap can affect the game
     * in whatever what it likes, including spawning new objects
     * @param movables (CopyOnWriteArraySet) List of movable objects in the game
     * @param collidables (CopyOnWriteArraySet) List of collidable objects
     * @param drawables (CopyOnWriteArraySet) List of drawable objects
     * @param bullets (CopyOnWriteArraySet) List of bullets on the screen
     * @param player (Player) The player object
     */
    void trigger(CopyOnWriteArraySet<Movable> movables, CopyOnWriteArraySet<Collidable> collidables, CopyOnWriteArraySet<Drawable> drawables, CopyOnWriteArraySet<Bullet> bullets, Player player);

    /**
     * Returns the last time that the arrow trap was triggered
     * @return (long) Last time the trap was triggered
     */
    long getLastTimeTriggered();

    /**
     * Sets the time that this trap was triggered last
     * @param lastTimeTriggered (long) Last time triggered
     */
    void setLastTimeTriggered(long lastTimeTriggered);
}
