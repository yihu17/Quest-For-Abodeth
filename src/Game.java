import org.jsfml.graphics.RenderWindow;
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

    private CopyOnWriteArraySet<Powerup> roomPowerups = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Movable> movables = new CopyOnWriteArraySet<>();

    public Game(RenderWindow window)
    {
        this.window = window;
        this.window.clear();
        this.gameRunning = true;
        this.player = new Player();
        this.roomPowerups.add(new DamagePlus(600, 600));

        // Read the CSV file
        rooms = new Room[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rooms[i][j] = new Room(Settings.GENERATOR.nextInt(4));
            }
        }
        currentRoom = rooms[0][0];

        Helper.printMatrix(rooms);
    }

    public void run()
    {
        while (gameRunning) {
            window.clear();

            // Draw the room
            window.draw(currentRoom);
            roomPowerups.forEach(window::draw);
            window.draw(player);

            window.display();

            roomPowerups.forEach(powerup -> {
                if (Helper.checkOverlap(player, powerup)) {
                    roomPowerups.remove(powerup);
                }
            });

            // Check for close events
            for (Event e : window.pollEvents()) {
                Helper.checkCloseEvents(e, window);
                if (e.type == MouseEvent.Type.MOUSE_BUTTON_PRESSED) {
                    System.out.println(player + " || " + e.asMouseEvent().position);
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
