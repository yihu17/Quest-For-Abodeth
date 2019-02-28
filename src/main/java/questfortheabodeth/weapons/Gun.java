package main.java.questfortheabodeth.weapons;

/**
 * A gun class so that weapons can be typed more effectively e.g.
 * are they a {@link Melee} weapon of a wepaon that fires things
 */
public abstract class Gun extends Weapon
{
    /**
     * Creates a new gun object
     *
     * @param name           (String) The name of this gun
     * @param bulletsPerShot (int) The number of bullets fired per shot
     * @param maxAmmo        (int) Maximum ammo this gun can have
     * @param fireRate       (int) Time between firing in milliseconds
     * @param damage         (int) Amount of damage this gun does
     */
    public Gun(String name, int bulletsPerShot, int maxAmmo, int fireRate, int damage)
    {
        super(name, fireRate, damage);
    }
}