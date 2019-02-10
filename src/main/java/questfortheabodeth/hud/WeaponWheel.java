package main.java.questfortheabodeth.hud;

import main.java.questfortheabodeth.weapons.Melee;
import main.java.questfortheabodeth.weapons.OneHandedWeapon;
import main.java.questfortheabodeth.weapons.TwoHandedWeapon;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public class WeaponWheel implements Drawable {
    private int top;
    private int left;

    private RectangleShape meleeRect;
    private RectangleShape oneHandedRect;
    private RectangleShape twoHandedRect;

    private OneHandedWeapon oneHandedWeapon = null;
    private TwoHandedWeapon twoHandedWeapon = null;
    private Melee meleeWeapon = null;

    public WeaponWheel() {
        // Assumes no weapons are present
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {

    }

    public Melee getMeleeWeapon() {
        return meleeWeapon;
    }

    public void setMeleeWeapon(Melee meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }

    public OneHandedWeapon getOneHandedWeapon() {
        return oneHandedWeapon;
    }

    public void setOneHandedWeapon(OneHandedWeapon oneHandedWeapon) {
        this.oneHandedWeapon = oneHandedWeapon;
    }

    public TwoHandedWeapon getTwoHandedWeapon() {
        return twoHandedWeapon;
    }

    public void setTwoHandedWeapon(TwoHandedWeapon twoHandedWeapon) {
        this.twoHandedWeapon = twoHandedWeapon;
    }
}
