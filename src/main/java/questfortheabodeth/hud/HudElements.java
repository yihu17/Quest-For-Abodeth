package main.java.questfortheabodeth.hud;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public class HudElements implements Drawable
{
    private HealthBar healthbar;
    private MiniMap minimap;
    private WeaponWheel weaponwheel;
    private AmmoCount ammoCount;

    public HudElements(HealthBar h, MiniMap m, WeaponWheel w, AmmoCount a)
    {
        this.healthbar = h;
        this.minimap = m;
        this.weaponwheel = w;
        this.ammoCount = a;
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(healthbar);
        renderTarget.draw(weaponwheel);
        renderTarget.draw(ammoCount);
        if (minimap.getVisibility()) {
            renderTarget.draw(minimap);
        }
    }

    public void toggleMiniMapVisibility()
    {
        minimap.toggleVisibility();
    }
}
