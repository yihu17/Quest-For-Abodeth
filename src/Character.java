import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

/**
 * An abstract class to define all the common attributes that both the player
 * and all monsters will have
 */
public abstract class Character implements Drawable, Collidable
{
    private int x;
    private int y;
    private int health;
    private int movementSpeed;
    private Image image;
    private int damage = 5;

    /**
     * Sets up the character
     *
     * @param x      (int) Character X position
     * @param y      (int) Character Y position
     * @param health (int) Health of the character
     * @param image  (String) Name of the image to load
     */
    public Character(int x, int y, int health, String image, int movementSpeed)
    {
        this.x = x;
        this.y = y;
        this.health = health;
        this.image = new Image(x, y, image);
        this.movementSpeed = movementSpeed;
    }

    public void setDamage(int damage)
    {
        this.damage = damage;
    }

    public int getDamage()
    {
        return this.damage;
    }

    /**
     * Sets the image of this character
     * Overwrites the previous image
     * @param i (Image) Character image
     */
    public void setImage(Image i)
    {
        this.image = i;
    }

    /**
     * Returns the image object for the character
     * Only accessible in sub classes
     * @return (Image) Characters images
     */
    protected Image getImage()
    {
        return this.image;
    }

    /**
     * Returns the bounds of the character
     * Useful for checking overlaps and collisions
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
     * Moves this character left by the current movement speed
     */
    public void moveLeft()
    {
        x -= movementSpeed;
        updatePosition();
    }

    /**
     * Moves this character right by the current movement speed
     */
    public void moveRight()
    {
        x += movementSpeed;
        updatePosition();
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
    public void setMovementSpeed(int speed)
    {
        this.movementSpeed = speed;
    }

    public int getMovementSpeed() {
        return movementSpeed;
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

    public int getHealth()
    {
        return health;
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

    /**
     * "Kill" the character
     * Different for enemies and players
     */
    public abstract void kill();

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        renderTarget.draw(image);
    }
}
