package main.java.questfortheabodeth;

import main.java.questfortheabodeth.characters.Character;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.Environment;
import main.java.questfortheabodeth.environments.Room;
import main.java.questfortheabodeth.environments.interactables.Door;
import main.java.questfortheabodeth.environments.traps.TrapZone;
import main.java.questfortheabodeth.hud.*;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.interfaces.Movable;
import main.java.questfortheabodeth.interfaces.Powerup;
import main.java.questfortheabodeth.menus.Button;
import main.java.questfortheabodeth.menus.GameMenu;
import main.java.questfortheabodeth.menus.PlayerDiedMenu;
import main.java.questfortheabodeth.powerups.Pickup;
import main.java.questfortheabodeth.weapons.Bullet;
import main.java.questfortheabodeth.weapons.Melee;
import main.java.questfortheabodeth.weapons.WeaponPickup;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
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
    private int roomCol = -1;
    private int roomRow = -1;

    private Player player;
    private Door doorInRange = null;

    private MiniMap miniMap;
    private HealthBar healthBar;
    private WeaponWheel weaponWheel;
    private AmmoCount ammoCount;
    private HudElements hud;

    private CopyOnWriteArraySet<Movable> movables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Drawable> drawables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Collidable> collidables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Bullet> bullets = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Enemy> enemies = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Interactable> interactables = new CopyOnWriteArraySet<>();


    public Game(RenderWindow window)
    {
        if (Settings.AUDIO_STREAMER != null) {
            Settings.AUDIO_STREAMER.stop();
        }
        this.window = window;
        this.window.clear();
        this.gameRunning = true;
        this.player = new Player();

        // Read the CSV file
        FileOperator ops = new FileOperator("res/assets/CSVs/roomLayout.csv");
        ArrayList<String> file = ops.readToList();
        int rows = file.size();
        int cols = file.get(0).split(",").length;
        rooms = new Room[rows][cols];


        /*
        This doesn't work because a lot of the array is null when it is still being generated causing lots of the
        doors to not appear. I don't think my room logic is wrong

        Probably needs to be done a separate loop
         */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int roomCode = Integer.parseInt(file.get(i).split(",")[j]);
                if (roomCode == 0) {
                    rooms[i][j] = null;
                } else {
                    rooms[i][j] = new Room(roomCode * -1, false, false, false, false);
                }
                if (roomCode == 1) {
                    roomCol = j;
                    roomRow = i;
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (rooms[i][j] == null) {
                    continue;
                }
                rooms[i][j] = new Room(
                        rooms[i][j].getType() * -1,
                        (i - 1 >= 0 && rooms[i - 1][j] != null) ? true : false, // up
                        (i + 1 < rows && rooms[i + 1][j] != null) ? true : false, // down
                        (j - 1 >= 0 && rooms[i][j - 1] != null) ? true : false, // left
                        (j + 1 < cols && rooms[i][j + 1] != null) ? true : false  // right
                );
            }
        }

        Helper.printMatrix(rooms);

        if (roomRow < 0 || roomCol < 0) {
            throw new IllegalStateException("Player has no start room");
        }
        currentRoom = rooms[roomRow][roomCol];

        miniMap = new MiniMap(rows, cols, roomRow, roomCol);
        healthBar = new HealthBar(player);
        weaponWheel = new WeaponWheel();
        ammoCount = new AmmoCount(player.ammoProperty());
        hud = new HudElements(healthBar, miniMap, weaponWheel, ammoCount);

        new Thread(Settings.GAME_TIME).start();

        this.scanRoom();
    }

    public void run()
    {
        int clocker = 0;
        Button time = new Button(120, 40, (Settings.WINDOW_WIDTH / 2) - 60, 10, "0");
        time.setTextXOffset(8);
        while (gameRunning && player.isCharacterAlive()) {
            time.setText(Settings.GAME_TIME.getFormattedTime());

            window.clear();
            // Draw the room
            window.draw(currentRoom);
            window.draw(player);
            drawables.forEach(window::draw);
            window.draw(hud);
            window.draw(time);
            window.display();

            // Move every single movable object (enemy movements, bullets etc.)
            // Once they have moved check to ensure they are still in the bounds of the window
            this.moveMovables();

            // Check for close events
            for (Event e : window.pollEvents()) {
                Helper.checkCloseEvents(e, window);
                if (
                        e.type == MouseEvent.Type.MOUSE_BUTTON_PRESSED &&
                                Mouse.isButtonPressed(Mouse.Button.LEFT) &&
                                (System.currentTimeMillis() - player.getLastTimeAttack()) >= player.getCurrentWeapon().getFireRate() &&
                                player.getCurrentWeapon() != null) {
                    if (!(player.getCurrentWeapon() instanceof Melee)) {
                        // The player character has fired a bullet
                        player.setLastTimeAttack(System.currentTimeMillis());
                        if (0 < player.ammoProperty().getValue()) {
                            int max = player.getCurrentWeapon().getName().equals("shotgun") ? 3 : 1;
                            int[] angles = {0, -6, 6};
                            for (int i = 0; i < max; i++) {
                                Bullet b = new Bullet(
                                        (int) player.getPlayerCenter().x,
                                        (int) player.getPlayerCenter().y,
                                        Helper.getAngleBetweenPoints(new Vector2i(player.getVectorPosition()), e.asMouseEvent().position) + angles[i],
                                        player.getCurrentWeapon().getDamageDealt()
                                );

                                movables.add(b);
                                drawables.add(b);
                                collidables.add(b);
                                bullets.add(b);
                                player.decreaseAmmo();
                            }
                        }
                    } else {
                        // Create a larger than normal FloatRect for the player
                        FloatRect meleeRange = new FloatRect(
                                player.getX() - player.getWidth(),
                                player.getY() - player.getHeight(),
                                3 * player.getWidth(),
                                3 * player.getHeight()
                        );
                        // Damage an enemy
                        enemies.forEach(enemy -> enemy.decreaseHealth(
                                0 < Helper.checkOverlap(enemy, meleeRange) ? player.getCurrentWeapon().getDamageDealt() : 0
                        ));
                    }
                } else if (e.type == Event.Type.KEY_PRESSED) {
                    if (e.asKeyEvent().key == Keyboard.Key.NUM1) {
                        if (player.switchWeapon(1)) {
                            weaponWheel.selectWeapon(player.getCurrentWeapon());
                            System.out.println("Player switched to their melee weapon");
                        } else {
                            System.out.println("Melee switch not allowed");
                        }
                    } else if (e.asKeyEvent().key == Keyboard.Key.NUM2) {
                        if (player.switchWeapon(2)) {
                            weaponWheel.selectWeapon(player.getCurrentWeapon());
                        }
                    } else if (e.asKeyEvent().key == Keyboard.Key.NUM3) {
                        if (player.switchWeapon(3)) {
                            weaponWheel.selectWeapon(player.getCurrentWeapon());
                        }
                    } else if (e.asKeyEvent().key == Keyboard.Key.E) {
                        // Player attempted to go through a door
                        if (doorInRange != null) {
                            // Go through the door
                            System.out.println("Going through the door " + doorInRange);
                            switchRoom(doorInRange.getLinkedDoor());
                        }
                    } else if (e.asKeyEvent().key == Keyboard.Key.M) {
                        hud.toggleMiniMapVisibility();
                    }
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
            runPlayerInteracts();
            runEnemyInteracts();


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
        //Don't know this should be in Game.java or elsewhere?(:)

        //This will be moved to when player reaches end of game (i.e final boss is killed)
        //FileOperator scores = new FileOperator("res/assets/CSVs/scores.csv");
        //scores.writeNewLine(Settings.GAME_TIME.getFormattedTime());

        if (!player.isCharacterAlive()) {
            openPlayerDiedMenu();
        }
        Settings.GAME_TIME = null;

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

            if (c instanceof Enemy) {
                int overlap = Helper.checkOverlap(player, c);
                if (0 < overlap) {
                    if (System.currentTimeMillis() - player.getLastTimeHit() >= ((Enemy) c).getAttackSpeed()) {
                        player.decreaseHealth(((Enemy) c).getAttackPower());
                        player.setLastTimeHit(System.currentTimeMillis());
                    }
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
                if (c instanceof Bullet || c instanceof Powerup || c instanceof WeaponPickup) {
                    continue;
                }
                if (0 < Helper.checkOverlap(b, c)) {
                    if (c instanceof Enemy) {
                        ((Enemy) c).decreaseHealth(player.getAdditionalDamage());

                        new Thread((Character) c).start(); //pauses enemy movement


                        //System.out.println("Bullet hit an enemy: " + c);
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
                if (c instanceof Enemy || c instanceof WeaponPickup || c instanceof Powerup) {
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

    private void runPlayerInteracts()
    {
        HashSet<Class<? extends Interactable>> currentInteracts = new HashSet<>();
        boolean localDoorRange = false;
        for (Interactable i : interactables) {
            int overlap;
            if (i instanceof TrapZone) {
                overlap = Helper.checkOverlap(player, ((TrapZone) i).getGlobalBounds());
            } else {
                overlap = Helper.checkOverlap(player, i);
            }
            if (0 < overlap) {
                // Player is on top of something
                if (i instanceof WeaponPickup) {
                    if (Keyboard.isKeyPressed(Keyboard.Key.E) && !player.hasWeapon(((WeaponPickup) i).getName())) {
                        // The player is pressing E and also does not have the weapon
                        weaponWheel.setWeapon(player.pickUpWeapon((WeaponPickup) i));
                        weaponWheel.selectWeapon(player.pickUpWeapon((WeaponPickup) i));
                        player.setWeaponImage(((WeaponPickup) i).getName());
                        ((WeaponPickup) i).remove();
                    } else if (player.hasWeapon(((WeaponPickup) i).getName())) {
                        // the player already has the weapon so add ammo
                        weaponWheel.setWeapon(player.pickUpWeapon((WeaponPickup) i));
                        ((WeaponPickup) i).remove();
                    } else {
                        // Player does not have the weapon and does not want it
                        ;
                    }
                } else if (i instanceof Door) {
                    doorInRange = (Door) i;
                    localDoorRange = true;
                } else if (i instanceof TrapZone) {
                    ((TrapZone) i).trigger();
                } else {
                    i.interact(player);
                    currentInteracts.add(i.getClass());
                }
            }
        }

        if (!localDoorRange) {
            doorInRange = null;
        }
        player.resetInteracts(currentInteracts);
    }

    private void runEnemyInteracts()
    {
        for (Enemy e : enemies) {
            HashSet<Class<? extends Interactable>> currentInteracts = new HashSet<>();
            for (Interactable i : interactables) {
                if (i instanceof WeaponPickup || i instanceof Door || i instanceof Powerup) {
                    continue;
                } else {
                    int overlap = Helper.checkOverlap(e, i);
                    if (0 < overlap) {
                        i.buffEnemy(e);
                        currentInteracts.add(i.getClass());
                    }
                }
            }
            e.resetInteracts(currentInteracts);
        }
    }

    private void scanRoom()
    {

        if (Settings.AUDIO_STREAMER != null && !Settings.AUDIO_STREAMER.isActive()) {
            currentRoom.playMusic();
        }

        collidables.clear();
        drawables.clear();
        enemies.clear();
        movables.clear();
        interactables.clear();
        bullets.clear();

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

        for (WeaponPickup w : currentRoom.getWeapons()) {
            interactables.add(w);
            drawables.add(w);
        }

        interactables.addAll(currentRoom.getInteractables());
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
            System.out.println("Returned button was null");
            return;
        }

        switch (b.getText().toLowerCase()) {
            case "quit to menu":
                gameRunning = false;
                System.out.println("Game no longer running");
                break;
            case "continue":
                // Continue playing the game
                break;
            default:
                throw new AssertionError("Unknown button was pressed: " + b.getText());
        }
    }

    private void openPlayerDiedMenu()
    {
        PlayerDiedMenu playerDiedMenu = new PlayerDiedMenu(window /*, screenshotSaved*/, Settings.GAME_TIME.getFormattedTime());
        playerDiedMenu.displayMenu();
        Button b = playerDiedMenu.getChosenButton();
        if (b == null) {
            return;
        }
    }

    public boolean saveGameScreenshot()
    {
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

    private void switchRoom(int direction)
    {
        switch (direction) {
            case -1:
                // Going left
                if (rooms[roomRow][roomCol - 1] == null || roomCol - 1 < 0) {
                    System.out.println("Illegal move");
                    return;
                }
                miniMap.move(MiniMap.Directions.LEFT);
                roomCol--;
                break;
            case -2:
                // Going up
                if (rooms[roomRow - 1][roomCol] == null || roomRow - 1 < 0) {
                    System.out.println("Illegal move");
                    return;
                }
                miniMap.move(MiniMap.Directions.UP);
                roomRow--;
                break;
            case -3:
                // Going right
                if (rooms[roomRow][roomCol + 1] == null || rooms[0].length < roomCol + 1) {
                    System.out.println("Illegal move");
                    return;
                }
                miniMap.move(MiniMap.Directions.RIGHT);
                roomCol++;
                break;
            case -4:
                // Going down
                if (rooms[roomRow + 1][roomCol] == null || rooms.length < roomRow + 1) {
                    System.out.println("Illegal move");
                    return;
                }
                miniMap.move(MiniMap.Directions.DOWN);
                roomRow++;
                break;
            default:
                throw new AssertionError("Unknown direction to travel in: " + direction);
        }

        currentRoom = rooms[roomRow][roomCol];
        for (Interactable i : currentRoom.getInteractables()) {
            if (i instanceof Door) {
                Door d = (Door) i;
                if (d.getLinkedDoor() == -1 && doorInRange.getLinkedDoor() == -3) {
                    player.setPosition((int) (d.getX() + 100), (int) d.getY());
                    break;
                } else if (d.getLinkedDoor() == -3 && doorInRange.getLinkedDoor() == -1) {
                    player.setPosition((int) (d.getX() - 100), (int) d.getY());
                    break;
                } else if (d.getLinkedDoor() == -2 && doorInRange.getLinkedDoor() == -4) {
                    player.setPosition((int) d.getX(), (int) (d.getY() + 100));
                    break;
                } else if (d.getLinkedDoor() == -4 && doorInRange.getLinkedDoor() == -2) {
                    player.setPosition((int) d.getX(), (int) (d.getY() - 100));
                    break;
                }
            }
        }

        scanRoom();
    }
}
