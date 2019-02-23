package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.CollidableInteractableEnvironment;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Movable;
import main.java.questfortheabodeth.interfaces.TrapZone;
import main.java.questfortheabodeth.weapons.Bullet;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

public class ShootingArrows extends CollidableInteractableEnvironment implements TrapZone
{
    private long lastTimeTriggered;
    private int fireRate = 1000;
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
        return new FloatRect(this.getX() - 190, this.getY() - 190, 320, 320);

    }

    @Override
    public void trigger(CopyOnWriteArraySet<Movable> movables, CopyOnWriteArraySet<Collidable> collidables, CopyOnWriteArraySet<Drawable> drawables, CopyOnWriteArraySet<Bullet> bullets, Player player)
    {
        double angle = Helper.getAngleBetweenPoints(new Vector2i(new Vector2f(super.getX() + (super.getWidth() / 2), super.getY() + (super.getHeight() / 2))), new Vector2i(player.getVectorPosition()));
        System.out.println("Angle before: " + angle);
        angle += Settings.GENERATOR.nextInt(20) - 20;
        System.out.println("Angle after: " + angle);
        Bullet b = new Bullet(
                (int) this.getX() + (int)(getWidth()/2),
                (int) this.getY() + (int)(getHeight()/2),
                angle, 10, true, "arrow");

        movables.add(b);
        drawables.add(b);
        collidables.add(b);
        bullets.add(b);
        if (Settings.SOUND_EFFECTS_ON) {
            Helper.playAudio("arrow");
        }
    }

    public int getFireRate()
    {
        return fireRate;
    }

    public long getLastTimeTriggered()
    {
        return lastTimeTriggered;
    }

    public void setLastTimeTriggered(long lastTimeTriggered)
    {
        this.lastTimeTriggered = lastTimeTriggered;
    }
}