import org.jsfml.graphics.FloatRect;

public class CollidableEnvironment extends Environment implements Collidable
{
    private String filename;
    public CollidableEnvironment(int xPos, int yPos, String imageFilePath) {
        super(xPos, yPos, imageFilePath);
        this.filename = imageFilePath.split("/")[imageFilePath.split("/").length - 1];
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public FloatRect getGlobalBounds() {
        return super.getGlobalBounds();
    }

    public static CollidableEnvironment getInstance() {
        return null;
    }

    @Override
    public String toString()
    {
        return "<CollidableEnvironment " + filename + " @ " + getGlobalBounds() + ">";
    }
}
