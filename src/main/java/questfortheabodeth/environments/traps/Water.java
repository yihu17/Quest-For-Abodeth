package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;

public class Water extends InteractableEnvironment
{
    public Water(int xPos, int yPos, String imageFilePath) {
        super(xPos, yPos, imageFilePath);
    }

    @Override
    public void interact(Player p) {
        if (p.applyInteract(this)) {
            p.setMovementSpeed(p.getMovementSpeed() * 0.75);
            System.out.println("I am on water");
            System.out.println("Speed is now " + p.getMovementSpeed());
        }
    }

    @Override
    public void remove(Player p) {
        p.setMovementSpeed(p.getMovementSpeed() / 0.75);
        System.out.println("Out of the water");
        System.out.println("Speed is now " + p.getMovementSpeed());
    }

    public void buffEnemy(Enemy e) {
        if (e.applyInteract(this)) {
            e.setMovementSpeed(e.getMovementSpeed() * 2);
        }
    }

    public void removeEnemyBuff(Enemy e) {
        e.setMovementSpeed(e.getMovementSpeed() / 2);
    }
}
