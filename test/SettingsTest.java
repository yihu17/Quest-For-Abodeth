import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest
{
    @Test
    public void testScreenWidth()
    {
        assertEquals(Settings.WINDOW_WIDTH, 1920);
    }

    @Test
    public void testScreenHeight()
    {
        assertEquals(Settings.WINDOW_HEIGHT, 1080);
    }
}