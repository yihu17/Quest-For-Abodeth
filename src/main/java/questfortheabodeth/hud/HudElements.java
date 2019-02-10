package main.java.questfortheabodeth.hud;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public class HudElements implements Drawable {
    private HealthBar healthbar;
    private MiniMap minimap;
    private WeaponWheel weaponwheel;

    public HudElements(HealthBar h, MiniMap m, WeaponWheel w) {
        this.healthbar = h;
        this.minimap = m;
        this.weaponwheel = w;
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        renderTarget.draw(healthbar);
        renderTarget.draw(minimap);
        renderTarget.draw(weaponwheel);
    }
}
