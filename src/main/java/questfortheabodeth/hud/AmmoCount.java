package main.java.questfortheabodeth.hud;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import main.java.questfortheabodeth.Settings;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

public class AmmoCount extends Text {
    public AmmoCount(ReadOnlyIntegerProperty ammo) {
        this.setColor(Color.WHITE);
        this.setFont(Settings.MAIN_MENU_FONT);
        this.setPosition(new Vector2f(5, 1020));
        this.setString("Ammo: " + ammo.getValue());

        ammo.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                setString("Ammo: " + newValue);
            }
        });
    }
}
