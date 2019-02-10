package main.java.questfortheabodeth.characters;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.interfaces.Powerup;
import main.java.questfortheabodeth.sprites.Image;
import main.java.questfortheabodeth.weapons.*;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The main.java.questfortheabodeth.characters
 */
public class Player extends Character
{
    private static String imageName = "res/assets/player/player.png";
    private Powerup currentPowerup = null;
    private long lastTimeHit;
    private HashSet<Class<? extends Interactable>> appliedInteracts = new HashSet<>();

    private Weapon currentWeapon;
    private Melee meleeWeapon;
    private OneHandedWeapon oneHandedWeapon;
    private TwoHandedWeapon twoHandedWeapon;

    private int ammo;


    /**
     * Creates a new Player instance based off of the imageName image
     */
    public Player() {
        super(250, 250, 100, imageName, Settings.PLAYER_SPEED);
    }

    public void switchWeapon() {
        //try catch for position in weaponsHolding
    }

    public void resetInteracts(HashSet<Class<? extends Interactable>> current) {
        appliedInteracts.removeAll(current);

        for (Class<? extends Interactable> c : appliedInteracts) {
            //undo the interact
            try {
                Constructor struct = c.getDeclaredConstructors()[0];
                Interactable i = (Interactable) struct.newInstance(0, 0, "");
                i.remove(this);
                System.out.println(i + " is removing the buff");
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        appliedInteracts = current;
    }

    /**
     * The player has died so open up a died menu
     * that will show the high score etc.
     * <p>
     * Needs to have a button that goes back to the main menu
     * rather than just closing the menu
     */
    @Override
    public void kill() {
        //System.out.println("I died!");
    }

    /**
     * Draws the Player to the screen
     * @param renderTarget (RenderTarget) Window to draw the main.java.questfortheabodeth.characters on to
     * @param renderStates (RenderStates) I really should figure out what these are
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        renderTarget.draw(this.getImage());
    }

    @Override
    public String toString() {
        return String.format("<Player @ [%.0f, %.0f]", this.getX(), this.getY());
    }

    public Vector2f getPlayerCenter() {
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

    public void setLastTimeHit(long time) {
        this.lastTimeHit = time;
    }

    public long getLastTimeHit() {
        return this.lastTimeHit;
    }

    public void pickUpWeapon(WeaponPickup weapon) {
        switch (weapon.getName()) {
            case "machete":
                if (!hasWeapon("machete")) {
                    meleeWeapon = new Melee("machete", 3);
                }
                break;
            case "revolver":
                if (!hasWeapon("revolver")) {
                    oneHandedWeapon = new OneHandedWeapon("revolver", 1, 50, 3);
                } else {
                    this.ammo += 50;
                }
                break;
            case "shotgun":
                if (!hasWeapon("shotgun")) {
                    twoHandedWeapon = new TwoHandedWeapon("shotgun", 5, 20, 6);
                } else {
                    this.ammo += 20;
                }
                break;
            case "ar15":
                if (!hasWeapon("ar15")) {
                    twoHandedWeapon = new TwoHandedWeapon("ar15", 1, 25, 2);
                } else {
                    this.ammo += 25;
                }
                break;
            case "uzi":
                if (!hasWeapon("uzi")) {
                    oneHandedWeapon = new OneHandedWeapon("uzi", 3, 60, 1);
                } else {
                    this.ammo += 60;
                }
                break;
        }
    }

    public boolean hasWeapon(String weaponSearching) {
        if (meleeWeapon != null && meleeWeapon.getName().equals(weaponSearching)) {
            return true;
        }
        if (oneHandedWeapon != null && oneHandedWeapon.getName().equals(weaponSearching)) {
            return true;
        }
        if (twoHandedWeapon != null && twoHandedWeapon.getName().equals(weaponSearching)) {
            return true;
        }
        return false;
    }

    public int amountOfWeaponsCarrying() {
        int carry = 0;
        carry += (meleeWeapon == null) ? 0 : 1;
        carry += (oneHandedWeapon == null) ? 0 : 1;
        carry += (twoHandedWeapon == null) ? 0 : 1;

        return carry;
    }
}