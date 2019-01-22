import java.util.List;

public interface Menu
{
    List<Clickable> getButtons();

    void displayMenu();

    Button getChosenButton();

    Image getBackground();
}
