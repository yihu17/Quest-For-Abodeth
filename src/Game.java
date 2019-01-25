import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.KeyEvent;

public class Game
{
    private RenderWindow window;
    private boolean gameRunning;
    private Room[][] rooms;
    private Room currentRoom;
    private Player player;

    public Game(RenderWindow window)
    {
        this.window = window;
        this.window.clear();
        this.gameRunning = true;
        this.player = new Player();

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
            window.draw(player);


            window.display();

            for (Event e : window.pollEvents()) {
                Helper.checkCloseEvents(e, window);

                if (e.type == Event.Type.KEY_PRESSED) {
                    KeyEvent ke = e.asKeyEvent();
                    switch(ke.key) {
                        case ESCAPE:
                            openInGameMenu();
                            break;
                        case W:
                            player.moveUp();
                            break;
                        case A:
                            player.moveLeft();
                            break;
                        case S:
                            player.moveDown();
                            break;
                        case D:
                            player.moveRight();
                            break;
                        default:
                            System.err.println("Non-mapped key pressed: " + ke);
                    }
                }
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
