package main.java.questfortheabodeth.characters;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.interfaces.Movable;
import org.jsfml.graphics.FloatRect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

/**
 * The Enemy class represents all enemies that can be spawned in the game.
 * The values that are passed in dictate how this enemy will behave/look.
 */
public class Enemy extends Character implements Movable
{
    private String type;
    private Player player = null;
    private int moveValue = 0;
    private HashSet<Class<? extends Interactable>> appliedInteracts = new HashSet<>();
    private String name;
    private int attackSpeed;
    private int attackPower;
    private long lastTimeHit;

    /**
     * Creates a new enemy
     *
     * @param xPos          (int) Start X coordinate
     * @param yPos          (int) Start Y coordinate
     * @param health        (int) Health points
     * @param imageFilePath (String) Image to load
     * @param movementSpeed (int) How fast can this enemy move
     * @param name          (String) Name of the enemy
     * @param attackSpeed   (int) How often can this enemy attack
     * @param attackPower   (int) How much damage does this enemy deal
     */
    public Enemy(int xPos, int yPos, int health, String imageFilePath, int movementSpeed, String name, int attackSpeed, int attackPower)
    {
        super(xPos, yPos, health, imageFilePath, movementSpeed);
        this.type = imageFilePath.split("/")[imageFilePath.split("/").length - 1];
        this.name = name;
        this.attackSpeed = attackSpeed;
        this.attackPower = attackPower;
    }

    /**
     * Removes the enemy from the screen to simulate it dying.
     * When a {@link Character} object is placed out of bounds it
     * is cleaned up in the main game loop
     */
    @Override
    public void kill()
    {
        setPosition(2 * Settings.WINDOW_WIDTH, 2 * Settings.WINDOW_HEIGHT);
        System.out.println(this + " died");
    }

    /**
     * Provide a more helpful view of this Enemy object
     * @return (String) Enemy information
     */
    @Override
    public String toString()
    {
        return "<Enemy (" + this.hashCode() + ") " + this.type + " @ [" + getX() + ", " + getY() + "] with " + getHealth() + "hp and speed " + getMovementSpeed() + ">";
    }

    /**
     * Give this enemy a reference to the player so that it can always
     * attempt to move in the correct direction (towards to the player)
     *
     * @see Player
     *
     * @param p (Player) The main player object
     */
    public void setPlayer(Player p)
    {
        this.player = p;
    }

    /**
     * Move this enemy closer to the player. This function works by first calculating
     * whether or not the player is to the left of the right of this enemy and move the
     * enemy in the relevant direction. Then the player is checked if they are above or
     * below the enemy and the enemy moves in the correspoding direction
     * @see Character#moveLeft()
     * @see Character#moveRight()
     * @see Character#moveUp()
     * @see Character#moveDown()
     */
    @Override
    public void move()
    {
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

    }

    /**
     * Dictates which ways the character can move in due to obstacles being in the
     * way of the current path
     * @see main.java.questfortheabodeth.Helper#checkOverlap(Collidable, Collidable)
     * @see main.java.questfortheabodeth.Helper#checkOverlap(Collidable, FloatRect)
     * @see main.java.questfortheabodeth.Helper#checkOverlap(FloatRect, FloatRect)
     *
     * @param moveValue (int) Move value
     */
    public void setMoveValue(int moveValue)
    {
        this.moveValue = moveValue;
    }

    /**
     * Applies an specific buff to this enemy. Interacts occur when an enemy has moved
     * over some part of the environment. A boolean value is returned to prevent one
     * interact buff being applied multiple times.
     * @param interactClass (Interactable) The interactable to apply to the enemy
     * @return (boolean) Whether or not the interact was applied.
     */
    public boolean applyInteract(Interactable interactClass)
    {
        if (appliedInteracts.contains(interactClass.getClass()) || (!name.equals("crocodile"))) {
            // Do not allow the interact to work
            return false;
        } else {
            appliedInteracts.add(interactClass.getClass());
            return true;
        }
    }

    /**
     * Resets all the current interacts to a new set of interacts that has been
     * calculated from the main game loop
     * @param current (HashSet) Set of classes that should be being applied
     */
    public void resetInteracts(HashSet<Class<? extends Interactable>> current)
    {
        appliedInteracts.removeAll(current);

        for (Class<? extends Interactable> c : appliedInteracts) {
            //undo the interact
            try {
                Constructor struct = c.getDeclaredConstructors()[0];
                Interactable i = (Interactable) struct.newInstance(0, 0, "");
                i.removeEnemyBuff(this);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        appliedInteracts = current;
    }

    /**
     * Returns this enemies name
     * @return (String) Enemy name
     */
    public String getEnemyName()
    {
        return name;
    }

    /**
     * Returns this enemies attack speed
     * @return (int) Enemy attack speed
     */
    public int getAttackSpeed()
    {
        return this.attackSpeed;
    }

    /**
     * Returns this enemies attack power
     * @return (int) Enemy attack power
     */
    public int getAttackPower()
    {
        return this.attackPower;
    }

    /**
     * Sets the time the enemy last attacked the player. Used to prevent the enemy
     * attacking the palyer as fast as the game can run
     * @param time (long) Last attack time
     */
    public void setLastTimeAttack(long time)
    {
        this.lastTimeHit = time;
    }

    /**
     * Returns the time that this enemy last attacked the player
     * @return (long) Last attack time
     */
    public long getLastTimeAttack()
    {
        return this.lastTimeHit;
    }
}
