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
    private ReadOnlyIntegerProperty ammo;

    public AmmoCount(ReadOnlyIntegerProperty ammo)
    {
        this.setColor(Color.WHITE);
        this.setFont(Settings.MAIN_MENU_FONT);
        this.setScale(new Vector2f(0.75f, 0.75f));
        this.setString(ammo.getValue().toString());
        this.setPosition(new Vector2f(29, 820));
        this.ammo = ammo;

        background = new Image(7, 803, "res/assets/pickups/ammobox_blank.png");
        background.setScale(new Vector2f(2, 2));

        switchAmmo(ammo);
    }

    public Image getBackground()
    {
        return background;
    }

    public void switchAmmo(ReadOnlyIntegerProperty ammo)
    {
        this.ammo = ammo;
        this.ammo.addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                setString(newValue.toString());
                setPosition(new Vector2f(
                        ammo.getValue().toString().length() == 3 ? 23 : 27,
                        820
                ));
            }
        });

        setString(String.valueOf(ammo.get()));
        setPosition(new Vector2f(
                ammo.getValue().toString().length() == 3 ? 23 : 27,
                820
        ));
    }
}
