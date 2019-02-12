package main.java.questfortheabodeth.environments.Interactables;

import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;

public class Door extends InteractableEnvironment {
    public Door(int xPos, int yPos, String imageFilePath) {
        super(xPos, yPos, imageFilePath);
    }

    @Override
    public void interact(Player p) {

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
