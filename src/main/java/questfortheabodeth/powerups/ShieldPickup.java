package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

public class ShieldPickup extends Pickup
{
    private int shieldHealth;
    private int timeout;

    public ShieldPickup(int x, int y, int timeout) {
        super(x, y, "res/assets/pickups/shieldPickup.png");
        this.timeout = timeout;
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    //function for when picked up/ used:
    @Override
    public void applyBuff(Player p) {

    }

    @Override
    public void removeBuff(Player p) {

    }

}