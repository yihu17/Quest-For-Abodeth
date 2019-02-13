package main.java.questfortheabodeth.characters;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.interfaces.Movable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

public class Enemy extends Character implements Movable {
    private String type;
    private Player player = null;
    private int moveValue = 0;
    private HashSet<Class<? extends Interactable>> appliedInteracts = new HashSet<>();
    private String name;

    public Enemy(int xPos, int yPos, int health, String imageFilePath, int movementSpeed, String name) {
        super(xPos, yPos, health, imageFilePath, movementSpeed);
        this.type = imageFilePath.split("/")[imageFilePath.split("/").length - 1];
        this.name = name;
    }

    @Override
    public void kill() {
        setPosition(2 * Settings.WINDOW_WIDTH, 2 * Settings.WINDOW_HEIGHT);
        System.out.println(this + " died");
    }

    @Override
    public String toString() {
        return "<Enemy (" + this.hashCode() + ") " + this.type + " @ [" + getX() + ", " + getY() + "] with " + getHealth() + "hp and speed " + getMovementSpeed() + ">";
    }

    public void setPlayer(Player p) {
        this.player = p;
    }

    @Override
    public void move() {
        String printout = this + " has moved ";
        if (this.getX() <= player.getX() && Settings.MOVE_RIGHT_SET.contains(moveValue)) {
            this.moveRight();
            printout += "right, ";
        } else if (player.getX() < this.getX() && Settings.MOVE_LEFT_SET.contains(moveValue)) {
            this.moveLeft();
            printout += "left, ";
        }

        if (this.getY() <= player.getY() && Settings.MOVE_DOWN_SET.contains(moveValue)) {
            this.moveDown();
            printout += "down, ";
        } else if (player.getY() < this.getY() && Settings.MOVE_UP_SET.contains(moveValue)) {
            this.moveUp();
            printout += "up, ";
        }

//        System.out.println(printout + " towards " + player + " due to a move value of " + moveValue + "(" + appliedInteracts.size() + " interacts)");
    }

    public void setMoveValue(int moveValue) {
        this.moveValue = moveValue;
    }

    public boolean applyInteract(Interactable interactClass) {
        if (appliedInteracts.contains(interactClass.getClass()) || (!name.equals("crocodile"))) {
            // Do not allow the interact to work
            return false;
        } else {
            appliedInteracts.add(interactClass.getClass());
            return true;
        }
    }

    public void resetInteracts(HashSet<Class<? extends Interactable>> current) {
        appliedInteracts.removeAll(current);

        for (Class<? extends Interactable> c : appliedInteracts) {
            //undo the interact
            try {
                Constructor struct = c.getDeclaredConstructors()[0];
                Interactable i = (Interactable) struct.newInstance(0, 0, "");
                i.removeEnemyBuff(this);
                System.out.println(i + " is removing the buff from enemy");
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        appliedInteracts = current;
    }


}
