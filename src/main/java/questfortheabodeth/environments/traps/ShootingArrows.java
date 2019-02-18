package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.CollidableEnvironment;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Movable;
import main.java.questfortheabodeth.weapons.Bullet;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

public class ShootingArrows extends CollidableEnvironment implements TrapZone
{

    private ArrayList<Movable> movables;
    private ArrayList<Drawable> drawables;
    private ArrayList <Collidable> collidables;

    public ShootingArrows(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath);
    }

    @Override
    public void effect()
    {
        //
    }

    @Override
    public FloatRect getGlobalBounds()
    {
        return new FloatRect(this.getX()-50, this.getY()-50,140,140);
    }

    @Override
    public void trigger()
    {
        Bullet b = new Bullet(
                (int) this.getX(),
                (int) this.getY(),
                90 ,
                10);

        movables.add(b); //?
        drawables.add(b); //?
        collidables.add(b); //?
        System.out.println("Shooting arrow trap triggered!");
    }

    public ArrayList<Movable> getMovables()
    {
        return this.movables;
    }

    public ArrayList<Drawable> getDrawables()
    {
        return this.drawables;
    }

    public ArrayList<Collidable> getCollidables()
    {
        return this.collidables;
    }
}