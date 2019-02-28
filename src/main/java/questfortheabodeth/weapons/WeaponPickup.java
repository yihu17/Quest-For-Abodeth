package main.java.questfortheabodeth.weapons;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

/**
 * Models a weapon as an object on the screen that can be interacted with
 */
public class WeaponPickup implements Drawable, Interactable
{
    private int xPos;
    private int yPos;
    private String name;
    private Image image;
    private String filePath;
    private int ammo;

    /**
     * Creates a new interactable weapon object on the screen
     *
     * @param xPos      (int) X position of this weapon
     * @param yPos      (int) Y position of this wepaon
     * @param filePath  (String) image file to load
     * @param name      (String) Name of the weapon
     * @param ammoCount (int) Amount of ammo this weapon has in it
     */
    public WeaponPickup(int xPos, int yPos, String filePath, String name, int ammoCount)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.name = name;
        this.image = new Image(xPos, yPos, filePath);
        this.filePath = filePath;
        this.ammo = ammoCount;
    }

    /**
     * Provide a more useful string representation of the WeaponPickup
     * @return (String) WeaponPickup information
     */
    @Override
    public String toString()
    {
        return "<WeaponPickup " + name + " @ [" + xPos + ", " + yPos + "]>";
    }

    /**
     * Draws the weapon pickup to the screen
     * @param renderTarget (Rendertarget) Where to draw the weapon to
     * @param renderStates (RenderStates) ???
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(image);
    }

    /**
     * Returns the X position of this weapon pickup. Note this is the top
     * left coordinate of the image
     * @return (int) X position
     */
    @Override
    public float getX()
    {
        return xPos;
    }

    /**
     * Returns the Y position of this weapon pickup. Note this is the top
     * left coordinate of the image
     * @return (int) Y position
     */
    @Override
    public float getY()
    {
        return yPos;
    }

    /**
     * Returns the width of this weapon pickup
     * @return (int) Weapon width
     */
    @Override
    public float getWidth()
    {
        return this.image.getGlobalBounds().width;
    }

    /**
     * Returns the height of this weapon pickup
     * @return (int) Weapon height
     */
    @Override
    public float getHeight()
    {
        return this.image.getGlobalBounds().height;
    }

    /**
     * Returns the name of this weapon pickup
     * @return (String) Weapon name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Remove this weapon pickup from the game
     */
    public void remove()
    {
        this.xPos = 2 * Settings.WINDOW_WIDTH;
        this.yPos = 2 * Settings.WINDOW_HEIGHT;
        this.image.setPosition(this.xPos, this.yPos);
    }

    /**
     * Moves this weapon pickup
     * @param x (int) New X position
     * @param y (int) New Y position
     */
    public void move(int x, int y)
    {
        this.xPos = x;
        this.yPos = y;
        this.image.setPosition(xPos, yPos);
    }

    /**
     * Weapons have no lasting interacts with the player other than
     * allowing the player to pass over them so do nothing here
     * @param p (Player) The player object
     */
    @Override
    public void interact(Player p)
    {

    }

    /**
     * Weapons have no lasting interacts with the player other than
     * allowing the player to pass over them so do nothing here
     * @param p (Player) The player object
     */
    @Override
    public void remove(Player p)
    {

    }

    /**
     * Weapons have no lasting interacts with the enemy other than
     * allowing the enemy to pass over them so do nothing here
     * @param e (Enemy) The enemy object
     */
    @Override
    public void buffEnemy(Enemy e)
    {

    }

    /**
     * Weapons have no lasting interacts with the enemy other than
     * allowing the enemy to pass over them so do nothing here
     * @param e (Enemy) The enemy object
     */
    @Override
    public void removeEnemyBuff(Enemy e)
    {

    }

    /**
     * Returns the amount of ammo currently held in this weapon
     * @return (int) Amount of ammo
     */
    public int getAmmo()
    {
        return ammo;
    }
}
