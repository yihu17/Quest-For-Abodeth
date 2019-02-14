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
import java.util.Arrays;
import java.util.List;

public class PlayerDiedMenu implements Menu {
    private RenderWindow window;
    private Image background;
    private ArrayList<Clickable> buttons = new ArrayList<>();
    private Button chosenButton = null;
    private boolean menuOpen = true;
    private Text title, time;

    public PlayerDiedMenu(RenderWindow window, String time) {
        this.window = window;
        Button back = new Button(250, 75, Settings.WINDOW_WIDTH / 2 - 75, 500, "MAIN MENU");
        back.setTextXOffset(
                250 / 2 - (back.getText().length() / 2) * 14
        );
        back.setOnPress(new EventHandler() {
            @Override
            public void run() {
                menuOpen = false;
                chosenButton = back;
            }
        });
        buttons.addAll(Arrays.asList(back));

        Text t = new Text("YOU DIED", Settings.MAIN_MENU_FONT, 64);
        t.setColor(Color.BLACK);
        t.setPosition((Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 50, 200);
        title = t;

        Text tb = new Text(time, Settings.MAIN_MENU_FONT, 64);
        tb.setColor(Color.BLACK);
        tb.setPosition((Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 50, 400);
        this.time = tb;

        this.background = new Image(0, 0, "res/assets/menus/mainmenu.png");
    }


    /**
     * Returns all the buttons present in the menu
     *
     * @return (List) List of clickables in the menu
     */
    @Override
    public List<Clickable> getButtons() {
        return buttons;
    }

    /**
     * Displays the menu on the screen
     */
    @Override
    public void displayMenu() {
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
    public Button getChosenButton() {
        return chosenButton;
    }

    /**
     * Returns the background image of the menu
     * This menu has no background and so returns null
     *
     * @return (null) No image
     */
    @Override
    public main.java.questfortheabodeth.sprites.Image getBackground() {
        return null;
    }
}
