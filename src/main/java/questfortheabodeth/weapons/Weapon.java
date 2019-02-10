package main.java.questfortheabodeth.weapons;

import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

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