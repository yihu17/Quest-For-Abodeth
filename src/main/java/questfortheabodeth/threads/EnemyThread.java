package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.interfaces.Collidable;

import java.util.concurrent.CopyOnWriteArraySet;

public class EnemyThread extends Thread
{
    private CopyOnWriteArraySet<Collidable> collidables;
    private CopyOnWriteArraySet<Enemy> enemies;
    private int returnValue;

    public EnemyThread(CopyOnWriteArraySet<Enemy> e, CopyOnWriteArraySet<Collidable> cs)
    {
        this.enemies = e;
        this.collidables = cs;
    }

    @Override
    public void run()
    {
        for (Enemy e : enemies) {
            checkEnemy(e);
        }
    }

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
