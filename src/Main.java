import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.window.VideoMode;


public class Main
{
    private RenderWindow window;
    private Button playButton;
    private Button instructionsButton;
    private Button quitButton;
    private Vector2f mousePosition;

    public Main()
    {
        this.window = new RenderWindow();
        this.window.setFramerateLimit(30);

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

        quitButton = new Button(200, 50, 200, 250);
        quitButton.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                window.close();
                System.exit(0);
            }
        });
        quitButton.setText("Quit");
    }

    public void run()
    {
        //this.window.create(VideoMode.getDesktopMode(), Settings.WINDOW_TITLE, WindowStyle.FULLSCREEN);
        this.window.create(new VideoMode(640, 360, 32), Settings.WINDOW_TITLE);


        // As long as the window is open run the game loop
        while (window.isOpen()) {
            window.clear(Color.BLACK);
            window.display();

            // Display the main menu
            MainMenu menu = new MainMenu(window);
            menu.displayMenu();
        }
    }

    public static void main(String[] args)
    {
        Main game = new Main();
        game.run();
    }
}
