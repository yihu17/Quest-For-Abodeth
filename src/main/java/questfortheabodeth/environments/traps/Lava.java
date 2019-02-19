package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;

public class Lava extends InteractableEnvironment{
    private int damageSpeed = 1000;

    public Lava (int xPos, int yPos, String imageFilePath) {
        super(xPos, yPos, imageFilePath);
    }

    @Override
    public void interact(Player p) {
        if (System.currentTimeMillis() - p.getLastTimeHit() >= damageSpeed) {
            p.decreaseHealth(20);
            p.setLastTimeHit(System.currentTimeMillis());
        }
        if(System.currentTimeMillis() - super.getLastAudioTrigger() >= Helper.getLengthOfAudioFile("lava")) {
            Helper.playAudio("lava");
            super.setLastAudioTrigger(System.currentTimeMillis());
        }
    }

    @Override
    public void remove(Player p) {

    }

    @Override
    public void buffEnemy(Enemy e) {

    }

    @Override
    public void removeEnemyBuff(Enemy e) {

    }
}
