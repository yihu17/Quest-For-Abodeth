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
import org.jsfml.window.event.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The menu that is displayed when the player has won the game
 * The players time is shown on the menu and the score is saved
 * to the list of scores
 */
public class PlayerWinMenu implements Menu
{
    private RenderWindow window;
    private Image background;
    private ArrayList<Clickable> buttons = new ArrayList<>();
    private Button chosenButton = null;
    private boolean menuOpen = true;
    private Text title, time, credits;

    /**
     * Creates a new Player wins menu
     *
     * @param window (RenderWindow) Where to draw the menu to
     * @param time   (String) The playes current time
     */
    public PlayerWinMenu(RenderWindow window, String time)
    {
        this.window = window;
        this.window.setMouseCursorVisible(true);
        Button back = new Button(250, 75, Settings.WINDOW_WIDTH / 2 - 48, 500, "MAIN MENU");
        back.setTextXOffset(
                250 / 2 - (back.getText().length() / 2) * 18
        );
        back.setTextYOffset(17);
        back.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = back;
            }
        });
        buttons.addAll(Arrays.asList(back));

        Text t = new Text("YOU WIN", Settings.MAIN_MENU_FONT, 86);
        t.setColor(Color.BLACK);
        t.setPosition((Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 30, 200);
        title = t;

        Text tb = new Text(time, Settings.MAIN_MENU_FONT, 64);
        tb.setColor(Color.BLACK);
        tb.setPosition((Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 32, 400);
        this.time = tb;

        credits = new Text("A GAME DEVELOPED BY: EDWARD CORKE, ETHAN COTTERELL, CHESTER HUANG & TOM DAFFERN", Settings.ARIAL_FONT, 20);
        credits.setColor(Color.BLACK);
        credits.setPosition(675, 1000);

        this.background = new Image(0, 0, "res/assets/menus/mainmenu.png");

        Highscores scores = new Highscores();
        scores.addScore(time);
        scores.save();
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
            window.draw(this.time);
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
     * Returns the selected button
     *
     * @return (main.java.questfortheabodeth.menus.Button) Clicked button
     */
    @Override
    public Button getChosenButton()
    {
        return chosenButton;
    }

    /**
     * Returns the background image of the menu
     * This menu has no background and so returns null
     *
     * @return (Image) The background image of the menu
     */
    @Override
    public Image getBackground()
    {
        return background;
    }
}
