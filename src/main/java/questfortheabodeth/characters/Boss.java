package main.java.questfortheabodeth.characters;

import javafx.beans.property.SimpleBooleanProperty;

public class Boss extends Enemy
{
    private SimpleBooleanProperty gameOver;

    public Boss(int xPos, int yPos, int health, String imageFilePath, int movementSpeed, String name, int attackSpeed, int attackPower, SimpleBooleanProperty gameOver)
    {
        super(xPos, yPos, health, imageFilePath, movementSpeed, name, attackSpeed, attackPower);
        this.gameOver = gameOver;
    }

    @Override
    public void decreaseHealth(int amount)
    {
        super.decreaseHealth(amount);

        // TODO: Play Ed screaming
    }

    @Override
    public void kill()
    {
        // TODO: Do a weird fancy thing to the image
        gameOver.set(true);
    }
}
