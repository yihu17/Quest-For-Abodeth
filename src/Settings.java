import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.window.VideoMode;

import java.io.File;
import java.io.IOException;

public class Settings
{
    public static final int WINDOW_HEIGHT;
    public static final int WINDOW_WIDTH;
    public static final int WINDOW_BITS_PER_PIXEL;
    public static final int WINDOW_X_PADDING = 100;
    public static final int WINDOW_Y_PADDING = 100;
    public static final String WINDOW_TITLE = "Quest for the Abodeth";
    public static final Font ARIAL = new Font();

    public static final Color LIGHT_GREY = new Color(231, 231, 231);
    public static final Color GREY = new Color(169, 169, 169);
    public static final Color DARK_GREY = new Color(105, 105, 105);

    static {
        VideoMode desktop = VideoMode.getDesktopMode();
        WINDOW_HEIGHT = desktop.height;
        WINDOW_WIDTH = desktop.width;
        WINDOW_BITS_PER_PIXEL = desktop.bitsPerPixel;

        File f = new File("res/pixelated.ttf");
        try {
            ARIAL.loadFromFile(f.toPath());
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
