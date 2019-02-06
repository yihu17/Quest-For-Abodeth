public class HealthBoost extends Pickup{
    private int healthBoostage = 50;
    private int timeout;
    private int addSpeed = 50;

    public HealthBoost(int x, int y, int timeout) {
        super(x, y, "res/assets/pickups/health-boost.png");
        this.timeout = timeout;
    }

    private void buff(Player p) {
        p.setMovementSpeed(p.getMovementSpeed() + addSpeed);
    }

    //function for when picked up/ used:
    public void run() {
        try {
            System.out.println("Speed up applied");
            Thread.sleep(timeout);
            System.out.println("Speed up ends");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}