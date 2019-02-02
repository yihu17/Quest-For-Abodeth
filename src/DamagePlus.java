public class DamagePlus extends Pickup {
    private int damageDealt = 10;

    public DamagePlus(int x, int y)
    {
        super(x, y, "res/damage-plus.png");
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
    public float getHeight() {
        return 0;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    //function for when picked up/ used:
}
