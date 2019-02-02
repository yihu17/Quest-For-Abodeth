public class Environment extends Image
{
    private int xPos, yPos;
    private int width = Settings.ROOM_DIVISION_SIZE;
    private int height = Settings.ROOM_DIVISION_SIZE;
    private boolean collidiable;

    public Environment(int xPos, int yPos, String imageFilePath, boolean collidiable) {
        super(xPos, yPos, imageFilePath);
        this.xPos = xPos;
        this.yPos = yPos;
        this.collidiable = collidiable;
    }

    public float getX()
    {
        return this.xPos;
    }

    public float getY()
    {
        return this.yPos;
    }

    public float getWidth()
    {
        return this.getGlobalBounds().width;
    }

    public float getHeight()
    {
        return this.getGlobalBounds().height;
    }

    public boolean isCollidiable() {
        return collidiable;
    }
}