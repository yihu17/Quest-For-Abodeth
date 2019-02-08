package main.java.questfortheabodeth.characters;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.interfaces.Powerup;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import java.util.HashSet;

/**
 * The main.java.questfortheabodeth.characters
 */
public class Player extends Character
{
    private static String imageName = "res/assets/player/player.png";
    private Powerup currentPowerup = null;
    private HashSet<Class<? extends Interactable>> appliedInteracts = new HashSet<>();


    /**
     * Creates a new main.java.questfortheabodeth.characters instance based off of the imageName image
     */
    public Player()
    {
        super(250, 250, 100, imageName, Settings.PLAYER_SPEED);
    }

    public void switchWeapon()
    {
    }

    public void resetInteracts(HashSet<Class<? extends Interactable>> current) {
        // current is the updated list of current interactions and applied is the old one
        // Current will contain what is actually happening
        // applied is a list of what WAS happening
        // Figure out what was removed and remove that buff

        // current.removeAll(appliedInteracts) = List of all new interacts applied
        System.out.println("Applied interact = " + appliedInteracts.size());
        appliedInteracts.removeAll(current);
        System.out.println("After removing interact = " + appliedInteracts.size());

        for (Class<? extends Interactable> c : appliedInteracts) {
            System.out.println("Removing interact = " + appliedInteracts.size());
            //undo the interact
        }

        appliedInteracts = current;
        System.out.println("Applied interact = " + appliedInteracts.size());
        System.out.println("");
    }

    /**
     * The player has died so open up a died menu
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

    public boolean applyInteract(Interactable interactClass) {
        if (appliedInteracts.contains(interactClass.getClass())) {
            // Do not allow the interact to work
            return false;
        } else {
            appliedInteracts.add(interactClass.getClass());
            return true;
        }
    }
}
