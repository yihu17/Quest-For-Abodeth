import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

public class Game
{
    private RenderWindow window;
    private boolean gameRunning;

    public Game(RenderWindow window)
    {
        this.window = window;
        this.window.clear();
        this.gameRunning = true;
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
                        System.out.println("Spawning in game menu");
                        GameMenu ingame = new GameMenu(window);
                        System.out.println("Menu created");
                        ingame.displayMenu();
                        System.out.println("Menu hiding");
                        Button b = ingame.getChosenButton();
                        if (b == null) {
                            continue;
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
            }
        }
    }
}
