package main.java.questfortheabodeth.hud;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

public class MiniMap implements Drawable {

    private boolean visibility = false;
    /**
     * Creates a new MiniMap of the given size
     *
     * @param rows (int) Number of rows
     * @param cols (int) Number of cols
     */
    public MiniMap(int rows, int cols) {
        this(rows, cols, 0, 0);
    }

    private int rows;

    public enum Directions {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private boolean[][] rooms;
    private int[] currentRoom;
    private int cols;

    /**
     * Creates a new MiniMap of the given size
     *
     * @param rows   (int) Number of rows
     * @param cols   (int) Number of cols
     * @param startX (int) Where the character starts in X
     * @param startY (int) Where the character start in Y
     */
    public MiniMap(int rows, int cols, int startX, int startY) {
        this.rows = rows;
        this.cols = cols;
        rooms = new boolean[rows][cols];
        currentRoom = new int[]{startY, startX};

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rooms[i][j] = false;
            }
        }

        // Call the visit room now so the players position in
        // the map is updated
        visitRoom();
        this.dumpMinimap();
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        // For now assume a rectangle size of 20 and top left coordinate of [1760, 1080]
        int x = 1742;
        int y = 5;
        int width = 20;
        int outline = 2;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                RectangleShape rect = new RectangleShape();
                rect.setSize(new Vector2f(width, width));
                rect.setPosition(new Vector2f(x, y));
                if (currentRoom[0] == j && currentRoom[1] == i) {
                    rect.setFillColor(new Color(Color.RED, 128));
                } else {
                    rect.setFillColor(rooms[j][i] ? new Color(Color.WHITE, 128) : new Color(Color.BLACK, 128));
                }
                rect.setOutlineColor(new Color(Color.WHITE, 128));
                rect.setOutlineThickness(outline);
                renderTarget.draw(rect);
                x += (width + outline);
            }
            y += (width + outline);
            x = 1742;
        }
    }

    /**
     * The main.java.questfortheabodeth.characters has moved form one room to the next
     *
     * @param d (Direction) The direction the main.java.questfortheabodeth.characters moved in
     */
    public void move(Directions d) {
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
                throw new IllegalStateException("How the hell did you get here. You broke Java");
        }
        visitRoom();
    }

    /**
     * Updates the main.java.questfortheabodeth.hud.MiniMap with the room the main.java.questfortheabodeth.characters is currently in
     */
    public void visitRoom() {
        rooms[currentRoom[0]][currentRoom[1]] = true;
    }

    /**
     * Dump the minimap to the console
     */
    public void dumpMinimap() {
        for (int i = 0; i < rooms.length; i++) {
            boolean[] row = rooms[i];
            for (int j = 0; j < row.length; j++) {
                System.out.print((rooms[j][i] ? 1 : 0) + " ");
            }
            System.out.println();
        }
    }

    public void toggleVisibility() {
        if (this.visibility) {
            this.visibility = false;
        } else {
            this.visibility = true;
        }
    }

    public boolean getVisibility() {
        return this.visibility;
    }
}
