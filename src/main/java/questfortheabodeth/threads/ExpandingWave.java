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

            Vector2f size = wave.getSize();
            Vector2f newSize = new Vector2f(size.x + 4 , size.y + 4);

            wave.setSize(newSize);
            wave.setPosition(newPosition);

            try {
                Thread.sleep(7);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (newSize.x > Settings.ROOM_DIVISION_SIZE * 3) {
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
