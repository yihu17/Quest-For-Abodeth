import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenu implements Menu, Drawable
{
    private ArrayList<Button> buttons = new ArrayList<>();
    private Button playButton;
    private Button instructionsButton;
    private Button highscoreButton;
    private Button creditsButton;
    private Button quitButton;

    private Button chosenButton;
    private boolean menuOpen = false;

    public MainMenu()
    {
        playButton = new Button(200, 50, 200, 100, "Play");
        playButton.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = playButton;
            }
        });
        instructionsButton = new Button(200, 50, 200, 200, "Incstructions");
        instructionsButton.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = instructionsButton;
            }
        });
        highscoreButton = new Button(200, 50, 200, 300, "Highscores");
        creditsButton = new Button(200, 50, 200, 400, "Credits");
        quitButton = new Button(200, 50, 200, 500, "Quit");

        buttons.addAll(Arrays.asList(
                playButton,
                instructionsButton,
                highscoreButton,
                creditsButton,
                quitButton
        ));
    }

    @Override
    public List<Button> getButtons()
    {
        return this.buttons;
    }

    @Override
    public void displayMenu()
    {
        menuOpen = true;
        while (menuOpen) {

        }
    }

    @Override
    public Button getChosenButton()
    {
        return null;
    }

    @Override
    public void setBackground(String filename)
    {

    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {

    }
}
