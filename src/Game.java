import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Game
{
    private RenderWindow window;
    private boolean gameRunning;
    private Room[][] rooms;
    private Room currentRoom;
    private Player player;

    private CopyOnWriteArraySet<Powerup> roomPowerups = new CopyOnWriteArraySet<>();

    public Game(RenderWindow window)
    {
        Settings.audioStreamer.stop();
        this.window = window;
        this.window.clear();
        this.gameRunning = true;
        this.player = new Player();
        this.roomPowerups.add(new DamagePlus(600, 600));


        // Read the CSV file
        rooms = new Room[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rooms[i][j] = new Room(Settings.GENERATOR.nextInt(4));
            }
        }
        currentRoom = rooms[0][0];

        Helper.printMatrix(rooms);
    }

    public void run()
    {
        int clocker = 0;
        while (gameRunning) {
            window.clear();

            // Draw the room
            window.draw(currentRoom);
            roomPowerups.forEach(window::draw);
            window.draw(player);


            window.display();

            roomPowerups.forEach(new Consumer<Powerup>()
            {
                @Override
                public void accept(Powerup powerup)
                {
                    if (Helper.checkOverlap(player, powerup)) {
                        roomPowerups.remove(powerup);
                    } else {
                        //System.out.println("Player not in range of powerup");
                    }
                }
            });

            // Check for close events
            for (Event e : window.pollEvents()) {
                Helper.checkCloseEvents(e, window);
            }

            // Check for user key processes
            if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE) && clocker > 10) { //used to prevent screen flickering
                openInGameMenu(/*saveGameScreenshot()*/);
                clocker = 0;
            }
            if (Keyboard.isKeyPressed(Keyboard.Key.W)) {
                player.moveUp();
            }
            if (Keyboard.isKeyPressed(Keyboard.Key.A)) {
                player.moveLeft();
            }
            if (Keyboard.isKeyPressed(Keyboard.Key.S)) {
                player.moveDown();
            }
            if (Keyboard.isKeyPressed(Keyboard.Key.D)) {
                player.moveRight();
            }
            clocker++;
        }
    }

    private void openInGameMenu(/*boolean screenshotSaved*/)
    {
        GameMenu ingame = new GameMenu(window /*, screenshotSaved*/);
        ingame.displayMenu();
        Button b = ingame.getChosenButton();
        if (b == null) {
            return;
        }
        switch (b.getText().toLowerCase()) {
            case "quit to menu":
                gameRunning = false;
                break;
            case "continue":
                // Continue playing the game
                break;
            default:
                throw new AssertionError("Unknown button was pressed: " + b.getText());
        }
    }

    public boolean saveGameScreenshot() {
        try {
            Robot robot = new Robot();
            String fileName = "gamePausedScreenshot.jpg";
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()); //fullscreen
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            ImageIO.write(screenFullImage, "jpg", new File("res/" + fileName));
            return true;
        } catch (AWTException | IOException ex) {
            System.out.println("Error taking screenshot");
            return false;
        }
    }
}
