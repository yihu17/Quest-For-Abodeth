package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

public class DamagePlus extends Pickup {
    private int damageBoost = 10;
    private int timeout;
    private Player p;

    public DamagePlus(int x, int y, int timeout) {
        super(x, y, "res/assets/pickups/damagePickup.png");
        this.timeout = timeout;
    }

    //function for when picked up/ used:
    @Override
    public void applyBuff(Player p) {
        this.p = p;
        System.out.println("Getting buffed by damage boost");
        p.addDamage(damageBoost);
        System.out.println("Damage is now " + p.getDamage());
        new Thread(this).start();
    }

    @Override
    public void removeBuff(Player p) {
        p.addDamage(-damageBoost);
        System.out.println("Damage is now " + p.getDamage());
    }

    public void run() {
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            System.out.println(e);
        }

        removeBuff(p);
    }
}
