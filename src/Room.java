import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import java.util.ArrayList;

public class Room implements Drawable
{
    private int type;
    private ArrayList<Drawable> drawables = new ArrayList<>();

    /**
     * Creates a new room of the specified type
     *
     * @param type (int) Room type
     */
    public Room(int type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "<Room " + type + ">";
    }

    /**
     * Makes the room drawable. Method takes all the drawables in this Room class
     * and draw them all to the screen
     *
     * @param renderTarget (RenderTarget) The window to draw to
     * @param renderStates (RenderStates) Again, what are these
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        drawables.forEach(renderTarget::draw);
    }
}
