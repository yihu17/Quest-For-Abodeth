package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.hud.MeleeRange;
import org.jsfml.system.Vector2f;

/**
 * An expanding circle centered around the player to simulate
 * a shock wave of using the melee weapon
 */
public class ExpandingWave extends Thread
{
    private MeleeRange wave;

    /**
     * Creates a new expanding wave
     *
     * @param wave (MeleeRange) The circle object to expand
     */
    public ExpandingWave(MeleeRange wave)
    {
        this.wave = wave;
    }

    /**
     * Expand the circle around the player until it hits a certain size
     * and then remove it
     */
    @Override
    public void run()
    {
        // As long as this thread isn't interrupted keep expanding
        while (!isInterrupted()) {
            // Keep the circle centered on the player even when they move
            Vector2f newPosition = new Vector2f(
                    wave.getPlayer().getX() + wave.getPlayer().getWidth() / 2 - wave.getxOffset(),
                    wave.getPlayer().getY() + wave.getPlayer().getHeight() / 2 - wave.getyOffset()
            );

            // Increate the radius and move the circle
            wave.setRadius(wave.getRadius() + 2);
            wave.setPosition(newPosition);

            // Wait for 7 milliseconds
            try {
                Thread.sleep(7);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Has the circle reached its max size?
            if (wave.getRadius() > (Settings.ROOM_DIVISION_SIZE * 3 / 2)) {
                this.interrupt();
            }
            // Increase the circle size
            wave.xOffsetIncrease();
            wave.yOffsetIncrease();
        }
    }

    /**
     * Overides the default interrupt so that the circle can be hidden
     * before the thread is interrupted
     */
    @Override
    public void interrupt()
    {
        wave.setVisible(false);
        super.interrupt();
    }
}
