package main.java.questfortheabodeth.menus;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Highscores;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Clickable;
import main.java.questfortheabodeth.interfaces.Menu;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * A highscore menu that reads the most recent highscores and
 * displays them on the screen
 */
public class HighscoreMenu implements Menu
{
    private RenderWindow window;
    private Image background;
    private ArrayList<Clickable> buttons = new ArrayList<>();
    private Clickable chosenButton = null;
    private boolean menuOpen = true;
    private Text title;
    private ArrayList<Text> scoreText = new ArrayList<>();

    /**
     * Creates a new highscore menu
     *
     * @param window (RenderWindow) Where to draw the menu to
     */
    public HighscoreMenu(RenderWindow window)
    {
        this.window = window;
        ClickableImage back = new ClickableImage(6, 6, "res/assets/menus/button_back.png", "highscores");
        back.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = back;
            }
        });
        buttons.add(back);

        Text t = new Text("HIGHSCORES", Settings.MAIN_MENU_FONT, 64);
        t.setColor(Color.BLACK);
        t.setPosition((Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 50, 30);
        title = t;

        this.background = new Image(0, 0, "res/assets/menus/mainmenu.png");

        // Create a new highscores object to parse the highscores filee
        Highscores scores = new Highscores();
        int offset = 1;
        int x = (Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2;
        int y = 120;
        for (String s: scores.getScores()) {
            Text ts = new Text(s, Settings.MAIN_MENU_FONT, 64);
            ts.setPosition(new Vector2f(x, y + 60 * offset));
            ts.setColor(Color.BLACK);

            scoreText.add(ts);
            offset++;
        }
    }


    /**
     * Returns all the buttons present in the menu
     *
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
        menuOpen = true;
        while (menuOpen && window.isOpen()) {
            // Clear the window
            window.clear();

            // Draw window objects and background
            window.draw(background);
            window.draw(title);
            buttons.forEach(window::draw);
            scoreText.forEach(window::draw);

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
     * Returns the selected button
     *
     * @return (main.java.questfortheabodeth.menus.Button) Clicked button
     */
    @Override
    public Clickable getChosenButton()
    {
        return chosenButton;
    }

    /**
     * Returns the background image of the menu
     * This menu has no background and so returns null
     *
     * @return (null) No image
     */
    @Override
    public main.java.questfortheabodeth.sprites.Image getBackground()
    {
        return null;
    }
}
