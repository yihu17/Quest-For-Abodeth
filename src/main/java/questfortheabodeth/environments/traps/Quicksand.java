package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;

public class Quicksand extends InteractableEnvironment
{
    public Quicksand(int xPos, int yPos, String imageFilePath) {
        super(xPos, yPos, imageFilePath);
    }

    @Override
    public void interact(Player p) {
        if (p.applyInteract(this)) {
            p.setMovementSpeed(
                    p.getMovementSpeed() * 0.75
            );
            System.out.println("Speed is now " + p.getMovementSpeed());
        }
    }

    @Override
    public void remove(Player p) {
        p.setMovementSpeed(p.getMovementSpeed() / 0.75);
        System.out.println("Removing speed buff from player");
        System.out.println("Speed is now " + p.getMovementSpeed());
    }
}