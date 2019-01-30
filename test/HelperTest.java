import org.jsfml.system.Vector2i;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HelperTest
{
    @Test
    public void testAnglePositiveXPositiveY()
    {
        Vector2i playerPosition = new Vector2i(200, 200);
        Vector2i mousePosition = new Vector2i(691, 93);
        assertEquals(
                Math.round(Helper.getAngleBetweenPoints(playerPosition, mousePosition)),
                Math.round(77.71)
        );
    }

    @Test
    public void testAnglePositiveXNegativeY()
    {
        Vector2i playerPosition = new Vector2i(200, 200);
        Vector2i mousePosition = new Vector2i(691, 393);
        assertEquals(
                Math.round(Helper.getAngleBetweenPoints(playerPosition, mousePosition)),
                Math.round(111.46)
        );
    }

    @Test
    public void testAngleNegativeXNegativeY()
    {
        // Checks the function in the negative x and negative y space
        Vector2i playerPosition = new Vector2i(691, 200);
        Vector2i mousePosition = new Vector2i(200, 393);
        assertEquals(
                Math.round(Helper.getAngleBetweenPoints(playerPosition, mousePosition)),
                Math.round(248.54)
        );
    }

    @Test
    public void testAngleNegativeXPositiveY()
    {
        Vector2i playerPosition = new Vector2i(691, 200);
        Vector2i mousePosition = new Vector2i(200, 93);
        assertEquals(
                Math.round(Helper.getAngleBetweenPoints(playerPosition, mousePosition)),
                Math.round(282.29)
        );
    }
}
