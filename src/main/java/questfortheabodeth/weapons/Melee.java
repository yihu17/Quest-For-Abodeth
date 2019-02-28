package main.java.questfortheabodeth.weapons;

/**
 * Class that models a melee weapon.
 */
public class Melee extends Weapon
{
    /**
     * Creates a new melee weapon
     *
     * @param name     (String) The name of this weapon
     * @param fireRate (int) Time between firing this weapon in milliseconds
     * @param damage   (int) How much damage this weapon does
     */
    public Melee(String name, int fireRate, int damage)
    {
        super(name, fireRate, damage);
    }
}