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
 * The settings menu. From here the player can toggle various game settings
 * such as game music
 */
public class SettingsMenu implements Menu
{
    private RenderWindow window;
    private ArrayList<Clickable> buttons = new ArrayList<>();
    private Clickable chosenButton;
    private boolean menuOpen = false;
    private Image background;
    private int buttonWidth = 350;
    private int buttonHeight = 50;
    private Text title;

    /**
     * Creates a new settings menu
     *
     * @param window (RenderWindow) Where to draw the menu to
     */
    public SettingsMenu(RenderWindow window)
    {
        this.window = window;
        title = new Text("SETTINGS", Settings.MAIN_MENU_FONT, 56);
        title.setColor(Color.BLACK);
        title.setPosition(830, 50);

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
        //String[] ops = new String[]{"MUSIC", "SOUND EFFECTS", "CROSSHAIR"};

        Button music = new Button(
                buttonWidth,
                buttonHeight,
                (Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 100,
                Settings.WINDOW_Y_PADDING + 0 * (buttonHeight * 2) + 100);
        music.setText("MUSIC |  " + Settings.MUSIC_ON);
        music.setTextYOffset(5);
        music.setTextXOffset(buttonWidth / 2 - (5 / 2) * 14 - 105);
        music.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                if (Settings.MUSIC_ON) {
                    music.setOutlineColor(Color.RED);
                    music.setText("MUSIC | false");
                    Settings.MUSIC_ON = false;
                    Helper.stopAllAudio();
                } else {
                    music.setOutlineColor(Color.GREEN);
                    music.setText("MUSIC | true");
                    Settings.MUSIC_ON = true;
                }
                chosenButton = music;
            }
        });

        if (Settings.MUSIC_ON) {
            music.setOutlineColor(Color.GREEN);
        } else {
            music.setOutlineColor(Color.RED);
        }

        Button soundEffects = new Button(
                buttonWidth,
                buttonHeight,
                (Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 100,
                Settings.WINDOW_Y_PADDING + 1 * (buttonHeight * 2) + 100);
        soundEffects.setText("SOUND EFFECTS |  " + Settings.SOUND_EFFECTS_ON);
        soundEffects.setTextYOffset(5);
        soundEffects.setTextXOffset(buttonWidth / 2 - (5 / 2) * 14 - 105);
        soundEffects.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                if (Settings.SOUND_EFFECTS_ON) {
                    soundEffects.setOutlineColor(Color.RED);
                    soundEffects.setText("SOUND EFECTS | false");
                    Settings.SOUND_EFFECTS_ON = false;
                } else {
                    soundEffects.setOutlineColor(Color.GREEN);
                    soundEffects.setText("SOUND EFFECTS | true");
                    Settings.SOUND_EFFECTS_ON = true;
                }
                chosenButton = soundEffects;
            }
        });

        if (Settings.SOUND_EFFECTS_ON) {
            soundEffects.setOutlineColor(Color.GREEN);
        } else {
            soundEffects.setOutlineColor(Color.RED);
        }

        Button crosshair = new Button(
                buttonWidth,
                buttonHeight,
                (Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 100,
                Settings.WINDOW_Y_PADDING + 2 * (buttonHeight * 2) + 100);
        crosshair.setText("CROSSHAIR |  " + Settings.CROSSHAIR_VISIBLE);
        crosshair.setTextYOffset(5);
        crosshair.setTextXOffset(buttonWidth / 2 - (5 / 2) * 14 - 105);
        crosshair.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
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

        if (Settings.CROSSHAIR_VISIBLE) {
            crosshair.setOutlineColor(Color.GREEN);
        } else {
            crosshair.setOutlineColor(Color.RED);
        }

        Button dank = new Button(
                buttonWidth,
                buttonHeight,
                (Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 100,
                Settings.WINDOW_Y_PADDING + 3 * (buttonHeight * 2) + 100);
        dank.setText("DANK |  " + Settings.DANK_VERSION);
        dank.setTextYOffset(5);
        dank.setTextXOffset(buttonWidth / 2 - (5 / 2) * 14 - 105);
        if (Settings.DANK_VERSION) {
            dank.setOutlineColor(Color.GREEN);
        } else {
            dank.setOutlineColor(Color.RED);
        }
        dank.setOnPress(new EventHandler() {
            @Override
            public void run() {
                if (Settings.DANK_VERSION) {
                    dank.setOutlineColor(Color.RED);
                    dank.setText("DANK | false");
                    Settings.DANK_VERSION = false;
                } else {
                    dank.setOutlineColor(Color.GREEN);
                    dank.setText("DANK | true");
                    Settings.DANK_VERSION = true;
                }
                chosenButton = dank;
            }
        });

        Button animations = new Button(
                buttonWidth,
                buttonHeight,
                (Settings.WINDOW_WIDTH - Settings.WINDOW_X_PADDING * 2) / 2 - 100,
                Settings.WINDOW_Y_PADDING + 4 * (buttonHeight * 2) + 100);
        animations.setText("ANIMATIONS |  " + Settings.ANIMATIONS);
        animations.setTextYOffset(5);
        animations.setTextXOffset(buttonWidth / 2 - (5 / 2) * 14 - 105);
        if (Settings.ANIMATIONS) {
            animations.setOutlineColor(Color.GREEN);
        } else {
            animations.setOutlineColor(Color.RED);
        }
        animations.setOnPress(new EventHandler() {
            @Override
            public void run() {
                if (Settings.ANIMATIONS) {
                    animations.setOutlineColor(Color.RED);
                    animations.setText("ANIMATIONS | false");
                    Settings.ANIMATIONS = false;
                } else {
                    animations.setOutlineColor(Color.GREEN);
                    animations.setText("ANIMATIONS | true");
                    Settings.ANIMATIONS = true;
                }
                chosenButton = animations;
            }
        });


        buttons.add(music);
        buttons.add(soundEffects);
        buttons.add(crosshair);
        buttons.add(dank);
        buttons.add(animations);

        this.background = new Image(0, 0, "res/assets/menus/mainmenu.png");
    }

    /**
     * Returns a list of all the clickable objects on this menu
     * @return (List) List of clickable objects
     */
    @Override
    public List<Clickable> getButtons()
    {
        return this.buttons;
    }

    /**
     * Take control of the game loop and display the menu until
     * a button is pressed
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
     * Returns the {@link Clickable} that was clicked
     * @return (Clickable) The object that was clicked to close the menu
     */
    @Override
    public Clickable getChosenButton()
    {
        return chosenButton;
    }

    /**
     * Returns the background image of this menu
     * @return (Image) The background image of the menu
     */
    @Override
    public Image getBackground()
    {
        return background;
    }
}
