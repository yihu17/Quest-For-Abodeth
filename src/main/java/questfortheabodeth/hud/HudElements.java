package main.java.questfortheabodeth.hud;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import java.awt.*;
import java.util.ArrayList;

/**
 * Brings together all the relevant HUD elements and allows
 * them to be drawn to the screen as part of one call
 */
public class HudElements implements Drawable
{
    private Player player;
    private HealthBar healthbar;
    private MiniMap minimap;
    private WeaponWheel weaponwheel;
    private AmmoCount ammoCount;
    private Image crosshair;

    /**
     * Creates a new HUD elements object
     *
     * @param player (Player) The player object
     * @param h      (HealthBar) The health bar to draw
     * @param m      (MiniMap) The minimap to draw
     * @param w      (WeaponWheel) The weapon wheel to draw
     * @param a      (AmmoCount) The AmmoCount to draw
     */
    public HudElements(Player player, HealthBar h, MiniMap m, WeaponWheel w, AmmoCount a)
    {
        this.player = player;
        this.healthbar = h;
        this.minimap = m;
        this.weaponwheel = w;
        this.ammoCount = a;
        this.crosshair = new Image(0, 0, "res/assets/hud/crosshair.png");
    }

    /**
     * Draws all the HUD elements to the screen
     * @param renderTarget (RenderTarget) Where to draw the objects
     * @param renderStates (RenderStates) ???
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(healthbar);
        renderTarget.draw(weaponwheel);
        renderTarget.draw(ammoCount.getBackground());
        renderTarget.draw(ammoCount);
        drawCurrentPowerups(renderTarget);

        // Only draw the minimap if its visible
        if (minimap.getVisibility()) {
            renderTarget.draw(minimap);
        }

        // Only draw the crosshairs is their enabled
        if (Settings.CROSSHAIR_VISIBLE) {
            updateCrosshairPosition();
            renderTarget.draw(crosshair);
        }
    }

    /**
     * Toggles the minimaps visibility
     */
    public void toggleMiniMapVisibility()
    {
        minimap.toggleVisibility();
    }

    /**
     * Updates the crosshairs position so it follows the mouse
     */
    private void updateCrosshairPosition() {
        crosshair.setXPosition((int) MouseInfo.getPointerInfo().getLocation().getX() - Settings.ROOM_DIVISION_SIZE / 2);
        crosshair.setYPosition((int) MouseInfo.getPointerInfo().getLocation().getY() - Settings.ROOM_DIVISION_SIZE / 2);
    }

    /**
     * Draws the players current powerups
     * @param window (RenderTarget) Where to draw the current powerups
     */
    public void drawCurrentPowerups(RenderTarget window) {
        ArrayList<Image> powerupImages = new ArrayList<>();
        for (int i = 0; i < player.getCurrentPowerupsString().size(); i++) {
            try {
                powerupImages.add(new Image(15, 775 - (30 * (i + 1)), "res/assets/pickups/" + player.getCurrentPowerupsString().get(i) + ".png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.draw(powerupImages.get(i));
        }
    }
}
