package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.characters.Boss;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.Room;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.interfaces.Movable;
import main.java.questfortheabodeth.powerups.Pickup;
import main.java.questfortheabodeth.weapons.WeaponPickup;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderTarget;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Thread that handles all the loading of the room in the background. This
 * prevent the main game from hanging whilst this computationally expensive
 * operation is done in the backgroun
 */
public class RoomLoader extends Thread
{
    private Room[][] rooms;
    private int rows;
    private int cols;
    private int startRow;
    private int startCol;

    private CopyOnWriteArraySet<Movable> movables;
    private CopyOnWriteArraySet<Drawable> drawables;
    private CopyOnWriteArraySet<Collidable> collidables;
    private CopyOnWriteArraySet<Enemy> enemies;
    private CopyOnWriteArraySet<Interactable> interactables;

    private Player player;
    private RenderTarget window;

    /**
     * Creates a new room loader thread
     *
     * @param rooms         (Room[][]) The 2D array to load rooms into
     * @param startRow      (int) The row of the room the player spawns in
     * @param startCol      (int) The column of the room the player spawns in
     * @param movables      (CopyOnWriteArraySet) List of movable to load the rooms movables into
     * @param drawables     (CopyOnWriteArraySet) List of drawables to load the rooms drawable into
     * @param collidables   (CopyOnWriteArraySet) List of collidables to load the rooms collidables into
     * @param enemies       (CopyOnWriteArraySet) List of enemies to load the rooms enemies into
     * @param interactables (CopyOnWriteArraySet) List of interactables to load the rooms interactables into
     * @param player        (Player) The player object
     * @param window        (RenderTarget) Where to draw the elements to
     */
    public RoomLoader(
            Room[][] rooms,
            int startRow,
            int startCol,
            CopyOnWriteArraySet<Movable> movables,
            CopyOnWriteArraySet<Drawable> drawables,
            CopyOnWriteArraySet<Collidable> collidables,
            CopyOnWriteArraySet<Enemy> enemies,
            CopyOnWriteArraySet<Interactable> interactables,
            Player player,
            RenderTarget window
    )
    {
        this.rooms = rooms;
        this.rows = rooms.length;
        this.cols = rooms[0].length;
        this.startRow = startRow;
        this.startCol = startCol;
        this.movables = movables;
        this.drawables = drawables;
        this.collidables = collidables;
        this.enemies = enemies;
        this.interactables = interactables;
        this.player = player;
        this.window = window;
    }

    /**
     * Loop over the room array and regenerate any not null room.
     * This done so that door positions can be calculated.
     * Also does the currentRoom first time setup to speed up execution
     * this thread stops and the game starts
     */
    @Override
    public void run()
    {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (rooms[i][j] == null) {
                    continue;
                }
                rooms[i][j] = new Room(
                        rooms[i][j].getType() * -1,
                        i - 1 >= 0 && rooms[i - 1][j] != null, // up
                        i + 1 < rows && rooms[i + 1][j] != null, // down
                        j - 1 >= 0 && rooms[i][j - 1] != null, // left
                        j + 1 < cols && rooms[i][j + 1] != null  // right
                );
            }
        }

        // Set the current room
        Room currentRoom = rooms[startRow][startCol];

        // Add all the collidables
        collidables.addAll(currentRoom.getCollidables());

        // Add all the weapon pickups
        for (WeaponPickup w : currentRoom.getWeapons()) {
            interactables.add(w);
            drawables.add(w);
        }

        // Add all the enemies
        for (Enemy e : currentRoom.getEnemies()) {
            // If the enemy is a boss it needs these lists to spawn TheAbodeth
            if (e instanceof Boss) {
                ((Boss) e).setLists(drawables, collidables);
            }
            e.setPlayer(player);
            movables.add(e);
            enemies.add(e);
            drawables.add(e);
        }

        // Add all the pickups
        for (Pickup p : currentRoom.getPickups()) {
            collidables.add(p);
            drawables.add(p);
        }

        // Add all the interactables
        collidables.add(player);
        interactables.addAll(currentRoom.getInteractables());

        // Do some preliminary drawing to save time later
        window.draw(currentRoom);
        drawables.forEach(window::draw);

        // Stop the thread
        System.out.println("Interrupting the thread");
        this.interrupt();
    }
}
