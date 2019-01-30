public class Bullet extends Image implements Movable
{
    private float x;
    private float y;
    private double xSpeed;
    private double ySpeed;
    private double angle;

    /**
     * Creates a new image object.
     *
     * @param x (int) X coordinate of the top left of the image
     * @param y (int) Y coordinate of the top left of the image
     */
    public Bullet(int x, int y, float angle)
    {
        super(x, y, "bullet");
        this.xSpeed = Settings.BULLET_SPEED / 4.0;
        this.ySpeed = Settings.BULLET_SPEED / 4.0;
        this.angle = angle;
    }

    @Override
    public void move()
    {
        x += xSpeed;
        y += ySpeed;
        this.setPosition(x, y);
    }
}
