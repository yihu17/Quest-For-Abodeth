package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;

public class AmmoPickup extends Pickup
{
    private int ammo = 50;

    public AmmoPickup(int x, int y)
    {
        super(x, y, "res/assets/pickups/ammoPickup.png");
    }

    //function for when picked up/ used:

    @Override
    public void applyBuff(Player p)
    {
        p.increaseAmmo(ammo);
    }

    @Override
    public void removeBuff(Player p)
    {

    }
}