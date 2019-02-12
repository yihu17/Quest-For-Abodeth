package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.interfaces.Interactable;

public abstract class InteractableEnvironment extends Environment implements Interactable {
    public InteractableEnvironment(int xPos, int yPos, String imageFilePath) {
        super(xPos, yPos, imageFilePath, false, true);
    }


    public abstract void interact(Player p);
}