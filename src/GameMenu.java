import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An in game menu that pauses the game by taking control
 * of the game loop
 */
public class GameMenu implements Menu
{
    private RenderWindow window;
    private ArrayList<Clickable> buttons = new ArrayList<>();
    private Button chosenButton = null;
    private boolean menuOpen = true;

    /**
     * Creates a new in game menu
     *
     * @param window (RenderWindow) Window to draw the menu on
     */
    public GameMenu(RenderWindow window)
    {
        this.window = window;
        Button quit = new Button(300, 50, 200, 200, "Quit to menu");
        quit.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = quit;
            }
        });
        Button cont = new Button(300, 50, 200, 300, "Continue");
        cont.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = cont;
            }
        });
        buttons.addAll(Arrays.asList(quit, cont));
    }

    /**
     * Returns all the buttons present in the menu
     * @return (List) List of clickables in the menu
     */
    @Override
    public List<Clickable> getButtons()
    {
        return buttons;
    }

    /**
     * Displays the menu on the screen
     */
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

    /**
     * Returns the selected button
     * @return (Button) Clicked button
     */
    @Override
    public Button getChosenButton()
    {
        return chosenButton;
    }

    /**
     * Returns the background image of the menu
     * This menu has no background and so returns null
     * @return (null) No image
     */
    @Override
    public Image getBackground()
    {
        return null;
    }
}
