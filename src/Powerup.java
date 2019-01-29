import org.jsfml.graphics.Drawable;

/**
 * All powerups implement this so that their special effect can be applied
 * to the player
 */
public interface Powerup extends Drawable, Collidable
{
    /**
     * Applies this powerups special effect to the given
     * player object
     *
     * @param p (Player) Current player
     */
    void applyBuff(Player p);

    /**
     * Removes this powerups special effect to the given
     * player object
     * @param p (Player) Current player
     */
    void removeBuff(Player p);

    /**
     * Returns the Image object attached to this powerup so that it can be
     * drawn to the screen
     * @return (Image) Powerup image
     */
    Image getImage();
}
