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
    private ArrayList<Button> buttons = new ArrayList<>();
    private Vector2f mousePosition;

    public Main()
    {
        this.window = new RenderWindow();
        this.window.setFramerateLimit(30);
        buttons.add(new Button(100, 50, 320, 180));

        Button b = buttons.get(0);
        b.setText("Hello");
        b.setTextXOffset(14);
        b.setTextYOffset(5);
        b.setOnPress(new EventHandler() {
            @Override
            public void run()
            {
                System.out.println("Hello World");
            }
        });
    }

    public void run()
    {
        //this.window.create(VideoMode.getDesktopMode(), Settings.WINDOW_TITLE, WindowStyle.FULLSCREEN);
        this.window.create(new VideoMode(640, 360, 32), Settings.WINDOW_TITLE);
        while (window.isOpen()) {
            window.clear();

            // Update everything

            // Draw everything
            buttons.forEach(window::draw);

            window.display();

            for (Event event: window.pollEvents()) {
                if (event.type == Event.Type.CLOSED) {
                    window.close();
                } else if (event.type == Event.Type.KEY_PRESSED) {
                    if (event.asKeyEvent().key == Keyboard.Key.ESCAPE) {
                        window.close();
                    }
                } else if (event.type == Event.Type.MOUSE_BUTTON_PRESSED) {
                    Vector2i pos = event.asMouseEvent().position;
                    mousePosition = new Vector2f((float)pos.x, (float)pos.y);
                    buttons.forEach(button -> button.press(mousePosition));

                } else if (event.type == Event.Type.MOUSE_BUTTON_RELEASED) {
                    Vector2i pos = event.asMouseEvent().position;
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
