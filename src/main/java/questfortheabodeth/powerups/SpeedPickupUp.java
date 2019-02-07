package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

public class SpeedPickupUp extends Pickup
{
    private int timeout;
    private int addSpeed = 50;
    private Player p;

    public SpeedPickupUp(int x, int y, int timeout) {
        super(x, y, "res/assets/pickups/speedPickup.png");
        this.timeout = timeout;
    }

    //function for when picked up/ used:
    public void run() {
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            this.removeBuff(p);
        }
    }

    @Override
    public void applyBuff(Player p) {
        this.p = p;
        p.setMovementSpeed(p.getMovementSpeed() + addSpeed);
        this.run();
    }

    @Override
    public void removeBuff(Player p) {
        p.setMovementSpeed(p.getMovementSpeed() - addSpeed);
    }
}