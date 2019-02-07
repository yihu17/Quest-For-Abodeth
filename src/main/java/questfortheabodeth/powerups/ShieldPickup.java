package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

public class ShieldPickup extends Pickup
{
    private int shieldHealth = 50;

    public ShieldPickup(int x, int y) {
        super(x, y, "res/assets/pickups/shieldPickup.png");
    }

    //function for when picked up/ used:
    @Override
    public void applyBuff(Player p) {
        System.out.println("Getting buffed by shield");
        p.addShield(shieldHealth);
        System.out.println("Shield is now " + p.getShield());
    }

    @Override
    public void removeBuff(Player p) {

    }

}