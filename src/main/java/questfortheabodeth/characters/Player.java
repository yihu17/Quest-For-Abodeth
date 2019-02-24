package main.java.questfortheabodeth.characters;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.interfaces.Powerup;
import main.java.questfortheabodeth.sprites.Image;
import main.java.questfortheabodeth.weapons.*;
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
    private RenderTarget window;

    private static String imageName = "res/assets/player/player.png";
    private ArrayList<String> currentPowerups = new ArrayList<>();
    private long lastTimeAttack;
    private long lastTimeHit;
    private HashSet<Class<? extends Interactable>> appliedInteracts = new HashSet<>();
    private Weapon currentWeapon;
    private Melee meleeWeapon = null;
    private OneHandedWeapon oneHandedWeapon = null;
    private TwoHandedWeapon twoHandedWeapon = null;

    private SimpleIntegerProperty ammo = new SimpleIntegerProperty(25);


    /**
     * Creates a new Player instance based off of the imageName image
     */
    public Player(RenderTarget window)
    {
        super(250, 250, 100, imageName, Settings.PLAYER_SPEED);
        this.window = window;
    }

    public ReadOnlyIntegerProperty ammoProperty()
    {
        return ammo;
    }

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
                return true;
            case 3:
                if (twoHandedWeapon == null) {
                    return false;
                }
                currentWeapon = twoHandedWeapon;
                setWeaponImage(currentWeapon.getName());
                return true;
            default:
                return false;
        }
    }

    public void setWeaponImage(String weapon)
    {
        Image img = new Image((int) getX(), (int) getY(), "res/assets/player/player-" + weapon + ".png");
        if (getFacingDirection() == Character.Facing.LEFT) {
            img.flipHorizontal();
        }
        setImage(img);
    }

    public void resetInteracts(HashSet<Class<? extends Interactable>> current)
    {
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

    @Override
    public String toString()
    {
        return String.format("<Player @ [%.0f, %.0f]>", this.getX(), this.getY());
    }

    public Vector2f getPlayerCenter()
    {
        return new Vector2f(
                (float) (this.getX() + (0.5 * this.getWidth())),
                (float) (this.getY() + (0.5 * this.getHeight()))
        );
    }

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

    public void setLastTimeAttack(long time)
    {
        this.lastTimeAttack = time;
    }

    public long getLastTimeAttack()
    {
        return this.lastTimeAttack;
    }

    public void setLastTimeHit(long time) {
        this.lastTimeHit = time;
    }

    public long getLastTimeHit() {
        return this.lastTimeHit;
    }

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
        }
        if ((Helper.stringToWeapon(weapon.getName()).getName().equals("revolver"))) {
            this.ammo.setValue(ammo.get() + 20);
        }
        if ((Helper.stringToWeapon(weapon.getName()).getName().equals("shotgun"))) {
            this.ammo.setValue(ammo.get() + 40);
        }
        if ((Helper.stringToWeapon(weapon.getName()).getName().equals("ar15"))) {
            this.ammo.setValue(ammo.get() + 15);
        }
        if ((Helper.stringToWeapon(weapon.getName()).getName().equals("uzi"))) {
            this.ammo.setValue(ammo.get() + 45);
        }
        return currentWeapon;
    }

    public boolean hasWeapon(String weaponSearching)
    {
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

    public int amountOfWeaponsCarrying()
    {
        int carry = 0;
        carry += (meleeWeapon == null) ? 0 : 1;
        carry += (oneHandedWeapon == null) ? 0 : 1;
        carry += (twoHandedWeapon == null) ? 0 : 1;

        return carry;
    }

    public void decreaseAmmo()
    {
        this.ammo.set(ammo.get() - 1);
    }

    public void increaseAmmo(int amount)
    {
        this.ammo.set(ammo.get() + amount);
    }

    public Weapon getCurrentWeapon()
    {
        return currentWeapon;
    }

    public Gun getCurrentOneHandedWeapon()
    {
        return this.oneHandedWeapon;
    }

    public Gun getCurrentTwoHandedWeapon()
    {
        return this.twoHandedWeapon;
    }

    public void hit(int amount) {
        if (Settings.SOUND_EFFECTS_ON) {
            Helper.playAudio("playerHit");
        }
        super.decreaseHealth(amount);
    }

    public int getAmmo() {
        return ammo.intValue();
    }

    public ArrayList<String> getCurrentPowerupsString() {
        return currentPowerups;
    }

    public void addCurrentPowerup(String powerup) {
        currentPowerups.add(powerup);
    }

    public void removeCurrentPowerup(String powerup) {
        for (int i = 0; i < currentPowerups.size(); i++) {
            if (currentPowerups.get(i) == powerup) {
                currentPowerups.remove(i);
                break;
            }
        }
    }
}