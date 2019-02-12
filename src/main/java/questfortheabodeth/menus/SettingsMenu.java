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

public class SettingsMenu implements Menu {
    private RenderWindow window;
    private ArrayList<Clickable> buttons = new ArrayList<>();
    private Clickable chosenButton;
    private boolean menuOpen = false;
    private Image background;
    private int buttonWidth = 300;
    private int buttonHeight = 50;
    private Text title;

    public SettingsMenu(RenderWindow window) {
        this.window = window;
        Text t = new Text("Settings", Settings.MAIN_MENU_FONT, 56);
        t.setColor(Color.BLACK);
        t.setPosition(300, 50);
        title = t;
        String[] ops = new String[]{"settings", "settings", "settings", "settings", "back"};
        int i = 0;
        for (String s : ops) {
            Button b = new Button(
                    buttonWidth,
                    buttonHeight,
                    (Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 600,
                    Settings.WINDOW_Y_PADDING + i * (buttonHeight * 2) + 100,
                    s
            );
            b.setTextYOffset(5);
            b.setTextXOffset(
                    buttonWidth / 2 - (s.length() / 2) * 14
            );
            b.setOnPress(new EventHandler() {
                @Override
                public void run() {
                    menuOpen = false;
                    chosenButton = b;
                }
            });
            buttons.add(b);
            i++;
        }
        this.background = new Image(0, 0, "res/assets/menus/mainmenu.png");


    }

    @Override
    public List<Clickable> getButtons() {
        return this.buttons;
    }

    @Override
    public void displayMenu() {
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

    @Override
    public Clickable getChosenButton() {
        return chosenButton;
    }


    @Override
    public Image getBackground() {
        return null;
    }
}
