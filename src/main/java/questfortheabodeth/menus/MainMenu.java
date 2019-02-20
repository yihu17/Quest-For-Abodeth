package main.java.questfortheabodeth.menus;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Clickable;
import main.java.questfortheabodeth.interfaces.Menu;
import main.java.questfortheabodeth.sprites.Image;
import main.java.questfortheabodeth.threads.AudioThread;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.window.event.Event;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


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
    private Clip streamer;


    public MainMenu(RenderWindow window)
    {

        this.window = window;
        Text t = new Text("THE QUEST FOR THE ABODETH", Settings.MAIN_MENU_FONT, 82);
        t.setColor(Color.BLACK);
        t.setPosition(510, 280);
        title = t;
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

        if (Settings.AUDIO_ON && !Settings.BACKGROUND_AUDIO_PLAYING) {
            Settings.BACKGROUND_AUDIO_PLAYING = true;
            streamer = Helper.playAudio("mainMenu");
            new AudioThread("mainMenu");
        }


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

    public Clip getStreamer()
    {
        System.out.println("yeeetesty");
        return streamer;
    }
}