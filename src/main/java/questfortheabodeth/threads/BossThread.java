package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;

import java.util.ArrayList;

public class BossThread extends Thread {
    ArrayList<Enemy> minions = new ArrayList<>();
    Player player;

    public BossThread(Player player) {
        this.player = player;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(4000);
                minions.add(new Enemy(1600, 540, 40, "res/assets/enemies/bat.png", 4, "bat", 400, 20));
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
