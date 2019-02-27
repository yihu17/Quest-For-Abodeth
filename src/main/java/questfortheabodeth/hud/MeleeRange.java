package main.java.questfortheabodeth.hud;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Player;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

/**
 * Creates an expanding circle around the player to show
 * that the melee weapon has been used
 */
public class MeleeRange extends CircleShape
{
    private boolean visible = true;
    private Player player;
    private int xOffset;
    private int yOffset;

    /**
     * Creates a new expanding circle
     *
     * @param startPosition (Vector2f) Start position of the circle
     * @param p             (Player) The player to move the circle around
     */
    public MeleeRange(Vector2f startPosition, Player p)
    {
        this.player = p;
        this.setPosition(startPosition);
        xOffset = 1;
        yOffset = 1;
        this.setRadius(1);
        this.setOutlineColor(new Color(Settings.DARK_SAND, 128));
        this.setOutlineThickness(2);
        this.setFillColor(Color.TRANSPARENT);
    }

    /**
     * Whether or not the circle is currently visible
     * @param visible (boolean) Sets the circle visible
     */
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    /**
     * Returns true if the circle is currently visible or false if it is hidden
     * @return (boolean) Whethere or not the circle is showing
     */
    public boolean isVisible()
    {
        return this.visible;
    }

    /**
     * Returns the player object that this expanding circle is bound to
     * @return (Player) The player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Increases the offset of the X coordinate by 2
     */
    public void xOffsetIncrease()
    {
        xOffset += 2;
    }

    /**
     * Increases the offset of the Y coordinate by 2
     */
    public void yOffsetIncrease()
    {
        yOffset += 2;
    }

    /**
     * Returns the current X offset of this cirlce
     * @return (int) X offset
     */
    public int getxOffset() {
        return xOffset;
    }

    /**
     * Returns the current Y offset of this cirlce
     * @return (int) Y offset
     */
    public int getyOffset() {
        return yOffset;
    }
}
