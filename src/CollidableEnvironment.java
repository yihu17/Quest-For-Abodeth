
public class CollidableEnvironment extends Environment implements Collidable
{
    private String filename;
    public CollidableEnvironment(int xPos, int yPos, String imageFilePath) {
        super(xPos, yPos, imageFilePath);
        this.filename = imageFilePath.split("/")[imageFilePath.split("/").length - 1];
    }

    @Override
    public String toString()
    {
        return "<CollidableEnvironment " + filename + " @ " + getGlobalBounds() + ">";
    }
}
