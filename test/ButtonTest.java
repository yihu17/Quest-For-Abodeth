import main.java.questfortheabodeth.menus.Button;
import org.jsfml.graphics.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ButtonTest
{
    @Test
    public void testOutlineColor()
    {

        Button test = new Button(100, 100, 100, 100);
        test.setOutlineColor(Color.RED);

        assertEquals(test.getOutlineColor(), Color.RED);
    }
}