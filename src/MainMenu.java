import org.jsfml.graphics.Image;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenu implements Menu
{
    private ArrayList<Button> buttons = new ArrayList<>();
    private Button playButton;
    private Button instructionsButton;
    private Button highscoreButton;
    private Button creditsButton;
    private Button quitButton;

    private Button chosenButton;
    private boolean menuOpen = false;
    private RenderWindow window;
    private Image background;

    public MainMenu(RenderWindow window)
    {
        this.window = window;
        playButton = new Button(200, 50, 200, 0, "Play");
        playButton.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = playButton;
            }
        });
        instructionsButton = new Button(200, 50, 200, 75, "Instructions");
        instructionsButton.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = instructionsButton;
            }
        });
        highscoreButton = new Button(200, 50, 200, 150, "Highscores");
        highscoreButton.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = highscoreButton;
            }
        });
        creditsButton = new Button(200, 50, 200, 225, "Credits");
        creditsButton.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = highscoreButton;
            }
        });
        quitButton = new Button(200, 50, 200, 300, "Quit");
        quitButton.setOnPress(new EventHandler()
        {
            @Override
            public void run()
            {
                menuOpen = false;
                chosenButton = quitButton;
            }
        });

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
        while (menuOpen && window.isOpen()) {
            // Clear the window
            window.clear();

            // Draw window objets
            // Draw background
            buttons.forEach(window::draw);

            // Update the display
            window.display();

            // Check for events
            for (Event e : window.pollEvents()) {
                Helper.checkButtons(e, buttons);
                Helper.checkCloseEvents(e, window);
            }
        }
    }

    @Override
    public Button getChosenButton()
    {
        return chosenButton;
    }

    public void setBackground(String filename)
    {
        // Open the background and load it in
        this.background = null;
    }
}
