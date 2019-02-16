package main.java.questfortheabodeth.environments.traps;

import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.InteractableEnvironment;

public class SpikeTrap extends InteractableEnvironment
{
    private boolean activated = false;

    public SpikeTrap(int xPos, int yPos, String imageFilePath)
    {
        super(xPos, yPos, imageFilePath);
    }

    @Override
    public void interact(Player p)
    {
        if (p.applyInteract(this) && activated == false) {
            p.decreaseHealth(50);
            loadImageFromFile("res/assets/environment/spikeTrap2.png");
            activated = true;
        }
    }

    public void reset()
    {
        activated = false;
        loadImageFromFile("res/assets/environment/spikeTrap.png");
    }

    public boolean isActivated()
    {
        return activated;
    }

    @Override
    public void remove(Player p)
    {

    }

    @Override
    public void buffEnemy(Enemy e)
    {

    }

    @Override
    public void removeEnemyBuff(Enemy e)
    {

    }
}
