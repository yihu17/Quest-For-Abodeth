package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Interactable;
import org.jsfml.graphics.FloatRect;

public class CollidableInteractableEnvironment extends Environment implements Collidable, Interactable
{
    public CollidableInteractableEnvironment(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath, true);
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public FloatRect getGlobalBounds() {
        return super.getGlobalBounds();
    }

    public static CollidableInteractableEnvironment getInstance() {
        return null;
    }

    @Override
    public void interact()
    {

    }
}