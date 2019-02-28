package main.java.questfortheabodeth.weapons;

/**
 * Two handed weapon class
 */
public class TwoHandedWeapon extends Gun
{
    /**
     * Creates a new two handed weapon
     *
     * @param name           (String) Name of the weapon
     * @param bulletsPerShot (int) Number of bullets fired per shot
     * @param maxAmmo        (int) Maximum ammo this weapon can hav
     * @param fireRate       (int) Time between firing in milliseconds
     * @param damage         (int) Amount of damage the weapon does
     */
    public TwoHandedWeapon(String name, int bulletsPerShot, int maxAmmo, int fireRate, int damage)
    {
        super(name, bulletsPerShot, maxAmmo, fireRate, damage);
    }
}
