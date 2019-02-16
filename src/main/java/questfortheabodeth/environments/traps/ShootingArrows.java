package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.environments.CollidableEnvironment;
import org.jsfml.graphics.FloatRect;

public class ShootingArrows extends CollidableEnvironment implements TrapZone
{

    public ShootingArrows(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath);
    }

    @Override
    public void effect()
    {
        //if
    }

    @Override
    public FloatRect getGlobalBounds()
    {
        //create new floatRect (upper right to lower left)
        return super.getGlobalBounds(); //swap with new floatrect
    }

    @Override
    public void trigger()
    {
        System.out.println("Shooting arrow trap triggered!");
    }
}