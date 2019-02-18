package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.environments.CollidableInteractableEnvironment;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Movable;
import main.java.questfortheabodeth.interfaces.TrapZone;
import main.java.questfortheabodeth.weapons.Bullet;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;

import java.util.concurrent.CopyOnWriteArraySet;

public class ShootingArrows extends CollidableInteractableEnvironment implements TrapZone
{
    public ShootingArrows(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath);
        System.out.println("Created new Shooting arrow trap");
    }

    @Override
    public void effect()
    {
        //
    }

    @Override
    public FloatRect getGlobalBounds()
    {
        FloatRect r = new FloatRect(this.getX() - 50, this.getY() - 50, 140, 140);
        System.out.println(r);
        return r;
    }

    @Override
    public void trigger(CopyOnWriteArraySet<Movable> movables, CopyOnWriteArraySet<Collidable> collidables, CopyOnWriteArraySet<Drawable> drawables, CopyOnWriteArraySet<Bullet> bullets)
    {
        Bullet b = new Bullet(
                (int) this.getX(),
                (int) this.getY(),
                90 ,
                10);

        movables.add(b); //?
        drawables.add(b); //?
        collidables.add(b); //?
        bullets.add(b);
        System.out.println("Shooting arrow trap triggered!");
    }
}