package main.java.questfortheabodeth;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Texture;
import org.jsfml.window.VideoMode;

import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Settings
{
    public static final Random GENERATOR = new Random();

    // Stuff to do with the window
    public static final int WINDOW_HEIGHT;
    public static final int WINDOW_WIDTH;
    public static final int WINDOW_BITS_PER_PIXEL;
    public static final int WINDOW_X_PADDING = 100;
    public static final int WINDOW_Y_PADDING = 100;
    public static final String WINDOW_TITLE = "Quest for the Abodeth";
    public static final int WINDOW_FPS = 60;
    public static final int MAX_SCORES = 10;

    // The fonts used in various main.java.questfortheabodeth.menus in the game
    public static final Font MAIN_MENU_FONT = new Font();
    public static final Font ARIAL_FONT = new Font();

    // Custom colors
    public static final Color LIGHT_GREY = new Color(231, 231, 231);
    public static final Color GREY = new Color(169, 169, 169);
    public static final Color DARK_GREY = new Color(105, 105, 105);
    public static final Color SAND = new Color(255, 178, 127);
    public static final Color DARK_SAND = new Color(127, 51, 0);

    // Cached image objects
    public static HashMap<String, Texture> LOADED_IMAGES = new HashMap<>();

    // Sets that define which directions the player can move in
    public static final Set<Integer> MOVE_LEFT_SET = new HashSet<>(Arrays.asList(
            0, 1, 2, 4, 3, 6
    ));
    public static final Set<Integer> MOVE_UP_SET = new HashSet<>(Arrays.asList(
            0, 2, 4, 8, 6, 12
    ));
    public static final Set<Integer> MOVE_RIGHT_SET = new HashSet<>(Arrays.asList(
            0, 1, 4, 8, 12, 9
    ));
    public static final Set<Integer> MOVE_DOWN_SET = new HashSet<>(Arrays.asList(
            0, 1, 2, 8, 3, 9
    ));

    // Values in CSV files mapped to game objects
    public static HashMap<Integer, String> CSV_KEYS = new HashMap<>();
    public static HashMap<String, String> AUDIO_KEYS = new HashMap<>();

    // Game variables
    public static final int BULLET_SPEED = 20;
    public static final int PLAYER_SPEED = 5;
    public static final Set<String> MELEE_WEAPONS = new HashSet<>(Arrays.asList("machete"));
    public static final Set<String> ONE_HANDED_WEAPONS = new HashSet<>(Arrays.asList("revolver", "uzi"));
    public static final Set<String> TWO_HANDED_WEAPONS = new HashSet<>(Arrays.asList("shotgun", "ar15"));

    // Audio vairables about what is currently playing
    public static boolean BACKGROUND_AUDIO_PLAYING = false;
    public static ArrayList<Clip> AUDIO_STREAMERS = new ArrayList<Clip>();

    // Game settings that can be changed in game settings menu
    public static boolean MUSIC_ON = true;
    public static boolean SOUND_EFFECTS_ON = true;
    public static boolean CROSSHAIR_VISIBLE = true;
    public static boolean ANIMATIONS = false;
    public static boolean DANK_VERSION = false;

    // Initialise a lot of the variables
    static {
        // Get the window resolution
        VideoMode desktop = VideoMode.getDesktopMode();
        WINDOW_HEIGHT = desktop.height;
        WINDOW_WIDTH = desktop.width;
        WINDOW_BITS_PER_PIXEL = desktop.bitsPerPixel;

        // Load in the font files
        File f = new File("res/fonts/pixelated.ttf");
        File f2 = new File("res/fonts/SHPinscher-Regular.otf");
        try {
            MAIN_MENU_FONT.loadFromFile(f.toPath());
            ARIAL_FONT.loadFromFile(f2.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Environment Objects
        CSV_KEYS.put(-4, "lowerDoor");
        CSV_KEYS.put(-3, "rightDoor");
        CSV_KEYS.put(-2, "topDoor");
        CSV_KEYS.put(-1, "leftDoor");
        CSV_KEYS.put(0, null);
        CSV_KEYS.put(1, "wall"); //C
        CSV_KEYS.put(2, "floor");
        CSV_KEYS.put(3, null); //CI
        CSV_KEYS.put(4, "water"); //I
        CSV_KEYS.put(5, "quicksand"); //I
        CSV_KEYS.put(6, "spikeTrap"); //I
        CSV_KEYS.put(7, "switchPuzzle"); //(C?)I
        CSV_KEYS.put(8, "shootingArrowTrap"); //I
        CSV_KEYS.put(9, "swingingAxeTrap"); //I
        CSV_KEYS.put(10, "lava"); //I
        CSV_KEYS.put(11, "rollingBoulder"); //(ENEMY)
        CSV_KEYS.put(12, "fierySphinx"); //I
        CSV_KEYS.put(13, "graveyard"); //I
        CSV_KEYS.put(14, "crushingWalls"); //CI
        //Minimap Symbols
        CSV_KEYS.put(15, "startRoom");
        CSV_KEYS.put(16, "endRoom");
        CSV_KEYS.put(17, "path");
        CSV_KEYS.put(18, "visitedRoom");
        //Enemy Symbols
        CSV_KEYS.put(19, "zombie");
        CSV_KEYS.put(20, "jackal");
        CSV_KEYS.put(21, "bat");
        CSV_KEYS.put(22, "spider");
        CSV_KEYS.put(23, "mummifiedSlave");
        CSV_KEYS.put(24, "giantSpider");
        CSV_KEYS.put(25, "giantZombie");
        CSV_KEYS.put(26, "egyptianMummy");
        CSV_KEYS.put(27, "crocodile");
        //Pickup Symbols
        CSV_KEYS.put(28, "healthPickup");
        CSV_KEYS.put(29, "ammoPickup");
        CSV_KEYS.put(30, "shieldPickup");
        CSV_KEYS.put(31, "speedPickupUp");
        CSV_KEYS.put(32, "speedPickupDown");
        CSV_KEYS.put(33, "damagePickup");
        //weapons
        CSV_KEYS.put(34, "machete");
        CSV_KEYS.put(35, "revolver");
        CSV_KEYS.put(36, "shotgun");
        CSV_KEYS.put(37, "ar15");
        CSV_KEYS.put(38, "uzi");
        CSV_KEYS.put(39, "sniper");
        CSV_KEYS.put(40, "whip");
        // Bosses
        CSV_KEYS.put(98, "abodeth");
        CSV_KEYS.put(99, "edemy");

        // Audio files
        AUDIO_KEYS.put("machete", "weapons/machete");
        AUDIO_KEYS.put("shotgun","weapons/shotgun");
        AUDIO_KEYS.put("revolver","weapons/revolver");
        AUDIO_KEYS.put("uzi","weapons/uzi");
        AUDIO_KEYS.put("ar15","weapons/ar15");
        AUDIO_KEYS.put("whip", "weapons/whip");
        AUDIO_KEYS.put("sniper", "weapons/sniper");
        AUDIO_KEYS.put("arrow","traps/arrow");
        AUDIO_KEYS.put("lava","environment/lava");
        AUDIO_KEYS.put("water", "environment/water");
        AUDIO_KEYS.put("quicksand", "environment/quicksand");
        AUDIO_KEYS.put("playerHit", "playerHit");
        AUDIO_KEYS.put("mainMenu", "/mainMenu");
        AUDIO_KEYS.put("roomDataA", "roomMusic/track01");
        AUDIO_KEYS.put("roomDataB", "roomMusic/track02");
        AUDIO_KEYS.put("roomDataC", "roomMusic/track03");
        AUDIO_KEYS.put("roomDataD", "roomMusic/track04");
        AUDIO_KEYS.put("roomDataE", "roomMusic/track05");
        AUDIO_KEYS.put("roomDataF", "roomMusic/track06");
        AUDIO_KEYS.put("roomDataG", "roomMusic/track07");
        AUDIO_KEYS.put("roomDataH", "roomMusic/track08");
        AUDIO_KEYS.put("roomDataI", "roomMusic/track09");
        AUDIO_KEYS.put("roomDataJ", "roomMusic/track10");
    }

    public static final int ROOM_DIVISION_SIZE = 40;
    public static final int ROOM_DIVISION_COLUMNS = WINDOW_WIDTH / ROOM_DIVISION_SIZE;
    public static final int ROOM_DIVISION_ROWS = WINDOW_HEIGHT / ROOM_DIVISION_SIZE;

    public static StopClock GAME_TIME;
}
