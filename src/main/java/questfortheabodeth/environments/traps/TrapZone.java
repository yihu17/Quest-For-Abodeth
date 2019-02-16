package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.interfaces.Trap;
import org.jsfml.graphics.FloatRect;

public interface TrapZone extends Trap
{

    FloatRect getGlobalBounds();

    void trigger();
}
