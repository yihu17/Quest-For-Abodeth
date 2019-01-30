import org.jsfml.system.Vector2i;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HelperTest
{
    @Test
    public void testAngleCalculator()
    {
        // Checks the function in the positive x and y space
        Vector2i playerPosition = new Vector2i(200, 200);
        Vector2i mousePosition = new Vector2i(691, 93);
        assertEquals(
                Math.round(Helper.getAngleBetweenPoints(playerPosition, mousePosition)),
                Math.round(77.71)
        );

        // Checks the function in the positive x and negative y
        playerPosition = new Vector2i(200, 200);
        mousePosition = new Vector2i(691, 393);
        assertEquals(
                Math.round(Helper.getAngleBetweenPoints(playerPosition, mousePosition)),
                Math.round(111.46)
        );
    }
}
