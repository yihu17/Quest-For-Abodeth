package main.java.questfortheabodeth.hud;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Player;
import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

public class MeleeRange extends CircleShape
{
    private boolean visible = true;
    private Player player;
    private int xOffset;
    private int yOffset;

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

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public boolean isVisible()
    {
        return this.visible;
    }

    public Player getPlayer() {
        return player;
    }

    public void xOffsetIncrease()
    {
        xOffset += 2;
    }

    public void yOffsetIncrease()
    {
        yOffset += 2;
    }

    public int getxOffset() {
        return xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }
}
