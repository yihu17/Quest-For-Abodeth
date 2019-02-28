package main.java.questfortheabodeth.characters;

import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.interfaces.Movable;
import main.java.questfortheabodeth.interfaces.TrapZone;
import main.java.questfortheabodeth.weapons.Bullet;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;

import java.util.concurrent.CopyOnWriteArraySet;

public class EgyptianMummy extends Enemy implements TrapZone, Interactable
{
    /**
     * Creates a new enemy
     *
     * @param xPos          (int) Start X coordinate
     * @param yPos          (int) Start Y coordinate
     * @param health        (int) Health points
     * @param movementSpeed (int) How fast can this enemy move
     * @param name          (String) Name of the enemy
     * @param attackSpeed   (int) How often can this enemy attack
     * @param attackPower   (int) How much damage does this enemy deal
     */
    public EgyptianMummy(int xPos, int yPos, int health, int movementSpeed, String name, int attackSpeed, int attackPower)
    {
        super(xPos, yPos, health, "res/assets/enemies/egyptianMummy.png", movementSpeed, name, attackSpeed, attackPower);
    }

    /**
     * Triggers the trap. By passing in these lists the trap can affect the game
     * in whatever what it likes, including spawning new objects
     *
     * @param movables    (CopyOnWriteArraySet) List of movable objects in the game
     * @param collidables (CopyOnWriteArraySet) List of collidable objects
     * @param drawables   (CopyOnWriteArraySet) List of drawable objects
     * @param bullets     (CopyOnWriteArraySet) List of bullets on the screen
     * @param player      (Player) The player object
     */
    @Override
    public void trigger(CopyOnWriteArraySet<Movable> movables, CopyOnWriteArraySet<Collidable> collidables, CopyOnWriteArraySet<Drawable> drawables, CopyOnWriteArraySet<Bullet> bullets, Player player)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                double original = player.getMovementSpeed();
                player.setMovementSpeed(original / 2);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // Ignore this
                } finally {
                    player.setMovementSpeed(original);
                }
            }
        }).start();
    }

    @Override
    public FloatRect getGlobalBounds()
    {
        return new FloatRect(
                getX() - getWidth(),
                getY() - getHeight(),
                3 * getWidth(),
                3 * getHeight()
        );
    }

    /**
     * Run the traps effect
     */
    @Override
    public void effect()
    {

    }

    /**
     * Apply an effect to the player
     *
     * @param p (Player) The player object
     */
    @Override
    public void interact(Player p)
    {

    }

    /**
     * Remove the aforementioned effect from the player
     *
     * @param p (Player) The player object
     */
    @Override
    public void remove(Player p)
    {

    }

    @Override
    public void buffEnemy(Enemy e)
    {

    }

    @Override
    public void removeEnemyBuff(Enemy e)
    {

    }
}
