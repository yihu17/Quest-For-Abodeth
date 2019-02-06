public class AmmoPickup extends Pickup {
    private int ammo;

    public AmmoPickup(int x, int y) {
        super(x, y, "res/assets/pickups/ammo-pickup.png");
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

    @Override
    public void applyBuff(Player p) {

    }

    @Override
    public void removeBuff(Player p) {

    }
}