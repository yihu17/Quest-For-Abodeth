package main.java.questfortheabodeth.hud;

import main.java.questfortheabodeth.weapons.*;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * The Weapon Wheel to show in the Players HUD. It can show one of each weapon type
 * each in their own individual square. The currently selected weapon has a red box
 */
public class WeaponWheel implements Drawable
{

    private RectangleShape meleeRect;
    private RectangleShape oneHandedRect;
    private RectangleShape twoHandedRect;

    private OneHandedWeapon oneHandedWeapon = null;
    private TwoHandedWeapon twoHandedWeapon = null;
    private Melee meleeWeapon = null;

    /**
     * Creates a new WeaponWheel object to add to the HUD
     *
     * @param meleeWeapon     (Melee) The players Melee weapon
     * @param oneHandedWeapon (OneHandedWeapon) The players one handed weapon
     * @param twoHandedWeapon (TwoHandedWeapon) The players two handed weapon
     */
    public WeaponWheel(Melee meleeWeapon, OneHandedWeapon oneHandedWeapon, TwoHandedWeapon twoHandedWeapon)
    {
        this();
        this.meleeWeapon = meleeWeapon;
        this.oneHandedWeapon = oneHandedWeapon;
        this.twoHandedWeapon = twoHandedWeapon;
    }

    /**
     * Creates a new WeaponWheel object to add to the HUD
     */
    public WeaponWheel()
    {
        // Assumes no weapons are present
        meleeRect = new RectangleShape();
        meleeRect.setSize(new Vector2f(64, 64));
        meleeRect.setPosition(new Vector2f(7, 1006));
        applyEffects(meleeRect);

        oneHandedRect = new RectangleShape();
        oneHandedRect.setSize(new Vector2f(64, 64));
        oneHandedRect.setPosition(new Vector2f(7, 936));
        applyEffects(oneHandedRect);

        twoHandedRect = new RectangleShape();
        twoHandedRect.setSize(new Vector2f(64, 64));
        twoHandedRect.setPosition(new Vector2f(7, 867));
        applyEffects(twoHandedRect);
    }

    /**
     * Changes the players current {@link Weapon} in the weapon wheel
     * @param current (Weapon) The players current weapon
     */
    public void selectWeapon(Weapon current)
    {
        meleeRect.setFillColor(new Color(Color.WHITE, 128));
        oneHandedRect.setFillColor(new Color(Color.WHITE, 128));
        twoHandedRect.setFillColor(new Color(Color.WHITE, 128));

        // Fill the correct weapon box red to show its selected
        if (current instanceof Melee) {
            twoHandedRect.setFillColor(new Color(Color.RED, 128));
        }
        if (current instanceof OneHandedWeapon) {
            oneHandedRect.setFillColor(new Color(Color.RED, 128));
        }
        if (current instanceof TwoHandedWeapon) {
            meleeRect.setFillColor(new Color(Color.RED, 128));
        }
    }

    /**
     * Applys the same effects to every passed in rectangle shape
     * @see RectangleShape
     * @param shape (RectangleShape) The shape to apply effects to
     */
    private void applyEffects(RectangleShape shape)
    {
        shape.setOutlineColor(Color.WHITE);
        shape.setOutlineThickness(2);
        shape.setFillColor(new Color(Color.WHITE, 128));
    }

    /**
     * Sets the weapon object of the {@link Weapon} when
     * @param weapon (Weapon) Weapon to set in the player wheel
     */
    public void setWeapon(Weapon weapon)
    {
        if (weapon instanceof Melee) {
            meleeWeapon = (Melee) weapon;
        } else if (weapon instanceof OneHandedWeapon) {
            oneHandedWeapon = (OneHandedWeapon) weapon;
        } else if (weapon instanceof TwoHandedWeapon) {
            twoHandedWeapon = (TwoHandedWeapon) weapon;
        } else {
            throw new RuntimeException("Unsupported weapon class: " + weapon.getClass());
        }
    }

    /**
     * Draws the Weapon wheel to the screen
     * @param renderTarget (RenderTarget) Window to draw the weapon wheel to
     * @param renderStates (RenderStates) ???
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        // Draw all the rectangled
        renderTarget.draw(meleeRect);
        renderTarget.draw(oneHandedRect);
        renderTarget.draw(twoHandedRect);

        // Draw some weapon pickups
        if (meleeWeapon != null) {
            renderTarget.draw(new WeaponPickup(7, 867, "res/assets/weapons/" + meleeWeapon.getName() + ".png", meleeWeapon.getName(), 0));
        }
        if (oneHandedWeapon != null) {
            renderTarget.draw(new WeaponPickup(7, 936, "res/assets/weapons/" + oneHandedWeapon.getName() + ".png", oneHandedWeapon.getName(), 0));
        }
        if (twoHandedWeapon != null) {
            renderTarget.draw(new WeaponPickup(7, 1006, "res/assets/weapons/" + twoHandedWeapon.getName() + ".png", twoHandedWeapon.getName(), 0));
        }
    }
}
