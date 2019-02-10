package main.java.questfortheabodeth.weapons;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public class WeaponPickup implements Drawable, Collidable {
    private int xPos;
    private int yPos;
    private String name;
    private Image image;

    public WeaponPickup(int xPos, int yPos, String filePath, String name) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.name = name;
        this.image = new Image(xPos, yPos, filePath);
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

    public String getName() {
        return this.name;
    }

    public void remove() {
        this.xPos = 2 * Settings.WINDOW_WIDTH;
        this.yPos = 2 * Settings.WINDOW_HEIGHT;
        this.image.setPosition(this.xPos, this.yPos);
    }

    public void move(int x, int y) {
        this.xPos = x;
        this.yPos = y;
        this.image.setPosition(xPos, yPos);
    }
}
