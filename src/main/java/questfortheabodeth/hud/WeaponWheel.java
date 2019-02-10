package main.java.questfortheabodeth.hud;

import main.java.questfortheabodeth.weapons.*;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

public class WeaponWheel implements Drawable {
    private int top;
    private int left;

    private RectangleShape meleeRect;
    private RectangleShape oneHandedRect;
    private RectangleShape twoHandedRect;

    private OneHandedWeapon oneHandedWeapon = null;
    private TwoHandedWeapon twoHandedWeapon = null;
    private Melee meleeWeapon = null;

    public WeaponWheel(Melee meleeWeapon, OneHandedWeapon oneHandedWeapon, TwoHandedWeapon twoHandedWeapon) {
        this();
        this.meleeWeapon = meleeWeapon;
        this.oneHandedWeapon = oneHandedWeapon;
        this.twoHandedWeapon = twoHandedWeapon;
    }

    public WeaponWheel() {
        // Assumes no weapons are present
        meleeRect = new RectangleShape();
        meleeRect.setSize(new Vector2f(64, 64));
        meleeRect.setPosition(new Vector2f(5, 981));
        applyEffects(meleeRect);

        oneHandedRect = new RectangleShape();
        oneHandedRect.setSize(new Vector2f(64, 64));
        oneHandedRect.setPosition(new Vector2f(5, 911));
        applyEffects(oneHandedRect);

        twoHandedRect = new RectangleShape();
        twoHandedRect.setSize(new Vector2f(64, 64));
        twoHandedRect.setPosition(new Vector2f(5, 842));
        applyEffects(twoHandedRect);
    }

    private void applyEffects(RectangleShape shape) {
        shape.setOutlineColor(Color.WHITE);
        shape.setOutlineThickness(2);
        shape.setFillColor(new Color(Color.WHITE, 128));
    }

    public void setWeapon(Weapon weapon) {
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

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        renderTarget.draw(meleeRect);
        renderTarget.draw(oneHandedRect);
        renderTarget.draw(twoHandedRect);
        if (meleeWeapon != null) {
            renderTarget.draw(new WeaponPickup(5, 842, "res/assets/weapons/" + meleeWeapon.getName() + ".png", meleeWeapon.getName()));
        }
        if (oneHandedWeapon != null) {
            renderTarget.draw(new WeaponPickup(5, 911, "res/assets/weapons/" + oneHandedWeapon.getName() + ".png", oneHandedWeapon.getName()));
        }
        if (twoHandedWeapon != null) {
            renderTarget.draw(new WeaponPickup(5, 981, "res/assets/weapons/" + twoHandedWeapon.getName() + ".png", twoHandedWeapon.getName()));
        }
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
