package main.java.questfortheabodeth.weapons;

import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public abstract class Weapon implements Drawable, Collidable {
    private int damageDealt;
    private int fireRate;
    private Image image;
    private int xPos;
    private int yPos;

    public Weapon(int xPos, int yPos, String filePath) {
        this.xPos = xPos;
        this.yPos = yPos;
        image = new Image(xPos, yPos, filePath);
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        renderTarget.draw(image);
    }

    @Override
    public float getX() {
        return xPos;
    }

    @Override
    public float getY() {
        return yPos;
    }

    @Override
    public float getWidth() {
        return this.image.getGlobalBounds().width;
    }

    @Override
    public float getHeight() {
        return this.image.getGlobalBounds().height;
    }
}