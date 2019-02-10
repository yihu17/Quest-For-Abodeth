package main.java.questfortheabodeth.interfaces;

import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;

public interface Interactable extends Collidable
{
    void interact(Player p);

    void remove(Player p);

    void buffEnemy(Enemy e);

    void removeEnemyBuff(Enemy e);
}
