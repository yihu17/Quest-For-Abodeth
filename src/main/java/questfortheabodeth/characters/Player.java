package main.java.questfortheabodeth.characters;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.sprites.Image;
import main.java.questfortheabodeth.weapons.*;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The main Player object.
 */
public class Player extends Character
{
    private RenderTarget window;

    private static String imageName = "res/assets/player/player.png";
    private ArrayList<String> currentPowerups = new ArrayList<>();
    private long lastTimeAttack;
    private long lastTimeHit;
    private HashSet<Class<? extends Interactable>> appliedInteracts = new HashSet<>();

    private Weapon currentWeapon;
    private Melee meleeWeapon = null;
    private OneHandedWeapon oneHandedWeapon = (OneHandedWeapon)Helper.stringToWeapon("revolver");
    private TwoHandedWeapon twoHandedWeapon = null;
    private HashMap<String, SimpleIntegerProperty> ammoCounts = new HashMap<>();

    private SimpleIntegerProperty ammo;

    /**
     * Creates a new player object
     * @param window (RenderTarget) Where to draw the player to
     */
    public Player(RenderTarget window)
    {
        super(250, 250, 100, imageName, Settings.PLAYER_SPEED);
        this.window = window;
        ammoCounts.put("revolver", new SimpleIntegerProperty(25));
        ammo = ammoCounts.get("revolver");
        this.switchWeapon(2);
    }

    /**
     * Returns the current ammo as a read only property so it can be displayed
     * in the players {@link main.java.questfortheabodeth.hud.HudElements}
     *
     * @return (ReadOnlyIntegerProperty) Ammo property
     */
    public ReadOnlyIntegerProperty ammoProperty()
    {
        return ammo;
    }

    /**
     * Switches the players weapons.
     *   - 1. The players {@link Melee} weapon
     *   - 2. The players {@link OneHandedWeapon}
     *   - 3. The players {@link TwoHandedWeapon}
     * If the weapon chosen is not currently being held by the player or the
     * player is already using the chosen weapon, this method returns false.
     * If a successful switch occurred then this method returns true
     *
     * @param weaponNumber (int) Weapon to switch to
     * @return (boolean) Whether or not the weapon was switched to successfully
     */
    public boolean switchWeapon(int weaponNumber)
    {
        switch (weaponNumber) {
            case 1:
                if (meleeWeapon == null) {
                    return false;
                }
                currentWeapon = meleeWeapon;
                setWeaponImage(currentWeapon.getName());
                return true;
            case 2:
                if (oneHandedWeapon == null) {
                    return false;
                }
                currentWeapon = oneHandedWeapon;
                setWeaponImage(currentWeapon.getName());
                ammo = ammoCounts.get(currentWeapon.getName());
                return true;
            case 3:
                if (twoHandedWeapon == null) {
                    return false;
                }
                currentWeapon = twoHandedWeapon;
                setWeaponImage(currentWeapon.getName());
                ammo = ammoCounts.get(currentWeapon.getName());
                return true;
            default:
                return false;
        }
    }

    /**
     * When a player switches weapon the players image needs to be changed
     * to reflect this.
     * @param weapon (String) Name of the currently held weapon
     */
    public void setWeaponImage(String weapon)
    {
        Image img = new Image((int) getX(), (int) getY(), "res/assets/player/player-" + weapon + ".png");

        // New images are loaded in facing right so the image may require flipping
        if (getFacingDirection() == Character.Facing.LEFT) {
            img.flipHorizontal();
        }
        setImage(img);
    }

