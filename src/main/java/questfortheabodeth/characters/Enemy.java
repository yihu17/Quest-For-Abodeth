package main.java.questfortheabodeth.characters;

import main.java.questfortheabodeth.interfaces.Movable;

public class Enemy extends Character implements Movable
{
    private String type;
    private Player player = null;

    public Enemy(int xPos, int yPos, int health, String imageFilePath, int movementSpeed)
    {
        super(xPos, yPos, health, imageFilePath, movementSpeed);
        this.type = imageFilePath.split("/")[imageFilePath.split("/").length - 1];
        System.out.println(imageFilePath);
    }

    @Override
    public void kill()
    {
        System.out.println(this + " died");
    }

    @Override
    public String toString()
    {
        return "<Enemy " + this.type + " @ [" + getX() + ", " + getY() + "] with " + getHealth() + "hp>";
    }

    public void setPlayer(Player p)
    {
        this.player = p;
    }

    @Override
    public void move()
    {
        if (this.getX() <= player.getX()) {
            this.moveRight();
        } else {
            this.moveLeft();
        }

        if (this.getY() <= player.getY()) {
            this.moveDown();
        } else {
            this.moveUp();
        }
    }
}
