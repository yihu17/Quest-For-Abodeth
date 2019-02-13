package main.java.questfortheabodeth.hud;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import main.java.questfortheabodeth.characters.Player;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * The HealthBar class is based in a character health of 100
 * This 100 points is divided into 20 sections each 5 pixels wide
 */
public class HealthBar implements Drawable {
    private RectangleShape background;
    private RectangleShape health;

    public HealthBar(Player player) {
        background = new RectangleShape();
        background.setPosition(5, 1055);
        background.setSize(new Vector2f(200, 20));
        background.setFillColor(Color.BLACK);
        background.setOutlineColor(Color.WHITE);
        background.setOutlineThickness(2);

        health = new RectangleShape();
        health.setPosition(5, 1055);
        health.setSize(new Vector2f(200, 20));
        health.setFillColor(Color.RED);

        player.healthProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                if (newValue.intValue() >= 0) {
                    // This needs to work out the value between 0 and 200 that correlates to the percentage health the player has left
                    health.setSize(new Vector2f(200 - (newValue.intValue() / 100) * 200, 20));
                }
            }
        });
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        renderTarget.draw(background);
        renderTarget.draw(health);
    }
}
