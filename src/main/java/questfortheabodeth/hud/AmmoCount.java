package main.java.questfortheabodeth.hud;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

public class AmmoCount extends Text
{
    private Image background;

    public AmmoCount(ReadOnlyIntegerProperty ammo)
    {
        this.setColor(Color.WHITE);
        this.setFont(Settings.MAIN_MENU_FONT);
        this.setPosition(new Vector2f(16, 770));
        this.setString(ammo.getValue().toString());

        background = new Image(3, 758, "res/assets/pickups/ammobox_blank.png");
        background.setScale(new Vector2f(2, 2));

        ammo.addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                setString(newValue.toString());
            }
        });
    }

    public Image getBackground()
    {
        return background;
    }
}
