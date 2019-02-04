public class HealthBoost extends Pickup implements Runnable {
    private int healthBoostage = 50;

    public HealthBoost(int x, int y) {
        super(x, y, "res/assets/pickups/health-boost.png");
    }

    //function for when picked up/ used:
    public void run() {

    }
}