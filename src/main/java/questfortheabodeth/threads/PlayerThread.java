package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.Environment;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Powerup;
import main.java.questfortheabodeth.weapons.Bullet;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class PlayerThread extends Thread
{
    private Player player;
    private CopyOnWriteArraySet<Collidable> collidables;
    private int returnValue;

    public PlayerThread(Player p, CopyOnWriteArraySet<Collidable> cs)
    {
        this.player = p;
        this.collidables = cs;
    }

    @Override
    public void run()
    {
        HashSet<Integer> playerCanMove = new HashSet<>();
        int moveValues = 0;
        for (Collidable c : collidables) {
            if (c instanceof Player || c instanceof Bullet) {
                continue;
            }
            if (c instanceof Environment) {
                int overlap = Helper.checkOverlap(player, c);
                if (0 < overlap) {
                    playerCanMove.add(overlap);
                }
            }

            if (c instanceof Powerup) {
                int overlap = Helper.checkOverlap(player, c);
                if (0 < overlap) {
                    ((Powerup) c).applyBuff(player);
                }
            }
        }
        for(Integer i: playerCanMove) {
            moveValues += i;
        }

        this.returnValue = moveValues;
    }

    public int getReturnValue()
    {
        return returnValue;
    }
}
