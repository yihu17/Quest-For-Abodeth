package main.java.questfortheabodeth.hud;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import java.awt.*;
import java.util.Set;

public class HudElements implements Drawable
{
    private HealthBar healthbar;
    private MiniMap minimap;
    private WeaponWheel weaponwheel;
    private AmmoCount ammoCount;
    private Image crosshair;

    public HudElements(HealthBar h, MiniMap m, WeaponWheel w, AmmoCount a)
    {
        this.healthbar = h;
        this.minimap = m;
        this.weaponwheel = w;
        this.ammoCount = a;
        this.crosshair = new Image(0, 0, "res/assets/hud/crosshair.png");
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(healthbar);
        renderTarget.draw(weaponwheel);
        renderTarget.draw(ammoCount.getBackground());
        renderTarget.draw(ammoCount);
        if (minimap.getVisibility()) {
            renderTarget.draw(minimap);
        }
        updateCrosshairPosition();
        renderTarget.draw(crosshair);
    }

    public void toggleMiniMapVisibility()
    {
        minimap.toggleVisibility();
    }

    private void updateCrosshairPosition() {
        crosshair.setXPosition((int) MouseInfo.getPointerInfo().getLocation().getX() - Settings.ROOM_DIVISION_SIZE / 2);
        crosshair.setYPosition((int) MouseInfo.getPointerInfo().getLocation().getY() - Settings.ROOM_DIVISION_SIZE / 2);
    }
}
