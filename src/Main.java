import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;


public class Main
{
    private RenderWindow window;
    private MainMenu mainMenu;
    private Button playButton;
    private Button instructionsButton;
    private Button quitButton;

    public Main()
    {
        this.window = new RenderWindow();
        this.window.setFramerateLimit(30);
        mainMenu = new MainMenu(this.window, 3);

        playButton = new Button(200, 50, 200, 50);
        playButton.setText("Play");
        playButton.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                System.out.println("Spawning Game instance");
            }
        });

        instructionsButton = new Button(200, 50, 200, 150);
        instructionsButton.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                System.out.println("Spawning instructions menu");
            }
        });
        instructionsButton.setText("Instructions");

        mainMenu.setOption(0, playButton);
        mainMenu.setOption(1, instructionsButton);
        mainMenu.setOption(2, Helpers.getQuitButton(200, 50, 200, 250, window));
    }

    public void run()
    {
        //this.window.create(VideoMode.getDesktopMode(), Settings.WINDOW_TITLE, WindowStyle.FULLSCREEN);
        this.window.create(new VideoMode(640, 360, 32), Settings.WINDOW_TITLE);
        while (window.isOpen()) {
            window.clear(Color.BLACK);
            // Create a new main menu
            mainMenu.display();

            String opText = mainMenu.getSelectedOption().getText();
            switch(opText.toLowerCase()) {
                case "play":
                    System.out.println("Spawning new game instance");
                    break;
                case "instructions":
                    System.out.println("Printing instructions to screen");
                    break;
                case "quit":
                    this.window.close();
                    break;
                default:
                    throw new AssertionError("Unreachable code");
            }
        }
    }

    public static void main(String[] args)
    {
        Main game = new Main();
        game.run();
    }
}
