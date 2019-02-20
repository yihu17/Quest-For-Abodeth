package main.java.questfortheabodeth.hud;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Player;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * The HealthBar class is based in a character health of 100
 * This 100 points is divided into 20 sections each 5 pixels wide
 */
public class HealthBar implements Drawable
{
    private RectangleShape background;
    private RectangleShape health;
    private Text healthText;

    public HealthBar(Player player)
    {
        healthText = new Text();
        healthText.setString("Health:");
        healthText.setPosition(5, 0);
        healthText.setFont(Settings.MAIN_MENU_FONT);
        healthText.setColor(Color.WHITE);

        background = new RectangleShape();
        background.setPosition(98, 10);
        background.setSize(new Vector2f(200, 20));
        background.setFillColor(Color.BLACK);
        background.setOutlineColor(Color.WHITE);
        background.setOutlineThickness(2);

        health = new RectangleShape();
        health.setPosition(98, 10);
        health.setSize(new Vector2f(200, 20));
        health.setFillColor(Color.RED);

        player.healthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 0) {
                health.setSize(new Vector2f(2 * newValue.intValue(), 20));
            }
        });
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        renderTarget.draw(background);
        renderTarget.draw(health);
        renderTarget.draw(healthText);
    }
}
