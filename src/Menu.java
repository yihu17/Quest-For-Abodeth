import java.util.List;

public interface Menu
{
    List<Clickable> getButtons();

    void displayMenu();

    Clickable getChosenButton();

    Image getBackground();
}
