package main.java.questfortheabodeth.weapons;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Movable;
import main.java.questfortheabodeth.sprites.Image;

/**
 * Class that describes a bullet. Once fired, a bullet will travel in a straight
 * line across the map and then disappear when it is off the screen. If the bullet
 * hits a collidable it will disappear.
 */
public class Bullet extends Image implements Movable, Collidable
{
    private float x;
    private float y;
    private double xSpeed;
    private double ySpeed;
    private double angle;
    private int damage;

    /**
     * Creates a new image object.
     *
     * @param x (int) X coordinate of the top left of the image
     * @param y (int) Y coordinate of the top left of the image
     * @param angle (double) Angle the bullet is travelling at clockwise about the vertical
     */
    public Bullet(int x, int y, double angle)
    {
        super(x, y, "res/assets/weapons/bullet.png");
        this.setScale(Helper.getScaleValue(64, 64, 16, 16));
        this.angle = angle;
        this.xSpeed = Settings.BULLET_SPEED * Math.sin(Math.toRadians(angle));
        this.ySpeed = Settings.BULLET_SPEED * Math.cos(Math.toRadians(angle)) * -1;
        this.x = x;
        this.y = y;
        this.damage = 5;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    /**
     * Increase the x and y coordinates of the bullet
     * and set the position of the image
     */
    @Override
    public void move()
    {
        x += xSpeed;
        y += ySpeed;
        this.setPosition(x, y);
    }

    /**
     * Override the toString method to make it easier to understand
     * where bullets are
     *
     * @return (String) This bullet as a string
     */
    @Override
    public String toString()
    {
        return "<main.java.questfortheabodeth.weapons.Bullet " + this.hashCode() + " @ [" + x + ", " + y + "] w/ angle " + angle + ">";
    }

    /**
     * Returns the X position of this bullet
     * @return (float) X coordinate
     */
    @Override
    public float getX()
    {
        return x;
    }

    /**
     * Returns the Y position of this bullet
     * @return (float) Y coordinate
     */
    @Override
    public float getY()
    {
        return y;
    }

    /**
     * Returns the height of the image used by the bullet
     * @return (float) Height of the bullet
     */
    @Override
    public float getHeight()
    {
        return this.getGlobalBounds().height;
    }

    /**
     * Returns the width of the image used by the bullet
     * @return (float0 Width of the bullet
     */
    @Override
    public float getWidth()
    {
        return this.getGlobalBounds().width;
    }

    public int getDamage()
    {
        return damage;
    }
}
