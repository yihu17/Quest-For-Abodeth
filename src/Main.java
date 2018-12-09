import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;
import org.jsfml.window.event.Event;

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

        while (window.isOpen()) {
            window.clear();

            // Update everything

            // Draw everything

            window.display();

            for (Event event: window.pollEvents()) {
                if (event.type == Event.Type.CLOSED) {
                    window.close();
                } else if (event.type == Event.Type.KEY_PRESSED) {
                    if (event.asKeyEvent().key == Keyboard.Key.ESCAPE) {
                        window.close();
                    }
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
