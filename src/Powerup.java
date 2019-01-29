import org.jsfml.graphics.Drawable;

public interface Powerup extends Drawable
{
    void applyBuff(Player p);

    void removeBuff(Player p);

    Image getImage();
}
