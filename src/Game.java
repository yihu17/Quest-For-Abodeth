import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.MouseEvent;

import java.util.concurrent.CopyOnWriteArraySet;

public class Game
{
    private RenderWindow window;
    private boolean gameRunning;
    private Room[][] rooms;
    private Room currentRoom;
    private Player player;

    private CopyOnWriteArraySet<Movable> movables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Drawable> drawables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Collidable> collidables = new CopyOnWriteArraySet<>();

    public Game(RenderWindow window)
    {
        Settings.audioStreamer.stop();
        this.window = window;
        this.window.clear();
        this.gameRunning = true;
        this.player = new Player();
        DamagePlus d = new DamagePlus(600, 600);
        drawables.add(d);
        collidables.add(d);


        // Read the CSV file
        rooms = new Room[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rooms[i][j] = new Room(Settings.GENERATOR.nextInt(4));
            }
        }
        currentRoom = rooms[0][0];
    }

    public void run()
    {
        while (gameRunning) {
            window.clear();

            // Draw the room
            window.draw(currentRoom);
            window.draw(player);
            // Draw all the drawable objects
            drawables.forEach(window::draw);
            // Update the window
            window.display();

            // Move every single movable object (enemy movements, bullets etc.)
            // Once they have moved check to enusre they are still in the bounds of the window
            movables.forEach(Movable::move);
            movables.forEach(movable -> {
                if (movable.getX() < -50 || Settings.WINDOW_WIDTH + 50 < movable.getX()) {
                    if (movable.getY() < -50 || Settings.WINDOW_HEIGHT + 50 < movable.getY()) {
                        // Off the screen so remove all instances of it
                        if (movable instanceof Collidable) {
                            collidables.remove(movable);
                        }
                        movables.remove(movable);
                        drawables.remove(movable);
                    }
                }
            });

            // Check all collidables against each other
            collidables.forEach(collidable -> {
                // Check collisions
                // Dont bother checking non-movables against other non movables
            });

            // Check for close events
            for (Event e : window.pollEvents()) {
                Helper.checkCloseEvents(e, window);
                if (e.type == MouseEvent.Type.MOUSE_BUTTON_PRESSED) {
                    Bullet b = new Bullet(
                            (int) player.getPlayerCenter().x,
                            (int) player.getPlayerCenter().y,
                            Helper.getAngleBetweenPoints(new Vector2i(player.getVectorPosition()), e.asMouseEvent().position)
                    );
                    movables.add(b);
                    drawables.add(b);
                    collidables.add(b);
                }
            }

            // Check for user key processes
            if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) {
                openInGameMenu();
            }
            if (Keyboard.isKeyPressed(Keyboard.Key.W)) {
                player.moveUp();
            }
            if (Keyboard.isKeyPressed(Keyboard.Key.A)) {
                player.moveLeft();
            }
            if (Keyboard.isKeyPressed(Keyboard.Key.S)) {
                player.moveDown();
            }
            if (Keyboard.isKeyPressed(Keyboard.Key.D)) {
                player.moveRight();
            }
        }
    }


    private void openInGameMenu()
    {
        GameMenu ingame = new GameMenu(window);
        ingame.displayMenu();
        Button b = ingame.getChosenButton();
        if (b == null) {
            return;
        }
        switch (b.getText().toLowerCase()) {
            case "quit to menu":
                gameRunning = false;
                break;
            case "continue":
                // Continue playing the game
                break;
            default:
                throw new AssertionError("Unknown button was pressed: " + b.getText());
        }
    }
}
