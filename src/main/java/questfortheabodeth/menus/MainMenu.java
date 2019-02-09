package main.java.questfortheabodeth.menus;

import jdk.nashorn.internal.runtime.ECMAException;
import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.interfaces.Clickable;
import main.java.questfortheabodeth.interfaces.Menu;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.Event;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MainMenu implements Menu
{
    private ArrayList<Clickable> buttons = new ArrayList<>();
    private Clickable chosenButton;
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
                    Settings.WINDOW_Y_PADDING + i * (buttonHeight * 2) + 65,
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

        ClickableImage settings = new ClickableImage(1850, 6, "res/temp-ico.png", "settings");
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

        this.background = new Image(0, 0, "res/menuBackgrounds/mainmenu.png");

        playMusic();

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

    public void playMusic() {
        try {
            if (Settings.AUDIO_STREAMER.isActive()) {
                Settings.AUDIO_STREAMER.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File soundFile = new File("res/assets/audio/mainMenu.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Settings.AUDIO_STREAMER = AudioSystem.getClip();
            Settings.AUDIO_STREAMER.open(audioInputStream);
            Settings.AUDIO_STREAMER.start();
        } catch (Exception e) {
            System.out.println("Audio error");
        }
    }
}
