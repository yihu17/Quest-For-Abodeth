package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.interfaces.Collidable;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * A thread to check enemy collisions and ensure that they do not pass through walls
 */
public class EnemyThread extends Thread
{
    private CopyOnWriteArraySet<Collidable> collidables;
    private CopyOnWriteArraySet<Enemy> enemies;

    /**
     * Creates a new enemy thread
     *
     * @param e  (CopyOnWriteArraySet) List of enemies
     * @param cs (CopyOnWriteArraySet) List of collidables
     */
    public EnemyThread(CopyOnWriteArraySet<Enemy> e, CopyOnWriteArraySet<Collidable> cs)
    {
        this.enemies = e;
        this.collidables = cs;
    }

    /**
     * For every enemy check them against all collidable for any collisions
     */
    @Override
    public void run()
    {
        for (Enemy e : enemies) {
            checkEnemy(e);
        }
    }

    /**
     * Check the given enemy against every collidable on the map. If there is a
     * is a collision set the value of the collision so the enemy knows how to move
     * @param e (Enemy) The enemy to check
     */
    private void checkEnemy(Enemy e)
    {
        for (Collidable c : collidables) {
            if (c instanceof Enemy) {
                continue;
            }

            e.setMoveValue(Helper.checkOverlap(e, c));
        }
    }
}
