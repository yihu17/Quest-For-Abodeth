package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;

public class Water extends InteractableEnvironment {

    public Water (int xPos, int yPos, String imageFilePath) {
        super(xPos, yPos, imageFilePath);
    }

    @Override
    public void interact(Player p) {
        if (p.applyInteract(this)) {
            p.setMovementSpeed(
                    (int) (p.getMovementSpeed() * 0.75)
            );
        }
    }

    @Override
    public void remove(Player p) {
        p.setMovementSpeed(Settings.PLAYER_SPEED);
        System.out.println("Removing speed buff from player");
    }
}
