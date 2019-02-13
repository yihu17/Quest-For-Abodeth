package main.java.questfortheabodeth.environments.Interactables;

import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;
import org.jsfml.graphics.FloatRect;

public class Door extends InteractableEnvironment {
    private int linkedDoor;
    public Door(int xPos, int yPos, String imageFilePath, int linkedDoor) {
        super(xPos, yPos, imageFilePath);
        this.linkedDoor = linkedDoor;
    }


    @Override
    public void interact(Player p) {

    }

    @Override
    public void remove(Player p) {

    }

    @Override
    public void buffEnemy(Enemy e) {

    }

    @Override
    public void removeEnemyBuff(Enemy e) {

    }

    /**
     * Returns a bounds of 3 times the width and 3 times the height of the door so
     * that a player can interact with without having to overlap it
     * @return (FloatRect) Door bounds
     */
    @Override
    public FloatRect getGlobalBounds()
    {
        return new FloatRect(
                getX() - getWidth(),
                getY() - getHeight(),
                3 * getWidth(),
                3 * getHeight()
        );
    }

    public int getLinkedDoor() {
        return linkedDoor;
    }
}
