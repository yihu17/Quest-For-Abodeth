public class SpeedPickupUp extends Pickup {
    private int timeout;

    public SpeedPickupUp(int x, int y, int timeout) {

        super(x, y, "res/assets/pickups/speed-pickup-up.png");
        this.timeout = timeout;
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