import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public class DamagePlus implements Powerup
{
    private static String damagePlus = "res/damage-plus.png";

    private int damage = 10;
    private Image image;

    public DamagePlus(int x, int y)
    {
        this.image = new Image(x, y, damagePlus);
    }

    @Override
    public void applyBuff(Player p)
    {
        p.setDamage(p.getDamage() + damage);
    }

    @Override
    public void removeBuff(Player p)
    {
        p.setDamage(p.getDamage() - damage);
    }

    @Override
    public Image getImage()
    {
        return this.image;
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(this.image);
    }
}
