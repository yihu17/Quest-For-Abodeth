package main.java.questfortheabodeth.powerups;

import main.java.questfortheabodeth.interfaces.Powerup;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;

/**
 * The base class for all pickups. Provides methods to draw the pickup
 * on the screen and methods so that collision detection can be run on
 * these objects so that they can be picked up.
 */
public abstract class Pickup extends Thread implements Drawable, Powerup
{
    private float xPos;
    private float yPos;
    private Image image;

    /**
     * Creates a new Pickup object
     *
     * @param xPos          (int) X position of this pickup
     * @param yPos          (int) Y position of this pickup
     * @param imageFilePath (String) Image file to load
     */
    public Pickup(float xPos, float yPos, String imageFilePath)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = new Image((int) xPos, (int) yPos, imageFilePath);
    }

    /**
     * Draws this pickup onto the screen
     * @param renderTarget (RenderTarget) Where to render the pickup
     * @param renderStates (RenderStates) ???
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(image);
    }

    /**
     * Returns the image of this pickup
     * @return (Image) Pickups image
     */
    @Override
    public Image getImage()
    {
        return image;
    }

    /**
     * Returns the current X position of this pickup
     * @return (float) X position
     */
    @Override
    public float getX()
    {
        return xPos;
    }

    /**
     * Returns the current Y position of this pickup
     * @return (float) Y position
     */
    @Override
    public float getY()
    {
        return yPos;
    }

    /**
     * Returns the current height of this pickup as seen by {@link Sprite#getGlobalBounds()}
     * @return (float) Height of this object
     */
    @Override
    public float getHeight()
    {
        return image.getGlobalBounds().height;
    }

    /**
     * Returns the current width of this pickup as seen by {@link Sprite#getGlobalBounds()}
     * @return (float) Width of this object
     */
    @Override
    public float getWidth()
    {
        return image.getGlobalBounds().width;
    }
}