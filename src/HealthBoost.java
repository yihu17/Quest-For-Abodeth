public class HealthBoost extends Pickup {
    private int healthBoostage;

    public HealthBoost(int x, int y) {
        super(x, y, "res/assets/pickups/health-boost.png");
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