    /**
     * Resets the players interacts to those calculated from the game loop.
     * The passed in values are the interacts that are still applied
     * @param current (HashSet) Set of interacts to apply
     */
    public void resetInteracts(HashSet<Class<? extends Interactable>> current)
    {
        // Remove all the current interacts from the local list
        // Any remaining need to be removed from the player
        appliedInteracts.removeAll(current);

        for (Class<? extends Interactable> c : appliedInteracts) {
            // Undo the interact
            try {
                Constructor struct = c.getDeclaredConstructors()[0];
                Interactable i = (Interactable) struct.newInstance(0, 0, "");
                i.remove(this);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        appliedInteracts = current;
    }

    /**
     * The player has died. Here because the method is abstract in {@link Character#kill()}.
     * When the player dies a bound boolean property is toggled so that the game
     * loop can exit
     */
    @Override
    public void kill()
    {
        System.out.println("I died!");
    }

    /**
     * Draws the Player to the screen
     *
     * @param renderTarget (RenderTarget) Window to draw the main.java.questfortheabodeth.characters on to
     * @param renderStates (RenderStates) I really should figure out what these are
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(this.getImage());
    }

    /**
     * Provide a more information view of the player object when printed
     * @return (String) Player information
     */
    @Override
    public String toString()
    {
        return String.format("<Player @ [%.0f, %.0f]>", this.getX(), this.getY());
    }

    /**
     * Returns the center of the players image object as opposed to the normal top
     * left coordinate. Used when finding the angle between points
     * @see Helper#getAngleBetweenPoints(Vector2i, Vector2i)
     * @return (Vector2f) Players center
     */
    public Vector2f getPlayerCenter()
    {
        return new Vector2f(
                (float) (this.getX() + (0.5 * this.getWidth())),
                (float) (this.getY() + (0.5 * this.getHeight()))
        );
    }

    /**
     * Applies an interact to the player
     * @param interactClass (Interactable) The interactable to apply
     * @return (boolean) Whether or not the interact was applied
     */
    public boolean applyInteract(Interactable interactClass)
    {
        if (appliedInteracts.contains(interactClass.getClass())) {
            // Do not allow the interact to work
            return false;
        } else {
            appliedInteracts.add(interactClass.getClass());
            return true;
        }
    }

    /**
     * Sets the time in which the player last attacked
     * @param time (long) Time attacked
     */
    public void setLastTimeAttack(long time)
    {
        this.lastTimeAttack = time;
    }

    /**
     * Returns the time at which this player last attacked
     * @return (long) Last attack time
     */
    public long getLastTimeAttack()
    {
        return this.lastTimeAttack;
    }

    /**
     * Sets the last time this player was hit by a projectile/enemy
     * @param time (long) Last time the player was damaged
     */
    public void setLastTimeHit(long time) {
        this.lastTimeHit = time;
    }

    /**
     * Returns the time at which this player was last damaged
     * @return (long) Time the player was damaged
     */
    public long getLastTimeHit() {
        return this.lastTimeHit;
    }

    /**
     * Allows the player to pickup a weapons and ammo.
     * <p>
     * If the player does not already have the weapon then the picked
     * up weapon is added to the players inentory of weapons. The picked
     * up weapon is also set as the players current weapon. When the
     * weapon is picked up the ammo held in the weapon is added to the
     * players inventory as well.
     * </p>
     * <p>
     * If the player already has the weapon in their inventory then the
     * ammo of the weapon is picked up and the weapon is removed from the
     * screen.
     * </p>
     * @param weapon (WeaponPickup) The weapon object that was picked up
     * @return (Weapon) The players current weapon
     */
    public Weapon pickUpWeapon(WeaponPickup weapon)
    {
        if (!hasWeapon(weapon.getName())) {
            Weapon cw = Helper.stringToWeapon(weapon.getName());
            if (cw instanceof Melee) {
                meleeWeapon = (Melee) cw;
                currentWeapon = meleeWeapon;
            } else if (cw instanceof OneHandedWeapon) {
                oneHandedWeapon = (OneHandedWeapon) cw;
                currentWeapon = oneHandedWeapon;
            } else if (cw instanceof TwoHandedWeapon) {
                twoHandedWeapon = (TwoHandedWeapon) cw;
                currentWeapon = twoHandedWeapon;
            }

            if (!ammoCounts.containsKey(weapon.getName())) {
                ammoCounts.put(weapon.getName(), new SimpleIntegerProperty(weapon.getAmmo()));
            }
            ammo = ammoCounts.get(weapon.getName());
            System.out.println("Ammo set to " + weapon.getName());
        }
        switch (Helper.stringToWeapon(weapon.getName()).getName()) {
            case "revolver":
                this.ammoCounts.get("revolver").setValue(ammoCounts.get("revolver").get() + weapon.getAmmo());
                break;
            case "shotgun":
                this.ammoCounts.get("shotgun").setValue(ammoCounts.get("shotgun").get() + weapon.getAmmo());
                break;
            case "ar15":
                this.ammoCounts.get("ar15").setValue(ammoCounts.get("ar15").get() + weapon.getAmmo());
                break;
            case "uzi":
                this.ammoCounts.get("uzi").setValue(ammoCounts.get("uzi").get() + weapon.getAmmo());
                break;
        }
        return currentWeapon;
    }

    /**
     * Checks whether or not the player has the given weapon
     * @param weaponSearching (String) Weapon name to check
     * @return (boolean) True if the player already has the weapon, false otherwise
     */
    public boolean hasWeapon(String weaponSearching)
    {
        if (meleeWeapon != null && meleeWeapon.getName().equals(weaponSearching)) {
            return true;
        }
        if (oneHandedWeapon != null && oneHandedWeapon.getName().equals(weaponSearching)) {
            return true;
        }
        return twoHandedWeapon != null && twoHandedWeapon.getName().equals(weaponSearching);
    }

    /**
     * Returns the ammo of the gun that is specified
     * @throws AssertionError If the requested ammo is not present then something has gone wrong in the game
     * @param name (String) name of the gun
     * @return (ReadOnlyIntegerProperty) Ammo property of the gun
     */
    public ReadOnlyIntegerProperty getAmmoCount(String name)
    {
        if (ammoCounts.containsKey(name)) {
            return ammoCounts.get(name);
        }

        throw new AssertionError("Cannot compute the ammo");
    }

    /**
     * Decreases the amount of ammo the player has for the gun they are currently using
     */
    public void decreaseAmmo()
    {
        this.ammo.set(ammo.get() - 1);
    }

    /**
     * This function is only used in the applyBuff method of the AmmoPickup.
     * Increases the amount of ammo for all ammo pools the player has
     * @param amount (int) Amount to increase ammo by
     */
    public void increaseAmmo(int amount)
    {
        for (String k: ammoCounts.keySet()) {
            String type = Helper.getTypeOfWeapon(k);
            if (type == null || type.equals("Melee")) {
                continue;
            }
            ammoCounts.get(k).set(
                    ammoCounts.get(k).get() + (ammoCounts.get(k).get() / 10)
            );
        }
        this.ammo.set(ammo.get() + amount);
    }

    /**
     * Returns the {@link Weapon} currently being used by the player
     * @return (Weapon) Players current weapon
     */
    public Weapon getCurrentWeapon()
    {
        return currentWeapon;
    }

    /**
     * Returns the players {@link Melee} wepaon
     * @return (Melee) Players melee weapon or null
     */
    public Melee getMeleeWeapon()
    {
        return this.meleeWeapon;
    }

    /**
     * Returns the players {@link OneHandedWeapon}
     * @return (OneHandedWeapon) Players one handed weapon or null
     */
    public OneHandedWeapon getCurrentOneHandedWeapon()
    {
        return this.oneHandedWeapon;
    }

    /**
     * Returns the players {@link TwoHandedWeapon}
     * @return (TwoHandedWeapon) Players two handed weapon or null
     */
    public TwoHandedWeapon getCurrentTwoHandedWeapon()
    {
        return this.twoHandedWeapon;
    }

    /**
     * Damages the player for the amount given. If the games sound effects are
     * eneabled the player damaged sound is played
     * @param amount (int) Damage dealt
     */
    public void hit(int amount) {
        if (Settings.SOUND_EFFECTS_ON) {
            Helper.playAudio("playerHit");
        }
        super.decreaseHealth(amount);
    }

    /**
     * Rerturns the players current ammo as an integer
     * @return (int) Current ammo
     */
    public int getAmmo() {
        return ammo.intValue();
    }

    /**
     * Returns a list of all the powerups the player currently has
     * @return (ArrayList) List of powerup names
     */
    public ArrayList<String> getCurrentPowerupsString() {
        return currentPowerups;
    }

    /**
     * Adds a powerup to the players list of currently applied powerups
     * @param powerup (String) Powerup name
     */
    public void addCurrentPowerup(String powerup) {
        currentPowerups.add(powerup);
    }

    /**
     * Removes a powerup from the players list of current applied powerups
     * @param powerup (String) Powerup name
     */
    public void removeCurrentPowerup(String powerup) {
        for (int i = 0; i < currentPowerups.size(); i++) {
            if (currentPowerups.get(i).equals(powerup)) {
                currentPowerups.remove(i);
                break;
            }
        }
    }
}