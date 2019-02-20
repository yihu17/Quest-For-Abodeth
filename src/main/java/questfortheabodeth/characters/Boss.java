package main.java.questfortheabodeth.characters;

import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.powerups.TheAbodeth;
import org.jsfml.graphics.Drawable;

import java.util.concurrent.CopyOnWriteArraySet;

public class Boss extends Enemy
{
    private CopyOnWriteArraySet<Drawable> drawables = null;
    private CopyOnWriteArraySet<Collidable> interactables = null;

    public Boss(int xPos, int yPos, int health, String imageFilePath, int movementSpeed, String name, int attackSpeed, int attackPower)
    {
        super(xPos, yPos, health, imageFilePath, movementSpeed, name, attackSpeed, attackPower);
        System.out.println("Created the boss from path " + imageFilePath);
    }

    public void setGameOver(CopyOnWriteArraySet<Drawable> d, CopyOnWriteArraySet<Collidable> i)
    {
        this.drawables = d;
        this.interactables = i;
    }

    @Override
    public void decreaseHealth(int amount)
    {
        super.decreaseHealth(amount);

        // TODO: Play Ed screaming
    }

    @Override
    public void kill()
    {
        // TODO: Do a weird fancy thing to the image
        TheAbodeth abodeth = new TheAbodeth(
                (float)(getX() + 0.5 * getWidth()),
                (float)(getY() + 0.5 * getHealth()),
                "res/assets/enemies/bat.png"
        );
        drawables.add(abodeth);
        interactables.add(abodeth);

        super.kill();
    }
}
