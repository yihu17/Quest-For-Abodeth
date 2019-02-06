public class HealthBoost extends Pickup {

    private int healthBoostage = 50;
    private int timeout;
    private int addSpeed = 50;
    private Player p;

    public HealthBoost(int x, int y, int timeout) {
        super(x, y, "res/assets/pickups/health-boost.png");
        System.out.println("spawning health");
        this.timeout = timeout;
    }

    //function for when picked up/ used:
    public void run() {
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void applyBuff(Player p) {
        System.out.println("Getting buffed");
        this.p = p;
        p.setMovementSpeed(p.getMovementSpeed() + addSpeed);
        this.run();
    }

    @Override
    public void removeBuff(Player p) {
        p.setMovementSpeed(p.getMovementSpeed() - addSpeed);
    }
}