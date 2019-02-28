package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.Environment;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Powerup;
import main.java.questfortheabodeth.weapons.Bullet;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The player thread checks all the collision of the player against other
 * objects on the screen
 */
public class PlayerThread extends Thread
{
    private Player player;
    private CopyOnWriteArraySet<Collidable> collidables;
    private int returnValue;

    /**
     * Creates a new player thread
     *
     * @param p  (Player) The player object
     * @param cs (CopyOnWriteArraySet) List of collidable objects
     */
    public PlayerThread(Player p, CopyOnWriteArraySet<Collidable> cs)
    {
        this.player = p;
        this.collidables = cs;
    }

    /**
     * Runs the player collision thread
     * @see Helper#checkOverlap(Collidable, Collidable)
     */
    @Override
    public void run()
    {
        // Gets all the values of how the player can move
        HashSet<Integer> playerCanMove = new HashSet<>();
        int moveValues = 0;
        for (Collidable c : collidables) {
            // Ensure we don't chek against bullets and the player itself
            if (c instanceof Player || c instanceof Bullet) {
                continue;
            }

            // If we are checking an environment object, add the returned value to the set
            if (c instanceof Environment) {
                int overlap = Helper.checkOverlap(player, c);
                if (0 < overlap) {
                    playerCanMove.add(overlap);
                }
            }

            // If we are checking a powerup and the player is overlapping it apply the buff
            if (c instanceof Powerup) {
                int overlap = Helper.checkOverlap(player, c);
                if (0 < overlap) {
                    ((Powerup) c).applyBuff(player);
                }
            }
        }

        // Sum the set
        for (Integer i : playerCanMove) {
            moveValues += i;
        }

        // Set the return value for the thread
        // to be used in the game loop
        this.returnValue = moveValues;
    }

    /**
     * Returns the last summed number from the movement algorithm. This is used
     * to determine which directions the player can move in in the main thread
     * @see main.java.questfortheabodeth.Settings#MOVE_RIGHT_SET
     * @see main.java.questfortheabodeth.Settings#MOVE_LEFT_SET
     * @see main.java.questfortheabodeth.Settings#MOVE_UP_SET
     * @see main.java.questfortheabodeth.Settings#MOVE_DOWN_SET
     * @return (int) Player movement value
     */
    public int getReturnValue()
    {
        return returnValue;
    }
}
