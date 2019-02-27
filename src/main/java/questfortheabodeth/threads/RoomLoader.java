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

        Room currentRoom = rooms[startRow][startCol];

        collidables.addAll(currentRoom.getCollidables());

        for (WeaponPickup w : currentRoom.getWeapons()) {
            interactables.add(w);
            drawables.add(w);
        }

        for (Enemy e : currentRoom.getEnemies()) {
            if (e instanceof Boss) {
                ((Boss) e).setLists(drawables, collidables);
            }
            e.setPlayer(player);
            movables.add(e);
            enemies.add(e);
            drawables.add(e);
        }


        for (Pickup p : currentRoom.getPickups()) {
            collidables.add(p);
            drawables.add(p);
        }

        collidables.add(player);
        interactables.addAll(currentRoom.getInteractables());

        window.draw(currentRoom);
        drawables.forEach(window::draw);

        System.out.println("Interrupting the thread");
        this.interrupt();
    }
}
