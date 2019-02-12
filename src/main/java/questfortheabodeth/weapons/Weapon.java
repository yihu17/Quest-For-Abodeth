package main.java.questfortheabodeth.weapons;

public abstract class Weapon {
    private int damageDealt;
    private int fireRate;
    private String name;

    public Weapon(String name, int fireRate) {
        this.name = name;
        this.fireRate = fireRate;
    }

    public String getName() {
        return this.name;
    }
}