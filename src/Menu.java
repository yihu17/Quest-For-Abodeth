import java.util.List;

public interface Menu
{
    /**
     * A list of all the clickable object in the menu
     *
     * @return (List) List of Clickables
     */
    List<Clickable> getButtons();

    /**
     * Displays the menu on screen
     */
    void displayMenu();

    /**
     * Returns the object that was clicked in the menu
     * @return (Clickable) Clicked object
     */
    Clickable getChosenButton();

    /**
     * An image to display as the background
     * @return (Image) Background image
     */
    Image getBackground();
}
