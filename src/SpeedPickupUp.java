public class SpeedPickupUp extends Pickup implements Runnable{
    private int timeout;
    private int addSpeed = 50;

    public SpeedPickupUp(int x, int y, int timeout) {
        super(x, y, "res/assets/pickups/speed-pickup-up.png");
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