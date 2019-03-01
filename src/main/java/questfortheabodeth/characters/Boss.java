package main.java.questfortheabodeth.characters;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.hud.HealthBar;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.powerups.TheAbodeth;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The Boss class represents the final boss character of the game
 * When the Boss is killed by the player it runs the normal {@link Enemy#kill()}
 * method of the enemy class, as well as spawning the {@link TheAbodeth} pickup
 * that allows the player to end the game.
 *
 * @see Enemy
 */
public class Boss extends Enemy
{
    private CopyOnWriteArraySet<Drawable> drawables = null;
    private CopyOnWriteArraySet<Collidable> interactables = null;
    private Text healthText = new Text();

    /**
     * Creates a new Boss character
     *
     * @param xPos          (int) Start X position
     * @param yPos          (int) Start Y position
     * @param health        (int) Number of health points
     * @param imageFilePath (String) Which image file to load
     * @param movementSpeed (int) How fast the boss can move
     * @param name          (String) Name of the boss
     * @param attackSpeed   (int) Time between attacks (in milliseconds)
     * @param attackPower   (int) Damage the boss deals when it attacks the player
     */
    public Boss(int xPos, int yPos, int health, String imageFilePath, int movementSpeed, String name, int attackSpeed, int attackPower)
    {
        super(xPos, yPos, health, imageFilePath, movementSpeed, name, attackSpeed, attackPower);
        System.out.println("Created the boss from path " + imageFilePath);
        healthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                healthText.setString(newValue.toString());
            }
        });
        healthText.setColor(Color.WHITE);
        healthText.setFont(Settings.MAIN_MENU_FONT);
        healthText.setString(String.valueOf(health));
    }

    /**
     * Allows the boss to spawn {@link TheAbodeth} onto the screen when it is killed
     *
     * @param d (CopyOnWriteSet) The list of drawables to draw in the screen
     * @param i (CopyOnWriteSet) The list of collidable objects that are checked against the player
     */
    public void setLists(CopyOnWriteArraySet<Drawable> d, CopyOnWriteArraySet<Collidable> i)
    {
        this.drawables = d;
        this.interactables = i;
    }

    /**
     * Decreases this bosses health by the given amount
     *
     * TODO: Play a damage sound fo teh boss "roaring"
     * @param amount (int) Amount to decrease by
     */
    @Override
    public void decreaseHealth(int amount)
    {
        super.decreaseHealth(amount);
    }

    /**
     * "Kills" the boss. Run the super method to remove the boss from the
     * screen and spawn {@link TheAbodeth} where the boss died
     */
    @Override
    public void kill()
    {
        // TODO: Do a weird fancy thing to the image
        TheAbodeth abodeth = new TheAbodeth(
                (float)(getX() + 0.5 * getWidth()),
                (float)(getY() + 0.5 * getHealth()),
                "res/assets/enemies/bat.png"
        );
        drawables.add(abodeth);
        interactables.add(abodeth);

        super.kill();
    }

    /**
     * Draws this characters image to the screen
     *
     * @param renderTarget (RenderTarget) Window to draw the image to
     * @param renderStates (RenderStates) ???
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {

        renderTarget.draw(getImage());
        healthText.setPosition(new Vector2f(getX() + 10, getY() + 145));
        renderTarget.draw(healthText);
    }
}
