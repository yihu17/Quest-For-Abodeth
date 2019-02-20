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

public class SettingsMenu implements Menu
{
    private RenderWindow window;
    private ArrayList<Clickable> buttons = new ArrayList<>();
    private Clickable chosenButton;
    private boolean menuOpen = false;
    private Image background;
    private int buttonWidth = 300;
    private int buttonHeight = 50;
    private Text title;

    public SettingsMenu(RenderWindow window)
    {
        this.window = window;
        Text t = new Text("SETTINGS", Settings.MAIN_MENU_FONT, 56);
        t.setColor(Color.BLACK);
        t.setPosition(300, 50);
        title = t;
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
        String[] ops = new String[]{"MUSIC", "SOUND EFFECTS", "CROSSHAIR"};

        Button crosshair = new Button(
                buttonWidth,
                buttonHeight,
                (Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 600,
                Settings.WINDOW_Y_PADDING + 0 * (buttonHeight * 2) + 100);
        crosshair.setText("CROSSHAIR |  " + Settings.CROSSHAIR_VISIBLE);
        crosshair.setTextYOffset(5);
        crosshair.setTextXOffset(buttonWidth / 2 - (5 / 2) * 14 - 65);
        if (Settings.CROSSHAIR_VISIBLE) {
            crosshair.setOutlineColor(Color.GREEN);
        } else {
            crosshair.setOutlineColor(Color.RED);
        }
        crosshair.setOnPress(new EventHandler() {
            @Override
            public void run() {
                if (Settings.CROSSHAIR_VISIBLE) {
                    crosshair.setOutlineColor(Color.RED);
                    crosshair.setText("CROSSHAIR | false");
                    Settings.CROSSHAIR_VISIBLE = false;
                } else {
                    crosshair.setOutlineColor(Color.GREEN);
                    crosshair.setText("CROSSHAIR | true");
                    Settings.CROSSHAIR_VISIBLE = true;
                }
                chosenButton = crosshair;
            }
        });


        buttons.add(crosshair);


        this.background = new Image(0, 0, "res/assets/menus/mainmenu.png");


    }

    @Override
    public List<Clickable> getButtons()
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
    public Clickable getChosenButton()
    {
        return chosenButton;
    }


    @Override
    public Image getBackground()
    {
        return null;
    }
}
