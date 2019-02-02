import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public abstract class Pickup implements Drawable, Powerup {
    private int xPos;
    private int yPos;
    private Image image;

    public Pickup(int xPos, int yPos, String imageFilePath) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.image = new Image(xPos, yPos, imageFilePath);
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        renderTarget.draw(image);
    }

    @Override
    public void removeBuff(Player p) {

    }

    @Override
    public void applyBuff(Player p) {

    }

    @Override
    public Image getImage() {
        return image;
    }
}