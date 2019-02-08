package main.java.questfortheabodeth.weapons;

public class Gun extends Weapon {
    private int bulletsPerShot;
    private int maxAmmo;

    public Gun(int xPos, int yPos, String filePath) {
        super(xPos, yPos, filePath);
    }
}