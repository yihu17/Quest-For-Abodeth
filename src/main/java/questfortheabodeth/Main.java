package main.java.questfortheabodeth;

import main.java.questfortheabodeth.interfaces.Clickable;
import main.java.questfortheabodeth.menus.ControlsMenu;
import main.java.questfortheabodeth.menus.HighscoreMenu;
import main.java.questfortheabodeth.menus.MainMenu;
import main.java.questfortheabodeth.menus.SettingsMenu;
import main.java.questfortheabodeth.threads.AudioThread;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;

/**
 * The main entry point to the game. The window is created here.
 */
public class Main
{
    private RenderWindow window;

    /**
     * Creates a new instance of The Quest for the Abodeth
     */
    public Main()
    {
        this.window = new RenderWindow();
        this.window.setFramerateLimit(Settings.WINDOW_FPS);
    }

    /**
     * Runs the game
     */
    public void run()
    {
        this.window.create(VideoMode.getDesktopMode(), Settings.WINDOW_TITLE, WindowStyle.FULLSCREEN);
        //for (VideoMode v: VideoMode.getFullscreenModes()) {
        //    System.out.println(v);
        //}
        //this.window.create(new VideoMode(1366, 768, 32), main.java.questfortheabodeth.Settings.WINDOW_TITLE);


        // As long as the window is open run the game loop
        while (window.isOpen()) {
            if (Settings.MUSIC_ON && !Settings.BACKGROUND_AUDIO_PLAYING) {
                Settings.BACKGROUND_AUDIO_PLAYING = true;
                Helper.playAudio("mainMenu");
                new AudioThread("mainMenu");
            }

            // Display the main menu
            MainMenu menu = new MainMenu(window);
            menu.displayMenu();
            Clickable chosenOption = menu.getChosenButton();

            // Added this clause to ensure that if the user exits using an escape method
            // not NullPointerException is thrown when attempting to get the text of a null
            // button
            if (chosenOption == null) {
                continue;
            }

            // A button was clicked in the menu so decide what to do next
            switch (chosenOption.getText().toLowerCase()) {
                case "play":
                    Helper.stopAllAudio();
                    System.out.println("Spawning new game instance");
                    Settings.GAME_TIME = new StopClock();
                    Game game = new Game(window);
                    game.run();
                    break;
                case "settings":
                    System.out.println("Opening settings menu");
                    SettingsMenu settingsMenu = new SettingsMenu(window);
                    settingsMenu.displayMenu();
                    break;
                case "controls":
                    System.out.println("Showing instructions menu");
                    ControlsMenu controlsMenu = new ControlsMenu(window);
                    controlsMenu.displayMenu();
                    break;
                case "highscores":
                    System.out.println("Showing high score menu");
                    HighscoreMenu highscoreMenu = new HighscoreMenu(window);
                    highscoreMenu.displayMenu();
                    break;
                case "credits":
                    System.out.println("Showing credits menu");
                    break;
                case "quit":
                    window.close();
                    System.exit(0);
                    break;
                default:
                    throw new AssertionError("Unknown option returned from the main menu '" + chosenOption.getText() + "'");
            }
        }
    }

    /**
     * Game entry point
     *
     * @param args (String[]) Any command line arguments passed
     */
    public static void main(String[] args)
    {
        Main game = new Main();
        game.run();
    }
}
