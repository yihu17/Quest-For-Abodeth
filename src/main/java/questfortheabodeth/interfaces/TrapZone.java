package main.java.questfortheabodeth.interfaces;

import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.weapons.Bullet;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;

import java.util.concurrent.CopyOnWriteArraySet;

public interface TrapZone extends Trap
{

    FloatRect getGlobalBounds();

    void trigger(CopyOnWriteArraySet<Movable> movables, CopyOnWriteArraySet<Collidable> collidables, CopyOnWriteArraySet<Drawable> drawables, CopyOnWriteArraySet<Bullet> bullets, Player player);
}
