package main.java.questfortheabodeth;

import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.Room;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Movable;
import main.java.questfortheabodeth.menus.Button;
import main.java.questfortheabodeth.menus.GameMenu;
import main.java.questfortheabodeth.powerups.DamagePlus;
import main.java.questfortheabodeth.threads.BulletThread;
import main.java.questfortheabodeth.threads.PlayerThread;
import main.java.questfortheabodeth.weapons.Bullet;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.MouseEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;


public class Game
{
    private RenderWindow window;
    private boolean gameRunning;
    private Room[][] rooms;
    private Room currentRoom;
    private Player player;

    private CopyOnWriteArraySet<Movable> movables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Drawable> drawables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Collidable> collidables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Bullet> bullets = new CopyOnWriteArraySet<>();

    private Thread[] checkerThreads;

    public Game(RenderWindow window)
    {
        Settings.AUDIO_STREAMER.stop();
        this.window = window;
        this.window.clear();
        this.gameRunning = true;
        this.player = new Player();
        DamagePlus d = new DamagePlus(0, 0);
        drawables.add(d);
        collidables.add(d);

        checkerThreads = new Thread[2];
        checkerThreads[0] = new PlayerThread(player, collidables);
        checkerThreads[1] = new BulletThread(bullets, collidables);


        // Read the CSV file
        rooms = new Room[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rooms[i][j] = new Room(Settings.GENERATOR.nextInt(4));
            }
        }

        currentRoom = rooms[0][0];
        this.scanRoom();
    }

    public void run()
    {
        int clocker = 0;
        while (gameRunning) {
            window.clear();

            // Draw the room
            window.draw(currentRoom);
            window.draw(player);
            // Draw all the drawable objects
            drawables.forEach(window::draw);
            // Update the window
            window.display();

            // Move every single movable object (enemy movements, bullets etc.)
            // Once they have moved check to enusre they are still in the bounds of the window
            this.moveMovables();


            // Check for close events
            for (Event e : window.pollEvents()) {
                Helper.checkCloseEvents(e, window);
                if (e.type == MouseEvent.Type.MOUSE_BUTTON_PRESSED) {
                    // The main.java.questfortheabodeth.characters has fired a bullet
                    Bullet b = new Bullet(
                            (int) player.getPlayerCenter().x,
                            (int) player.getPlayerCenter().y,
                            Helper.getAngleBetweenPoints(new Vector2i(player.getVectorPosition()), e.asMouseEvent().position)
                    );
                    movables.add(b);
                    drawables.add(b);
                    collidables.add(b);
                    bullets.add(b);
                }
            }

            // Check for user key processes
            if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE) && clocker > 10) { //used to prevent screen flickering
                openInGameMenu(/*saveGameScreenshot()*/);
                clocker = 0;
            }

            for (Thread t : checkerThreads) {
                t.run();
            }
            try {
                for (Thread t : checkerThreads) {
                    t.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int moveValues = ((PlayerThread) checkerThreads[0]).getReturnValue();

            if (Settings.MOVE_UP_SET.contains(moveValues) && Keyboard.isKeyPressed(Keyboard.Key.W)) {
                player.moveUp();
            }
            if (Settings.MOVE_LEFT_SET.contains(moveValues) && Keyboard.isKeyPressed(Keyboard.Key.A)) {
                player.moveLeft();
            }
            if (Settings.MOVE_DOWN_SET.contains(moveValues) && Keyboard.isKeyPressed(Keyboard.Key.S)) {
                player.moveDown();
            }
            if (Settings.MOVE_RIGHT_SET.contains(moveValues) && Keyboard.isKeyPressed(Keyboard.Key.D)) {
                player.moveRight();
            }

            //move towards enemy
            for (int i = 0; i < rooms[0][0].getEnemies().size(); i++) {
                if (player.getX() >= rooms[0][0].getEnemies().get(i).getX()) {
                    rooms[0][0].getEnemies().get(i).moveRight();
                }
                if (player.getX() <= rooms[0][0].getEnemies().get(i).getX()) {
                    rooms[0][0].getEnemies().get(i).moveLeft();
                }
                if (player.getY() >= rooms[0][0].getEnemies().get(i).getY()) {
                    rooms[0][0].getEnemies().get(i).moveDown();
                }
                if (player.getY() <= rooms[0][0].getEnemies().get(i).getY()) {
                    rooms[0][0].getEnemies().get(i).moveUp();
                }
            }


            clocker++;
        }
    }

    private void scanRoom()
    {
        collidables.addAll(currentRoom.getCollidables());

    }

    private void moveMovables()
    {
        movables.forEach(Movable::move);
        movables.forEach(movable -> {
            if (movable.getX() < -50 || Settings.WINDOW_WIDTH + 50 < movable.getX()) {
                if (movable.getY() < -50 || Settings.WINDOW_HEIGHT + 50 < movable.getY()) {
                    // Off the screen so remove all instances of it
                    if (movable instanceof Collidable) {
                        collidables.remove(movable);
                    }
                    movables.remove(movable);
                    drawables.remove(movable);
                    if (movable instanceof Bullet) {
                        bullets.remove(movable);
                    }
                }
            }
        });
    }

    private void openInGameMenu()
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
