public class SpeedUp extends Pickup implements Runnable {
    private int addSpeed = 50;
    private int timer = 15000;

    public SpeedUp(int x, int y) {
        super(x, y, "res/assets/enemies/spider.png");
    }

    public void apply() {
        Thread thread = new Thread();
    }

    //function for when picked up/ used:
    //thread to start the timer
    public void run() {
        try {
            System.out.println("Speed up applied");
            Thread.sleep(timer);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}