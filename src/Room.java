public class Room
{
    private int type;

    public Room(int type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "<Room " + type + ">";
    }
}
