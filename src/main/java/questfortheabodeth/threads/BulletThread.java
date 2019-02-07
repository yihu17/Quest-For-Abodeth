package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.weapons.Bullet;

import java.util.concurrent.CopyOnWriteArraySet;

public class BulletThread extends Thread
{
    private CopyOnWriteArraySet<Bullet> bullets;
    private CopyOnWriteArraySet<Collidable> collidables;

    public BulletThread(CopyOnWriteArraySet<Bullet> b, CopyOnWriteArraySet<Collidable> c)
    {
        this.bullets = b;
        this.collidables = c;
    }

    @Override
    public void run()
    {
        for (Bullet b : bullets) {
            checkBullet(b);
        }
    }

    private void checkBullet(Bullet b)
    {
        for (Collidable c : collidables) {
            if (c instanceof Bullet) {
                continue;
            }
            if (0 < Helper.checkOverlap(b, c)) {
                if (c instanceof Enemy) {
                    ((Enemy) c).decreaseHealth(b.getDamage());
                    System.out.println("Bullet hit an enemy: " + c);
                }
                b.setX(2 * Settings.WINDOW_WIDTH);
                b.setY(2 * Settings.WINDOW_HEIGHT);
            }
        }
    }
}
