package main.java.questfortheabodeth.characters;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

/**
 * An abstract class to define all the common attributes that both the main.java.questfortheabodeth.characters
 * and all monsters will have
 */
public abstract class Character extends Thread implements Drawable, Collidable
{
    private Facing face;
    private int x;
    private int y;
    private SimpleIntegerProperty health = new SimpleIntegerProperty();
    private double movementSpeed;
    private int shield = 0;
    private int ammo = 0;
    private Image image;
    private int damage = 5;

    /**
     * Sets up the character
     *
     * @param x             (int) Characters X position
     * @param y             (int) Characters Y position
     * @param health        (int) Health of the character
     * @param image         (String) Name of the image to load
     * @param movementSpeed (int) Speed of this character for X and Y coordinates
     */
    public Character(int x, int y, int health, String image, int movementSpeed)
    {
        this.x = x;
        this.y = y;
        this.health.set(health);
        this.image = new Image(x, y, image);
        this.movementSpeed = movementSpeed;
        this.face = Facing.RIGHT;
    }

    public ReadOnlyIntegerProperty healthProperty()
    {
        return this.health;
    }

    /**
     * Moves this character left by the current movement speed
     */
    public void moveLeft()
    {
        if (this.face == Facing.RIGHT) {
            this.image.flipHorizontal();
            this.face = Facing.LEFT;
        }
        x -= movementSpeed;
        updatePosition();
    }

    public void addDamage(int damage)
    {
        this.damage += damage;
    }

    public int getDamage()
    {
        return this.damage;
    }

    /**
     * Returns the image object for the character
     * Only accessible in sub classes
     *
     * @return (Image) Characters images
     */
    protected Image getImage()
    {
        return this.image;
    }

    /**
     * Sets the image of this character
     * Overwrites the previous image
     *
     * @param i (Image) Characters image
     */
    public void setImage(Image i)
    {
        this.image = i;
    }

    /**
     * Returns the bounds of the character
     * Useful for checking overlaps and collisions
     *
     * @return (FloatRect) Global bounds of the character
     */
    public FloatRect getGlobalBounds()
    {
        return this.image.getGlobalBounds();
    }

    /**
     * Returns the X position of this character
     *
     * @return (int) X Position
     */
    @Override
    public float getX()
    {
        return x;
    }

    /**
     * Returns the Y position of this character
     *
     * @return (int) Y Position
     */
    @Override
    public float getY()
    {
        return y;
    }

    public Vector2f getVectorPosition()
    {
        return new Vector2f(x, y);
    }

    @Override
    public float getWidth()
    {
        return this.image.getGlobalBounds().width;
    }

    @Override
    public float getHeight()
    {
        return this.image.getGlobalBounds().height;
    }

    /**
     * Moves this character up by the current movement speed
     */
    public void moveUp()
    {
        y -= movementSpeed;
        updatePosition();
    }

    /**
     * Moves this character down by the current movement speed
     */
    public void moveDown()
    {
        y += movementSpeed;
        updatePosition();
    }

    /**
     * Moves this character right by the current movement speed
     */
    public void moveRight()
    {
        if (this.face == Facing.LEFT) {
            this.image.flipHorizontal();
            this.face = Facing.RIGHT;
        }
        x += movementSpeed;
        updatePosition();
    }

    private enum Facing
    {
        LEFT,
        RIGHT
    }

    /**
     * After any change to the X and Y values
     */
    private void updatePosition()
    {
        this.image.setPosition(x, y);
    }

    /**
     * Updates the movement speed of this character. Used when moving th echaracter around the screen
     *
     * @param speed (int) Movement speed
     */
    public void setMovementSpeed(double speed)
    {
        this.movementSpeed = speed;
    }

    public double getMovementSpeed()
    {
        return movementSpeed;
    }

    /**
     * Decrease the main.java.questfortheabodeth.characters health by the specified amount. If the health of the character
     * falls below 0 then the kill function is called
     *
     * @param amount (int) Amount to decrease by
     */
    public void decreaseHealth(int amount)
    {
        this.health.set(health.get() - amount);
        if (health.get() <= 0) {
            this.kill();
        }
    }

    public int getHealth()
    {
        return health.get();
    }

    public void addHealth(int healthBoost)
    {
        health.add(healthBoost);
    }

    public int getShield()
    {
        return shield;
    }

    public void addShield(int shieldAmount)
    {
        this.shield += shieldAmount;
    }

    public int getAmmo()
    {
        return ammo;
    }

    public void addAmmo(int ammoAmount)
    {
        this.ammo += ammoAmount;
    }

    /**
     * Increase the health of the character by the specified amount
     *
     * @param amount (int) Amount to increase by
     */
    public void increaseHealth(int amount)
    {
        this.health.add(amount);
    }

    /**
     * "Kill" the character
     * Different for enemies and players
     */
    public abstract void kill();

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(image);
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.image.setPosition(x, y);
    }

    public void run()
    {
        double prevMovementSpeed = this.movementSpeed;
        this.movementSpeed = 0;
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.movementSpeed = prevMovementSpeed;
    }
}
