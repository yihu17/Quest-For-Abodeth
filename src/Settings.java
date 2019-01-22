import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Texture;
import org.jsfml.window.VideoMode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Settings
{
    // Stuff to do with the window
    public static final int WINDOW_HEIGHT;
    public static final int WINDOW_WIDTH;
    public static final int WINDOW_BITS_PER_PIXEL;
    public static final int WINDOW_X_PADDING = 100;
    public static final int WINDOW_Y_PADDING = 100;
    public static final String WINDOW_TITLE = "Quest for the Abodeth";

    // The fonts used in various menus in the game
    public static final Font MAIN_MENU_FONT = new Font();

    // Custom colors
    public static final Color LIGHT_GREY = new Color(231, 231, 231);
    public static final Color GREY = new Color(169, 169, 169);
    public static final Color DARK_GREY = new Color(105, 105, 105);

    // Cached texture files
    public static HashMap<String, Texture> LOADED_IMAGES = new HashMap<>();

    static {
        VideoMode desktop = VideoMode.getDesktopMode();
        WINDOW_HEIGHT = desktop.height;
        WINDOW_WIDTH = desktop.width;
        WINDOW_BITS_PER_PIXEL = desktop.bitsPerPixel;

        File f = new File("res/pixelated.ttf");
        try {
            MAIN_MENU_FONT.loadFromFile(f.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        for (VideoMode v : VideoMode.getFullscreenModes()) {
            System.out.println(v);
        }
    }
}
