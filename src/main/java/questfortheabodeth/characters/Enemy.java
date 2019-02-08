package main.java.questfortheabodeth.characters;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Movable;

public class Enemy extends Character implements Movable
{
    private String type;
    private Player player = null;
    private int moveValue = 0;

    public Enemy(int xPos, int yPos, int health, String imageFilePath, int movementSpeed)
    {
        super(xPos, yPos, health, imageFilePath, movementSpeed);
        this.type = imageFilePath.split("/")[imageFilePath.split("/").length - 1];
    }

    @Override
    public void kill()
    {
        setPosition(2 * Settings.WINDOW_WIDTH, 2 * Settings.WINDOW_HEIGHT);
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
        if (this.getX() <= player.getX() && Settings.MOVE_RIGHT_SET.contains(moveValue)) {
            this.moveRight();
        } else if (player.getX() <= this.getX() && Settings.MOVE_LEFT_SET.contains(moveValue)) {
            this.moveLeft();
        }

        if (this.getY() <= player.getY() && Settings.MOVE_DOWN_SET.contains(moveValue)) {
            this.moveDown();
        } else if (player.getY() <= this.getY() && Settings.MOVE_UP_SET.contains(moveValue)) {
            this.moveUp();
        }
    }

    public void setMoveValue(int moveValue)
    {
        this.moveValue = moveValue;
    }


}
