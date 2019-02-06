public class Enemy extends Character
{
    private String type;

    public Enemy(int xPos, int yPos, int health, String imageFilePath, int movementSpeed)
    {
        super(xPos, yPos, health, imageFilePath, movementSpeed);
        this.type = imageFilePath.split("/")[imageFilePath.split("/").length - 1];
        System.out.println(imageFilePath);
    }

    @Override
    public void kill()
    {
        System.out.println(this + " died");
    }

    @Override
    public String toString()
    {
        return "<Enemy " + this.type + " @ [" + getX() + ", " + getY() + "] with " + getHealth() + "hp>";
    }
}
