package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

public class TheAbodeth extends Pickup
{
    public TheAbodeth(float xPos, float yPos, String imageFilePath)
    {
        super(xPos, yPos, "res/assets/pickups/abodeth.png");
    }

    @Override
    public void applyBuff(Player p)
    {

    }

    @Override
    public void removeBuff(Player p)
    {

    }
}
