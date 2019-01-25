import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

/**
 * The player
 */
public class Player extends Character
{
    private static String imageName = "res/zombie.png";

    /**
     * Creates a new player instance based off of the imageName image
     */
    public Player()
    {
        super(200, 200, 100, imageName);
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
     * Draws the player to the screen
     * @param renderTarget (RenderTarget) Window to draw the player on to
     * @param renderStates (RenderStates) I really should figure out what these are
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(this.getImage());
    }
}
