package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.interfaces.Powerup;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public abstract class Pickup extends Thread implements Drawable, Powerup
{
    float xPos;
    float yPos;
    private Image image;


    public Pickup(float xPos, float yPos, String imageFilePath) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = new Image((int)xPos, (int)yPos, imageFilePath);
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        renderTarget.draw(image);
    }

    //getter methods
    @Override
    public Image getImage() {
        return image;
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
    public float getHeight() {
        return image.getGlobalBounds().height;
    }

    @Override
    public float getWidth() {
        return image.getGlobalBounds().width;
    }
}