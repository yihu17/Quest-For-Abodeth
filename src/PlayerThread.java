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
            if (c instanceof Player) {
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
