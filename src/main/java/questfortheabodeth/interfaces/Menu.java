package main.java.questfortheabodeth.interfaces;

import main.java.questfortheabodeth.sprites.Image;

import java.util.List;

public interface Menu {
    /**
     * A list of all the clickable object in the menu
     * A clickable object is probably going to be a main.java.questfortheabodeth.menus.ClickableImage
     * or a main.java.questfortheabodeth.menus.Button
     *
     * @return (List) List of Clickables
     * @see main.java.questfortheabodeth.menus.ClickableImage
     * @see main.java.questfortheabodeth.menus.Button
     */
    List<Clickable> getButtons();

    /**
     * Displays the menu on screen
     * Requires a game loop that takes control of the window
     * and draw all of its own Drawables
     *
     * @see main.java.questfortheabodeth.Main#run()
     */
    void displayMenu();

    /**
     * Returns the object that was clicked in the menu
     * This object should have a text meaning that represents what was pressed
     *
     * @return (main.java.questfortheabodeth.interfaces.Clickable) Clicked object
     */
    Clickable getChosenButton();

    /**
     * An image to display as the background
     *
     * @return (main.java.questfortheabodeth.sprites.Image) Background image
     */
    Image getBackground();
}
