public class HealthBoost extends Pickup {
    private int healthBoostage = 50;

    public HealthBoost(int x, int y) {
        super(x, y, "res/assets/pickups/health-boost.png");
    }

    //function for when picked up/ used:
    public void run() {

    }

    @Override
    public void applyBuff(Player p) {

    }

    @Override
    public void removeBuff(Player p) {

    }
}