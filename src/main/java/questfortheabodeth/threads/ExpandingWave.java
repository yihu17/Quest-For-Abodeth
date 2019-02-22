package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.hud.MeleeRange;
import org.jsfml.system.Vector2f;

public class ExpandingWave extends Thread
{
    private MeleeRange wave;

    public ExpandingWave(MeleeRange wave)
    {
        this.wave = wave;
    }

    @Override
    public void run()
    {
        while (!isInterrupted()) {
            Vector2f newPosition = new Vector2f(
                    wave.getPlayer().getX() + wave.getPlayer().getWidth() / 2 - wave.getxOffset(),
                    wave.getPlayer().getY() + wave.getPlayer().getHeight() / 2 - wave.getyOffset()
            );


            wave.setRadius(wave.getRadius() + 2);
            wave.setPosition(newPosition);

            try {
                Thread.sleep(7);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (wave.getRadius() > (Settings.ROOM_DIVISION_SIZE * 3 / 2)) {
                this.interrupt();
            }
            wave.xOffsetIncrease();
            wave.yOffsetIncrease();
        }
    }

    @Override
    public void interrupt()
    {
        wave.setVisible(false);
        super.interrupt();
    }
}
