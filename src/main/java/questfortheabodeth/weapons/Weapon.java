package main.java.questfortheabodeth.weapons;

/**
 * The base weapon class. Any weapon in the game will expend from this class
 */
public abstract class Weapon
{
    private int damageDealt;
    private int fireRate;
    private String name;

    /**
     * Creates a new weapon
     *
     * @param name     (String) Name of the weapon
     * @param fireRate (int) Time between firing this weapon in milliseconds
     * @param damage   (int) Amount of damage that this weapon does
     */
    public Weapon(String name, int fireRate, int damage)
    {
        this.name = name;
        this.fireRate = fireRate;
        this.damageDealt = damage;
    }

    /**
     * Returns the name of this weapon. Should directly correlate to a filename
     * @return (String) Weapon name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns the fire rate of this weapon in milliseconds
     * @return (int) Time between shots in milliseconds
     */
    public int getFireRate()
    {
        return this.fireRate;
    }

    /**
     * Returns that amount of damage that this weapon does
     * @return (int) Weapon damage
     */
    public int getDamageDealt()
    {
        return damageDealt;
    }
}