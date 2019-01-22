import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

public class Game
{
    private RenderWindow window;
    private boolean gameRunning;
    private Room[][] rooms;

    public Game(RenderWindow window)
    {
        this.window = window;
        this.window.clear();
        this.gameRunning = true;

        // Read the CSV file
        rooms = new Room[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rooms[i][j] = new Room(Settings.GENERATOR.nextInt(4));
            }
        }
        Helper.printMatrix(rooms);
    }

    public void run()
    {
        while (gameRunning) {
            window.clear();

            window.display();

            for (Event e : window.pollEvents()) {
                Helper.checkCloseEvents(e, window);

                if (e.type == Event.Type.KEY_PRESSED) {
                    if (e.asKeyEvent().key == Keyboard.Key.ESCAPE) {
                        openInGameMenu();
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
            default:
                throw new AssertionError("Unknown button was pressed: " + b.getText());
        }
    }
}
