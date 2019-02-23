package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;

public class Quicksand extends InteractableEnvironment
{
    public Quicksand(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath);
    }

    @Override
    public void interact(Player p)
    {
        if (p.applyInteract(this)) {
            p.setMovementSpeed(p.getMovementSpeed() / 2);
        }
        if (Settings.SOUND_EFFECTS_ON && System.currentTimeMillis() - super.getLastAudioTrigger() >= Helper.getLengthOfAudioFile("quicksand")) {
            Helper.playAudio("quicksand");
            super.setLastAudioTrigger(System.currentTimeMillis());
        }
    }

    @Override
    public void remove(Player p)
    {
        p.setMovementSpeed(p.getMovementSpeed() * 2);
    }

    @Override
    public void buffEnemy(Enemy e)
    {

    }

    @Override
    public void removeEnemyBuff(Enemy e)
    {

    }
}