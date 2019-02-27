package main.java.questfortheabodeth.menus;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Clickable;
import main.java.questfortheabodeth.interfaces.Menu;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.window.event.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * The main menu of the game. This is the starting point and from here
 * other sub-menus can be launched or the game played
 */
public class MainMenu implements Menu
{
    private ArrayList<Clickable> buttons = new ArrayList<>();
    private Clickable chosenButton;
    private boolean menuOpen = false;
    private RenderWindow window;
    private Image background;
    private int buttonWidth = 400;
    private int buttonHeight = 75;
    private Text title;

    /**
     * Creates a new main menu
     *
     * @param window (RenderWindow) Where to draw the menu to
     */
    public MainMenu(RenderWindow window)
    {
        this.window = window;
        this.window.setMouseCursorVisible(true);
        title = new Text("THE QUEST FOR THE ABODETH", Settings.MAIN_MENU_FONT, 82);
        title.setColor(Color.BLACK);
        title.setPosition(510, 280);

        String[] ops = new String[]{"Play", "Quit"};
        int i = 0;
        for (String s : ops) {
            Button b = new Button(
                    buttonWidth,
                    buttonHeight,
                    (Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 100,
                    Settings.WINDOW_Y_PADDING + i * (buttonHeight * 2) + 340,
                    s
            );
            b.setTextYOffset(17);
            b.setTextXOffset(
                    buttonWidth / 2 - (s.length() / 2) * 14
            );
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

        ClickableImage highscores = new ClickableImage(6, 6, "res/assets/menus/button_highscores.png", "highscores");
        highscores.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = highscores;
            }
        });
        buttons.add(highscores);
        ClickableImage settings = new ClickableImage(1850, 6, "res/assets/menus/button_settings.png", "settings");
        settings.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = settings;
            }
        });
        buttons.add(settings);
        ClickableImage controls = new ClickableImage(1850, 1010, "res/assets/menus/button_controls.png", "controls");
        controls.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = controls;
            }
        });
        buttons.add(controls);

        this.background = new Image(0, 0, "res/assets/menus/mainmenu.png");


    }

    /**
     * Returns a list of all the buttons on this menu
     * @see Clickable
     * @return (List) A list of clickable objects
     */
    @Override
    public List<Clickable> getButtons()
    {
        return this.buttons;
    }

    /**
     * Takes over the main game loop to display the menu on the scree
     */
    @Override
    public void displayMenu()
    {
        menuOpen = true;
        while (menuOpen && window.isOpen()) {
            // Clear the window
            window.clear();

            // Draw window objects and background
            window.draw(background);
            window.draw(title);
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

    /**
     * Returns the button that was clicked on the menu
     * @return (Clickable) The object that was clicked to exit the menu
     */
    @Override
    public Clickable getChosenButton()
    {
        return chosenButton;
    }

    /**
     * Returns the menus background so that it can be drawn to the screen
     * @return (Image) Background image of the background
     */
    @Override
    public Image getBackground()
    {
        return null;
    }
}