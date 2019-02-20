package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

public class LoadingScreenThread extends Thread {
    private RenderWindow window;
    private Image loadingCog = new Image((Settings.WINDOW_WIDTH / 2) - 50, (Settings.WINDOW_HEIGHT / 2) + 50, "res/assets/loadingScreenCog.png");
    ;
    private int loadingCogAngle = 0;
    private Text loadingText = new Text("LOADING GAME", Settings.MAIN_MENU_FONT, 50);

    public LoadingScreenThread(RenderWindow window) {
        this.window = window;
        this.window.setMouseCursorVisible(false);
        loadingText.setPosition(800, 500);
    }

    public void run() {
        while (true) {
            window.clear();
            rotateCog();
            window.draw(loadingCog);
            window.draw(loadingText);
            window.display();
        }
    }

    private void rotateCog() {
        if (loadingCogAngle >= 360) {
            loadingCogAngle = -1;
        }
        loadingCogAngle++;
        loadingCog.setRotation(loadingCogAngle);
    }
}
