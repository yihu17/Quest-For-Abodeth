import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;
import org.jsfml.window.event.Event;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Main
{
    private RenderWindow window;
    private ArrayList<Button> buttons = new ArrayList<>();
    private Vector2f mousePosition;

    public Main()
    {
        this.window = new RenderWindow();
        this.window.setFramerateLimit(30);
        buttons.add(new Button(100, 50, 500, 500));

        Button b = buttons.get(0);
        b.setText("Hello");
        b.setTextXOffset(14);
        b.setTextYOffset(5);
    }

    public void run()
    {
        this.window.create(VideoMode.getDesktopMode(), Settings.WINDOW_TITLE, WindowStyle.FULLSCREEN);

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

                    buttons.forEach(new Consumer<Button>()
                    {
                        @Override
                        public void accept(Button button)
                        {
                            if (button.getGlobalBounds().contains(mousePosition)) {
                                button.press();
                            }
                        }
                    });
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
