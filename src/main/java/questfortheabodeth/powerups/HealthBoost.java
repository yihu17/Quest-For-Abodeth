package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

public class HealthBoost extends Pickup {

    private int healthBoostage = 50;
    private int timeout;
    private Player p;

    public HealthBoost(int x, int y, int timeout) {
        super(x, y, "res/assets/pickups/healthPickup.png");
        this.timeout = timeout;
    }

    //function for when picked up/ used:
    public void run() {
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            System.out.println(e);
        }

        removeBuff(p);
    }

    @Override
    public void applyBuff(Player p) {
        this.p = p;
        System.out.println("Getting buffed by item coordinate: " + this.getX() + " " + this.getY());
        p.setMovementSpeed(10);
        System.out.println("Speed is now " + p.getMovementSpeed());
        new Thread(this).start();
    }


    @Override
    public void removeBuff(Player p) {
        p.setMovementSpeed(6);
    }
}