package main.java.questfortheabodeth.hud;

import main.java.questfortheabodeth.weapons.Melee;
import main.java.questfortheabodeth.weapons.OneHandedWeapon;
import main.java.questfortheabodeth.weapons.TwoHandedWeapon;
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

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        renderTarget.draw(meleeRect);
        renderTarget.draw(oneHandedRect);
        renderTarget.draw(twoHandedRect);
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
