package main.java.questfortheabodeth;

import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.Environment;
import main.java.questfortheabodeth.environments.Room;
import main.java.questfortheabodeth.hud.MiniMap;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Movable;
import main.java.questfortheabodeth.interfaces.Powerup;
import main.java.questfortheabodeth.menus.Button;
import main.java.questfortheabodeth.menus.GameMenu;
import main.java.questfortheabodeth.powerups.DamagePlus;
import main.java.questfortheabodeth.powerups.HealthBoost;
import main.java.questfortheabodeth.powerups.Pickup;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;


public class Game
{
    private RenderWindow window;
    private boolean gameRunning;
    private Room[][] rooms;
    private Room currentRoom;
    private Player player;
    private MiniMap miniMap;

    private CopyOnWriteArraySet<Movable> movables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Drawable> drawables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Collidable> collidables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Bullet> bullets = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Enemy> enemies = new CopyOnWriteArraySet<>();

    public Game(RenderWindow window)
    {
        Settings.AUDIO_STREAMER.stop();
        this.window = window;
        this.window.clear();
        this.gameRunning = true;
        this.player = new Player();

        // Read the CSV file
        FileOperator ops = new FileOperator("res/assets/CSVs/roomLayout.csv");
        ArrayList<String> file = ops.readToList();
        int rows = file.size();
        int cols = file.get(0).split(",").length;
        int startRow = -1;
        int startCol = -1;
        rooms = new Room[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int roomCode = Integer.parseInt(file.get(i).split(",")[j]);
                rooms[i][j] = new Room(roomCode);
                if (roomCode == 1) {
                    startCol = j;
                    startRow = i;
                }
            }
        }

        if (startRow < 0 || startCol < 0) {
            throw new IllegalStateException("Player has no start room");
        }
        currentRoom = rooms[startRow][startCol];
        miniMap = new MiniMap(rows, cols, startRow, startCol);

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
            // Once they have moved check to ensure they are still in the bounds of the window
            this.moveMovables();


            // Check for close events
            for (Event e : window.pollEvents()) {
                Helper.checkCloseEvents(e, window);
                if (e.type == MouseEvent.Type.MOUSE_BUTTON_PRESSED) {
                    // The player character has fired a bullet
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

            int moveValues = runPlayerCollisions();
            runBulletCollisions();
            runEnemyCollisions();


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

            clocker++;
        }
    }

    private int runPlayerCollisions()
    {
        int moveValues = 0;
        HashSet<Integer> playerCanMove = new HashSet<>();
        for (Collidable c : collidables) {
            if (c instanceof Player || c instanceof Bullet) {
                continue;
            }
            if (c instanceof Environment) {
                int overlap = Helper.checkOverlap(player, c);
                if (0 < overlap) {
                    playerCanMove.add(overlap);
                }
            }

            if (c instanceof Powerup) {
                int overlap = Helper.checkOverlap(player, c);
                if (0 < overlap) {
                    ((Powerup) c).applyBuff(player);
                    drawables.remove(c);
                    collidables.remove(c);
                }
            }
        }
        for (Integer i : playerCanMove) {
            moveValues += i;
        }
        return moveValues;
    }

    private void runBulletCollisions()
    {
        for (Bullet b : bullets) {
            for (Collidable c : collidables) {
                if (c instanceof Bullet || c instanceof Powerup) {
                    continue;
                }
                if (0 < Helper.checkOverlap(b, c)) {
                    if (c instanceof Enemy) {
                        ((Enemy) c).decreaseHealth(b.getDamage());
                        ((Enemy) c).moveRight();
                        ((Enemy) c).moveRight();
                        ((Enemy) c).moveRight();
                        System.out.println("Bullet hit an enemy: " + c);
                    }
                    b.setX(2 * Settings.WINDOW_WIDTH);
                    b.setY(2 * Settings.WINDOW_HEIGHT);
                }
            }
        }
    }

    private void runEnemyCollisions()
    {
        for (Enemy e : enemies) {
            HashSet<Integer> values = new HashSet<>();
            for (Collidable c : collidables) {
                if (c instanceof Enemy) {
                    continue;
                }
                values.add(Helper.checkOverlap(e, c));
            }

            int value = 0;
            for (Integer i : values) {
                value += i;
            }
            e.setMoveValue(value);
        }
    }

    private void scanRoom()
    {
        collidables.addAll(currentRoom.getCollidables());
        for (Enemy e : currentRoom.getEnemies()) {
            e.setPlayer(player);
            movables.add(e);
            enemies.add(e);
            drawables.add(e);
        }

        for (Pickup p : currentRoom.getPickups()) {
             collidables.add(p);
             drawables.add(p);
        }
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
