import org.jsfml.system.Vector2f;

public class ClickableImage extends Image implements Clickable
{
    private EventHandler handler = null;
    private String text;

    /**
     * Creates a new image object
     *
     * @param x          (int) X coordinate of the top left of the image
     * @param y          (int) Y coordinate of the top left of the image
     * @param filename   (String) Image name to load
     * @param buttonText (String) Button text (Not shown in this class)
     */
    public ClickableImage(int x, int y, String filename, String buttonText)
    {
        super(x, y, filename);
        this.text = buttonText;
    }

    /**
     * The EventHandler to run when the image is clicked
     *
     * @param handler (EventHandler) Function to run on click
     */
    @Override
    public void setOnPress(EventHandler handler)
    {
        this.handler = handler;
    }

    /**
     * If the image is being clicked, then run the attached EventHndler
     * @param mousePosition (Vector2f) Position of the mouse
     */
    @Override
    public void press(Vector2f mousePosition)
    {
        if (this.getGlobalBounds().contains(mousePosition)) {
            if (handler != null) {
                handler.run();
            }
        }
    }

    /**
     * Do nothing on realease as the image (by default) does not change
     * @param mousePosition (Vector2f) Position of the mouse
     */
    @Override
    public void release(Vector2f mousePosition)
    {
        // No background color to change so do nothing
    }

    /**
     * Get the text representation of the image
     * @return (String) String representation of the image
     */
    @Override
    public String getText()
    {
        return text;
    }
}
