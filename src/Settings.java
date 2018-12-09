import org.jsfml.window.VideoMode;

public class Settings
{
    public static final int WINDOW_HEIGHT;
    public static final int WINDOW_WIDTH;
    public static final int WINDOW_BITS_PER_PIXEL;
    public static final String WINDOW_TITLE = "Quest for the Abodeth";

    static {
        VideoMode desktop = VideoMode.getDesktopMode();
        WINDOW_HEIGHT = desktop.height;
        WINDOW_WIDTH = desktop.width;
        WINDOW_BITS_PER_PIXEL = desktop.bitsPerPixel;
    }
}
