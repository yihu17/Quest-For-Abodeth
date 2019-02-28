package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.weapons.Bullet;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * A thread that handles all the bullet collision in the game.
 * This was moved to a thread so that other, similar, checks can
 * be run in parallel as opposed to doing everything linearly in
 * the main thread
 */
public class BulletThread extends Thread
{
    private CopyOnWriteArraySet<Bullet> bullets;
    private CopyOnWriteArraySet<Collidable> collidables;

    /**
     * Creates a new bullet thread
     *
     * @param b (CopyOnWriteArraySet) List of all the bullets on the screen
     * @param c (CopyOnWriteArraySet) List of all collidable objects on the screen
     */
    public BulletThread(CopyOnWriteArraySet<Bullet> b, CopyOnWriteArraySet<Collidable> c)
    {
        this.bullets = b;
        this.collidables = c;
    }

    /**
     * Runs the thread. For every bullet check to see if it has hit a collidable
     * object on the screen
     */
    @Override
    public void run()
    {
        for (Bullet b : bullets) {
            checkBullet(b);
        }
    }

    /**
     * For every colliable in the list, check it against the bullet for a collision.
     * If a collision has occurred then remove the bullet from the screen
     * @param b (Bullet) The bullet to check
     */
    private void checkBullet(Bullet b)
    {
        for (Collidable c : collidables) {
            // If the collidable is another bullet, ignore it
            if (c instanceof Bullet) {
                continue;
            }

            // Check for an overlap
            if (0 < Helper.checkOverlap(b, c)) {
                // If the bullet hit an enemy its health needs to be decreased
                if (c instanceof Enemy) {
                    ((Enemy) c).decreaseHealth(b.getDamage());
                    System.out.println("Bullet hit an enemy: " + c);
                }

                // Move the bullet out the map so it is eventually removed
                b.setX(2 * Settings.WINDOW_WIDTH);
                b.setY(2 * Settings.WINDOW_HEIGHT);
            }
        }
    }
}
