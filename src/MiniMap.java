public class MiniMap
{
    public enum Directions
    {
        UP, DOWN, LEFT, RIGHT
    }

    private boolean[][] rooms;
    private int[] currentRoom;

    public MiniMap(int rows, int cols)
    {
        this(rows, cols, 0, 0);
    }

    public MiniMap(int rows, int cols, int startX, int startY)
    {
        rooms = new boolean[rows][cols];
        currentRoom = new int[]{startX, startY};

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rooms[i][j] = false;
            }
        }

        visitRoom();
    }

    public void move(Directions d)
    {
        switch (d) {
            case UP:
                currentRoom[1] -= 1;
                break;
            case DOWN:
                currentRoom[1] += 1;
                break;
            case LEFT:
                currentRoom[0] -= 1;
                break;
            case RIGHT:
                currentRoom[0] += 1;
                break;
            default:
                throw new IllegalStateException("How the hell did you get here. you broke Java");
        }
        visitRoom();
    }

    public void visitRoom()
    {
        rooms[currentRoom[0]][currentRoom[1]] = true;
    }

    public void dumpMinimap()
    {
        for (int i = 0; i < rooms.length; i++) {
            boolean[] row = rooms[i];
            for (int j = 0; j < row.length; j++) {
                System.out.print((rooms[j][i] ? 1 : 0) + " ");
            }
            System.out.println("");
        }
    }

    public static void main(String[] args)
    {
        MiniMap m = new MiniMap(4, 4, 1, 0);
        m.dumpMinimap();

        m.move(Directions.DOWN);
        System.out.println("################");
        m.dumpMinimap();

        m.move(Directions.RIGHT);
        System.out.println("################");
        m.dumpMinimap();
    }
}
