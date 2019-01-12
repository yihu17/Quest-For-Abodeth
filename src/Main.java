import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import java.util.ArrayList;


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
        // Create a list so we can easily iterate over the buttons
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(playButton);
        buttons.add(instructionsButton);
        buttons.add(quitButton);

        // As long as the window is open run the game loop
        while (window.isOpen()) {
            window.clear(Color.BLACK);

            // Draw all the buttons
            buttons.forEach(window::draw);

            // Update the window
            window.display();

            // Poll for events
            for (Event e: window.pollEvents()) {
                if (e.type == Event.Type.CLOSED) {
                    window.close();
                } else if (e.type == Event.Type.KEY_PRESSED) {
                    if (e.asKeyEvent().key == Keyboard.Key.ESCAPE) {
                        window.close();
                    }
                }

                if (e.type == Event.Type.MOUSE_BUTTON_PRESSED) {
                    Vector2i pos = e.asMouseEvent().position;
                    mousePosition = new Vector2f((float)pos.x, (float)pos.y);
                    buttons.forEach(button -> button.press(mousePosition));
                } else if (e.type == Event.Type.MOUSE_BUTTON_RELEASED) {
                    Vector2i pos = e.asMouseEvent().position;
                    mousePosition = new Vector2f((float) pos.x, (float) pos.y);
                    buttons.forEach(button -> button.release(mousePosition));
                }
            }
        }
    }

    public static void main(String[] args)
    {
        Main game = new Main();
        game.run();
    }
}
