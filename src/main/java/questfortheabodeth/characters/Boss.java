package main.java.questfortheabodeth.characters;

import javafx.beans.property.SimpleBooleanProperty;

public class Boss extends Enemy
{
    private SimpleBooleanProperty gameOver = null;

    public Boss(int xPos, int yPos, int health, String imageFilePath, int movementSpeed, String name, int attackSpeed, int attackPower)
    {
        super(xPos, yPos, health, imageFilePath, movementSpeed, name, attackSpeed, attackPower);
    }

    public void setGameOver(SimpleBooleanProperty property)
    {
        this.gameOver = property;
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
        if (gameOver == null) {
            throw new IllegalStateException("Game cannot end");
        }
        gameOver.set(true);
    }
}
