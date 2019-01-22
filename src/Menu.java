import java.util.List;

public interface Menu
{
    List<Button> getButtons();

    void displayMenu();

    Button getChosenButton();

    Image getBackground();
}
