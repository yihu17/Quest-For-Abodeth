package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.interfaces.Interactable;

public class InteractableEnvironment extends Environment implements Interactable
{
    public InteractableEnvironment(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath, false);
    }

    @Override
    public void interact(Player p)
    {
        System.out.println("Player is interacting with " + this);
    }
}