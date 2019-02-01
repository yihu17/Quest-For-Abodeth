import java.util.List;

public interface Menu
{
    /**
     * A list of all the clickable object in the menu
     * A clickable object is probably going to be a ClickableImage
     * or a Button
     * @see ClickableImage
     * @see Button
     * @return (List) List of Clickables
     */
    List<Clickable> getButtons();

    /**
     * Displays the menu on screen
     * Requires a game loop that takes control of the window
     * and draw all of its own Drawables
     *
     * @see Main#run()
     */
    void displayMenu();

    /**
     * Returns the object that was clicked in the menu
     * This object should have a text meaning that represents what was pressed
     * @return (Clickable) Clicked object
     */
    Clickable getChosenButton();

    /**
     * An image to display as the background
     * @return (Image) Background image
     */
    Image getBackground();
}
