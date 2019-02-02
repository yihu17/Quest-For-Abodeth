import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public class Enemy extends Character {
    public Enemy(int xPos, int yPos, int health, String imageFilePath) {
        super(xPos, yPos, health, imageFilePath);
        System.out.println(imageFilePath);
    }

    @Override
    public void kill() {

    }
}
