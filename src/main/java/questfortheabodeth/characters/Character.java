package main.java.questfortheabodeth.characters;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * An abstract class to define all the common attributes that both the {@link Player} and
 * all {@link Enemy} objects will have
 */
public abstract class Character extends Thread implements Drawable, Collidable {
    private Facing face;
    private float x;
    private float y;
    private SimpleIntegerProperty health = new SimpleIntegerProperty();
    private double movementSpeed;
    private double finalSpeed;
    private int shield = 0;
    private Image image;
    private int additionalDamage = 5;

    /**
     * Sets up the character
     *
     * @param x             (int) Characters X position
     * @param y             (int) Characters Y position
     * @param health        (int) Health of the character
     * @param image         (String) Name of the image to load
     * @param movementSpeed (int) Speed of this character for X and Y coordinates
     */
    public Character(int x, int y, int health, String image, int movementSpeed) {
        this.x = x;
        this.y = y;
        this.health.set(health);
        this.image = new Image(x, y, image);
        this.movementSpeed = movementSpeed;
        this.face = Facing.RIGHT;
        finalSpeed = movementSpeed;
    }

    /**
     * Returns the health of this character as a read only property
     * This means that the health can onyl be increased and decreased
     * through the relevant methods
     *
     * @return (ReadOnlyIntegerProperty) Health property of this character
     * @see Character#decreaseHealth(int)
     * @see Character#increaseHealth(int)
     */
    public ReadOnlyIntegerProperty healthProperty() {
        return this.health;
    }

    /**
     * Adds additional damage to this characters normal damage. This is used to
     * buff the player when they pickup a damage boost as well as buff enemies
     * when they are over the relevant environment objects
     *
     * @param damage (int) Amount to increase the base damage by
     */
    public void addDamage(int damage) {
        this.additionalDamage += damage;
    }

    /**
     * Returns the additional damange that this character currently has through various
     * buffs
     * @return (int) Additional damage
     */
    public int getAdditionalDamage() {
        return this.additionalDamage;
    }

    /**
     * Returns the image object for the character
     * Only accessible in sub classes
     *
     * @return (Image) Characters images
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Sets the image of this character
     * Overwrites the previous image
     *
     * @param i (Image) Characters image
     */
    public void setImage(Image i) {
        this.image = i;
    }

    /**
     * Returns the bounds of the character
     * Useful for checking overlaps and collisions
     *
     * @return (FloatRect) Global bounds of the character
     */
    public FloatRect getGlobalBounds() {
        return this.image.getGlobalBounds();
    }

    /**
     * Returns the X position of this character
     *
     * @return (int) X Position
     */
    @Override
    public float getX() {
        return x;
    }

    /**
     * Returns the Y position of this character
     *
     * @return (int) Y Position
     */
    @Override
    public float getY() {
        return y;
    }

    /**
     * Returns the current position of this character in vector form
     *
     * @see Vector2f
     *
     * @return (Vector2f) Current position
     */
    public Vector2f getVectorPosition() {
        return new Vector2f(x, y);
    }

    /**
     * Returns the current width of this character as seen by the
     * {@link Sprite#getGlobalBounds()} method
     *
     * @return (float) Width of the character
     */
    @Override
    public float getWidth() {
        return this.image.getGlobalBounds().width;
    }

    /**
     * Returns the current height of this character as seen by the
     * {@link Sprite#getGlobalBounds()} method
     *
     * @return (float) Height of the character
     */
    @Override
    public float getHeight() {
        return this.image.getGlobalBounds().height;
    }

    /**
     * Moves this character up by the current movement speed
     */
    public void moveUp() {
        y -= movementSpeed;
        updatePosition();
    }

    /**
     * Moves this character down by the current movement speed
     */
    public void moveDown() {
        y += movementSpeed;
        updatePosition();
    }

    /**
     * Moves this character right by the current movement speed
     * If the character is not currently facing the direction they are moving in
     * the image will be flipped to represent this
     *
     * @see Image#flipHorizontal()
     */
    public void moveRight() {
        if (this.face == Facing.LEFT) {
            this.image.flipHorizontal();
            this.face = Facing.RIGHT;
        }
        x += movementSpeed;
        updatePosition();
    }

    /**
     * Moves this character left by the current movement speed
     * If the character is not currently facing the direction they are moving in
     * the image will be flipped to represent this
     *
     * @see Image#flipHorizontal()
     */
    public void moveLeft() {
        if (this.face == Facing.RIGHT) {
            this.image.flipHorizontal();
            this.face = Facing.LEFT;
        }
        x -= movementSpeed;
        updatePosition();
    }

    /**
     * Describes which way the character is currently facing
     */
    public enum Facing {
        LEFT,
        RIGHT
    }

    /**
     * Returns the current direction this character is facing
     * @return (Facing) Direction
     */
    public Facing getFacingDirection() {
        return face;
    }

    /**
     * After any change to the X and Y values of the character the base image
     * must be updated to reflect this
     */
    private void updatePosition() {
        this.image.setPosition(x, y);
    }

    /**
     * Updates the movement speed of this character. Used when moving th echaracter around the screen
     *
     * @param speed (int) Movement speed
     */
    public void setMovementSpeed(double speed) {
        this.movementSpeed = speed;
        finalSpeed = speed;
    }

    /**
     * Returns teh current movement speed of this character
     * @return (double) Movement speed
     */
    public double getMovementSpeed() {
        return movementSpeed;
    }


    /**
     * Decrease the main.java.questfortheabodeth.characters health by the specified amount. If the health of the character
     * falls below 0 then the kill function is called
     *
     * @param amount (int) Amount to decrease by
     */
    public void decreaseHealth(int amount) {
        int newValue = health.get() - amount;
        this.health.set(newValue < 0 ? 0 : newValue);
        System.out.println("Health decreased to " + this.health.get());
        if (health.get() <= 0) {
            this.kill();
        }
    }

    /**
     * Increase the health of the character by the specified amount
     *
     * @param amount (int) Amount to increase by
     */
    public void increaseHealth(int amount) {
        int newValue = health.get() + amount;
        if (newValue > 100) {
            newValue = 100;
        }
        this.health.set(newValue < 0 ? 0 : newValue);
    }

    /**
     * Returns the amount of health this character has as an integer
     * @return (int) Current health
     */
    public int getHealth() {
        return health.get();
    }

    /**
     * Returns the current amount of shield a character has
     * @return (int) Current shield
     */
    public int getShield() {
        return shield;
    }

    /**
     * Increases the amount of shield this character currently has
     * @param shieldAmount (int) How much to increase the shield by
     */
    public void addShield(int shieldAmount) {
        this.shield += shieldAmount;
    }

    /**
     * "Kill" the character
     * Different for enemies and players
     */
    public abstract void kill();

    /**
     * Draws this characters image to the screen
     * @param renderTarget (RenderTarget) Window to draw the image to
     * @param renderStates (RenderStates) ???
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        renderTarget.draw(image);
    }

    /**
     * Sets the current position of this character
     * @param x (int) X coordinate
     * @param y (int) Y coordinate
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        this.image.setPosition(x, y);
    }

    /**
     * Temporarily stops this character from moving to simulate
     * a recoil from a {@link main.java.questfortheabodeth.weapons.Bullet}
     */
    @Override
    public void run() {
        this.movementSpeed = 0;

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.movementSpeed = finalSpeed;
    }

    /**
     * The character is classes as alive if its health is greater than 0.
     * @return (boolean) True if alive, false otherwise
     */
    public boolean isCharacterAlive() {
        return !this.health.lessThanOrEqualTo(0).getValue();
    }
}
