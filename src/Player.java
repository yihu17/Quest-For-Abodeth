import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

public class Player extends Character
{
    private static String imageName = "res/zombie.png";

    public Player()
    {
        super(200, 200, 100, imageName);

    }

    @Override
    public void kill()
    {
        System.out.println("I died!");
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(this.getImage());
    }
}
