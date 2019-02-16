package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.interfaces.Collidable;

public class CollidableEnvironment extends Environment implements Collidable
{
    private String filename;

    public CollidableEnvironment(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath, true, false);
        this.filename = imageFilePath.split("/")[imageFilePath.split("/").length - 1];
    }

    @Override
    public String toString()
    {
        return "<main.java.questfortheabodeth.environments.CollidableEnvironment " + filename + " @ " + getGlobalBounds() + ">";
    }
}
