import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.Event;

import java.util.ArrayList;
import java.util.List;

public class GameMenu implements Menu
{
    private RenderWindow window;
    private ArrayList<Clickable> buttons = new ArrayList<>();
    private Button chosenButton = null;
    private boolean menuOpen = true;

    public GameMenu(RenderWindow window)
    {
        this.window = window;
        Button quit = new Button(200, 50, 100, 200, "Quit to menu");
        quit.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = quit;
            }
        });
        buttons.add(quit);
    }

    @Override
    public List<Clickable> getButtons()
    {
        return buttons;
    }

    @Override
    public void displayMenu()
    {
        while (menuOpen) {
            window.clear();
            buttons.forEach(window::draw);
            window.display();

            // Check for events
            for (Event e : window.pollEvents()) {
                Helper.checkButtons(e, buttons);
                Helper.checkCloseEvents(e, window);
            }
        }
    }

    @Override
    public Button getChosenButton()
    {
        return chosenButton;
    }

    @Override
    public Image getBackground()
    {
        return null;
    }
}
