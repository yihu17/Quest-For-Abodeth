package main.java.questfortheabodeth.hud;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class HudElements implements Drawable
{
    private Player player;
    private HealthBar healthbar;
    private MiniMap minimap;
    private WeaponWheel weaponwheel;
    private AmmoCount ammoCount;
    private Image crosshair;

    public HudElements(Player player, HealthBar h, MiniMap m, WeaponWheel w, AmmoCount a)
    {
        this.player = player;
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
        drawCurrentPowerups(renderTarget);

        if (minimap.getVisibility()) {
            renderTarget.draw(minimap);
        }
        if (Settings.CROSSHAIR_VISIBLE) {
            updateCrosshairPosition();
            renderTarget.draw(crosshair);
        }
    }

    public void toggleMiniMapVisibility()
    {
        minimap.toggleVisibility();
    }

    private void updateCrosshairPosition() {
        crosshair.setXPosition((int) MouseInfo.getPointerInfo().getLocation().getX() - Settings.ROOM_DIVISION_SIZE / 2);
        crosshair.setYPosition((int) MouseInfo.getPointerInfo().getLocation().getY() - Settings.ROOM_DIVISION_SIZE / 2);
    }

    public void drawCurrentPowerups(RenderTarget window) {
        ArrayList<Image> powerupImages = new ArrayList<>();
        for (int i = 0; i < player.getCurrentPowerupsString().size(); i++) {
            try {
                powerupImages.add(new Image(Settings.WINDOW_WIDTH - 30, 50 + (10 * i), "res/assets/pickups/" + player.getCurrentPowerupsString().get(i) + ".png"));
            } catch (Exception e) {

            }
            window.draw(powerupImages.get(i));
        }
    }
}
