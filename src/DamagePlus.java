public class DamagePlus extends Pickup {
    private int damageDealt = 10;

    public DamagePlus(int x, int y)
    {
        super(x, y, "res/assets/pickups/damage-plus.png");
    }
    //function for when picked up/ used:

    @Override
    public void applyBuff(Player p) {

    }

    @Override
    public void removeBuff(Player p) {

    }
}
