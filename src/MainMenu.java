import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.Event;

import java.util.ArrayList;
import java.util.List;

public class MainMenu implements Menu
{
    private ArrayList<Button> buttons = new ArrayList<>();
    private Button chosenButton;
    private boolean menuOpen = false;
    private RenderWindow window;
    private Image background;
    private int buttonWidth = 400;
    private int buttonHeight = 75;

    public MainMenu(RenderWindow window)
    {
        this.window = window;
        String[] ops = new String[]{"Play", "Instructions", "Highscores", "Credits", "Quit"};
        int i = 0;
        for (String s : ops) {
            Button b = new Button(
                    buttonWidth,
                    buttonHeight,
                    (Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 100,
                    Settings.WINDOW_Y_PADDING + i * (buttonHeight * 2),
                    s
            );
            b.setTextYOffset(17);
            System.out.println("Created '" + s + "' button at [" + ((Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 + 150) + ", " + (Settings.WINDOW_Y_PADDING + i * 100 + 100) + "]");
            b.setOnPress(new EventHandler()
            {
                @Override
                public void run()
                {
                    menuOpen = false;
                    chosenButton = b;
                }
            });
            buttons.add(b);
            i++;
        }

        this.background = new Image(0, 0, "res/mainmenu.png");
        background.setScale(Helper.getScaleValue(3840.0f, 2160.0f));
    }

    @Override
    public List<Button> getButtons()
    {
        return this.buttons;
    }

    @Override
    public void displayMenu()
    {
        menuOpen = true;
        while (menuOpen && window.isOpen()) {
            // Clear the window
            window.clear();

            // Draw window objets
            // Draw background
            window.draw(background);
            buttons.forEach(window::draw);

            // Update the display
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
