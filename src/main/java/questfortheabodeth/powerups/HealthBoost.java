package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

public class HealthBoost extends Pickup {
    private int healthBoostage = 50;

    public HealthBoost(int x, int y) {
        super(x, y, "res/assets/pickups/healthPickup.png");
    }

    //function for when picked up/ used:
    @Override
    public void applyBuff(Player p) {
        System.out.println("Getting buffed by Health");
        p.addHealth(healthBoostage);
        System.out.println("Health is now " + p.getHealth());
    }


    @Override
    public void removeBuff(Player p) {
    }
}