package main.java.questfortheabodeth.weapons;

public abstract class Gun extends Weapon {
    private int bulletsPerShot;
    private int maxAmmo;

    public Gun(String name, int bulletsPerShot, int maxAmmo, int fireRate, int damage) {
        super(name, fireRate ,damage);
        this.bulletsPerShot = bulletsPerShot;
        this.maxAmmo = maxAmmo;
    }

    public int getMaxAmmo()
    {
        return this.maxAmmo;
    }

    public int getBulletsPerShot()
    {
        return this.bulletsPerShot;
    }
}