package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Boss;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;

import java.util.ArrayList;
import java.util.Set;

public class BossThread extends Thread {
    ArrayList<Enemy> minions = new ArrayList<>();
    Player player;
    Boss finalBoss;

    public BossThread(Player player, Boss finalBoss) {
        this.player = player;
        this.finalBoss = finalBoss;
    }

    public void run() {
        while (finalBoss.getHealth() > 10) {
            try {
                Thread.sleep(4000);
                String fPath;
                if (Settings.DANK_VERSION) {
                    String[] memes = {"chester", "ethan", "tom"};
                    fPath = "res/assets/enemies/" + memes[Settings.GENERATOR.nextInt(2)] + ".png";
                } else {
                    fPath = "res/assets/enemies/bat.png";
                }
                System.out.println(fPath);
                minions.add(new Enemy((int) finalBoss.getX(), (int) finalBoss.getY(), 40, fPath, 4, "bat", 400, 20));
                minions.get(minions.size() - 1).setPlayer(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Enemy> getMinions() {
        return minions;
    }
}
