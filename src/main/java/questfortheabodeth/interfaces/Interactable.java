package main.java.questfortheabodeth.interfaces;

import main.java.questfortheabodeth.characters.Player;

public interface Interactable extends Collidable
{
    void interact(Player p);
}
