package main.java.questfortheabodeth.weapons;

public abstract class Weapon {
    private int damageDealt;
    private int fireRate;
    private String name;

    public Weapon(String name, int fireRate, int damage) {
        this.name = name;
        this.fireRate = fireRate;
        this.damageDealt = damage;
    }

    public String getName() {
        return this.name;
    }

    public int getFireRate() {
        return this.fireRate;
    }

    public int getDamageDealt()
    {
        return damageDealt;
    }

    public void setDamageDealt(int damageDealt)
    {
        this.damageDealt = damageDealt;
    }
}