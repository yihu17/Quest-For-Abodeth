import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;

/**
 * An abstract class to define all the common attributes that both the player
 * and all monsters will have
 */
public abstract class Character implements Drawable
{
    private int x;
    private int y;
    private int health;
    private int movementSpeed = 5;
    private Image image;

    public Character(int x, int y, int health, String image)
    {
        this.x = x;
        this.y = y;
        this.health = health;
        this.image = new Image(x, y, image);
    }

    public void setImage(Image i)
    {
        this.image = i;
    }

    protected Image getImage()
    {
        return this.image;
    }

    public FloatRect getGlobalBounds()
    {
        return this.image.getGlobalBounds();
    }

    /**
     * Returns the X position of this character
     *
     * @return (int) X Position
     */
    public int getX()
    {
        return x;
    }

    /**
     * Returns the Y position of this character
     *
     * @return (int) Y Position
     */
    public float getY()
    {
        return y;
    }

    /**
     * Moves this character up by the current movement speed
     */
    public void moveUp()
    {
        y -= movementSpeed;
    }

    /**
     * Moves this character down by the current movement speed
     */
    public void moveDown()
    {
        y += movementSpeed;
    }

    /**
     * Moves this character left by the current movement speed
     */
    public void moveLeft()
    {
        x -= movementSpeed;
    }

    /**
     * Moves this character right by the current movement speed
     */
    public void moveRight()
    {
        x += movementSpeed;
    }

    /**
     * Updates the movement speed of this character. Used when moving th echaracter around the screen
     *
     * @param speed (int) Movement speed
     */
    public void setMovementSpeed(int speed)
    {
        this.movementSpeed = speed;
    }

    /**
     * Decrease the characters health by the specified amount. If the health of the character
     * falls below 0 then the kill function is called
     *
     * @param amount (int) Amount to decrease by
     */
    public void decreaseHealth(int amount)
    {
        this.health -= amount;
        if (health <= 0) {
            this.kill();
        }
    }

    /**
     * Increase the health of the character by the specified amount
     *
     * @param amount (int) Amount to increase by
     */
    public void increaseHealth(int amount)
    {
        this.health += amount;
    }

    public abstract void kill();
}
