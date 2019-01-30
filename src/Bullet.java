public class Bullet extends Image implements Movable, Collidable
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
    public Bullet(int x, int y, double angle)
    {
        super(x, y, "res/temp-ico.png");
        this.setScale(Helper.getScaleValue(64, 64, 16, 16));
        this.angle = angle;
        this.xSpeed = Settings.BULLET_SPEED * Math.sin(Math.toRadians(angle));
        this.ySpeed = Settings.BULLET_SPEED * Math.cos(Math.toRadians(angle)) * -1;
        this.x = x;
        this.y = y;
    }

    @Override
    public void move()
    {
        x += xSpeed;
        y += ySpeed;
        this.setPosition(x, y);
    }

    @Override
    public String toString()
    {
        return "<Bullet " + this.hashCode() + " @ [" + x + ", " + y + "] w/ angle " + angle + ">";
    }

    @Override
    public float getX()
    {
        return x;
    }

    @Override
    public float getY()
    {
        return y;
    }

    @Override
    public float getHeight()
    {
        return this.getWidth();
    }

    @Override
    public float getWidth()
    {
        return this.getHeight();
    }
}
