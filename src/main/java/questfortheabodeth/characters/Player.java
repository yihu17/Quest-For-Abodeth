package main.java.questfortheabodeth.characters;

import main.java.questfortheabodeth.interfaces.Powerup;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

/**
 * The main.java.questfortheabodeth.characters
 */
public class Player extends Character
{
    private static String imageName = "res/assets/player/player.png";
    private Powerup currentPowerup = null;

    /**
     * Creates a new main.java.questfortheabodeth.characters instance based off of the imageName image
     */
    public Player()
    {
        super(250, 250, 100, imageName, 6);
    }

    public void switchWeapon()
    {
    }

    /**
     * The main.java.questfortheabodeth.characters has died so open up a died menu
     * that will show the high score etc.
     * <p>
     * Needs to have a button that goes back to the main menu
     * rather than just closing the menu
     */
    @Override
    public void kill()
    {
        System.out.println("I died!");
    }

    /**
     * Draws the main.java.questfortheabodeth.characters to the screen
     * @param renderTarget (RenderTarget) Window to draw the main.java.questfortheabodeth.characters on to
     * @param renderStates (RenderStates) I really should figure out what these are
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(this.getImage());
    }

    @Override
    public String toString()
    {
        return String.format("<Player @ [%.0f, %.0f]", this.getX(), this.getY());
    }

    public Vector2f getPlayerCenter()
    {
        return new Vector2f(
                (float) (this.getX() + (0.5 * this.getWidth())),
                (float) (this.getY() + (0.5 * this.getHeight()))
        );
    }
}
