import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;


public class Main
{
    private RenderWindow window;

    public Main()
    {
        this.window = new RenderWindow();
        this.window.setFramerateLimit(30);
    }

    public void run()
    {
        this.window.create(VideoMode.getDesktopMode(), Settings.WINDOW_TITLE, WindowStyle.FULLSCREEN);
        //this.window.create(new VideoMode(640, 360, 32), Settings.WINDOW_TITLE);

        // As long as the window is open run the game loop
        while (window.isOpen()) {
            // Display the main menu
            MainMenu menu = new MainMenu(window);
            menu.displayMenu();
            Button chosenOption = menu.getChosenButton();
            if (chosenOption == null) {
                continue;
            }
            switch (chosenOption.getText().toLowerCase()) {
                case "play":
                    System.out.println("Spawning new game instance");
                    break;
                case "instructions":
                    System.out.println("Showing instructions menu");
                    break;
                case "highscores":
                    System.out.println("Showing high score menu");
                    break;
                case "credits":
                    System.out.println("Showing credits menu");
                    break;
                case "quit":
                    window.close();
                    break;
                default:
                    throw new AssertionError("Unknown option returned from the main menu '" + chosenOption.getText() + "'");
            }
        }
    }

    public static void main(String[] args)
    {
        Main game = new Main();
        game.run();
    }
}
