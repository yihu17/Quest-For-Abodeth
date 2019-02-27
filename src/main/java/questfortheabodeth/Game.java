package main.java.questfortheabodeth;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import main.java.questfortheabodeth.characters.Boss;
import main.java.questfortheabodeth.characters.Character;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.characters.Player;
import main.java.questfortheabodeth.environments.Environment;
import main.java.questfortheabodeth.environments.Room;
import main.java.questfortheabodeth.environments.interactables.Door;
import main.java.questfortheabodeth.environments.traps.ShootingArrows;
import main.java.questfortheabodeth.hud.*;
import main.java.questfortheabodeth.interfaces.*;
import main.java.questfortheabodeth.menus.Button;
import main.java.questfortheabodeth.menus.GameMenu;
import main.java.questfortheabodeth.menus.PlayerDiedMenu;
import main.java.questfortheabodeth.menus.PlayerWinMenu;
import main.java.questfortheabodeth.powerups.HealthBoost;
import main.java.questfortheabodeth.powerups.Pickup;
import main.java.questfortheabodeth.powerups.TheAbodeth;
import main.java.questfortheabodeth.sprites.Image;
import main.java.questfortheabodeth.threads.AudioThread;
import main.java.questfortheabodeth.threads.ExpandingWave;
import main.java.questfortheabodeth.threads.RoomLoader;
import main.java.questfortheabodeth.weapons.*;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;
import org.jsfml.window.event.MouseEvent;

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
    private int endX, endY;
    private RoomLoader roomLoader;
    private SimpleBooleanProperty gameWon = new SimpleBooleanProperty(false);

    private Player player;
    private Door doorInRange = null;

    private MiniMap miniMap;
    private HealthBar healthBar;
    private WeaponWheel weaponWheel;
    private AmmoCount ammoCount;
    private HudElements hud;
    private MeleeRange meleeWave = null;

    private CopyOnWriteArraySet<Movable> movables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Drawable> drawables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Collidable> collidables = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Bullet> bullets = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Enemy> enemies = new CopyOnWriteArraySet<>();
    private CopyOnWriteArraySet<Interactable> interactables = new CopyOnWriteArraySet<>();


    public Game(RenderWindow window)
    {
        Settings.LOADED_IMAGES.clear(); //still needed?
        this.window = window;
        //loading screen thread

        this.window.clear();
        this.gameRunning = true;
        this.player = new Player(window);
        collidables.add(this.player);

        gameWon.addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                gameRunning = false;
                PlayerWinMenu winMenu = new PlayerWinMenu(window, Settings.GAME_TIME.getFormattedTime());
                winMenu.displayMenu();
            }
        });

        // Read the CSV file
        FileOperator ops = new FileOperator("res/assets/CSVs/roomLayout.csv");
        ArrayList<String> file = ops.readToList();
        int rows = file.size();
        int cols = file.get(0).split(",").length;
        rooms = new Room[rows][cols];

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
                if (roomCode == 3) {
                    endX = j;
                    endY = i;
                }
            }
        }

        if (roomRow < 0 || roomCol < 0) {
            throw new IllegalStateException("Player has no start room");
        }

        miniMap = new MiniMap(rows, cols, roomRow, roomCol, endX, endY);
        healthBar = new HealthBar(player);
        weaponWheel = new WeaponWheel(player.getMeleeWeapon(), player.getCurrentOneHandedWeapon(), player.getCurrentTwoHandedWeapon());
        ammoCount = new AmmoCount(player.ammoProperty());
        hud = new HudElements(player, healthBar, miniMap, weaponWheel, ammoCount);


        roomLoader = new RoomLoader(
                rooms,
                roomRow,
                roomCol,
                movables,
                drawables,
                collidables,
                enemies,
                interactables,
                player,
                window
        );

        new Thread(Settings.GAME_TIME).start();
    }

    public void load()
    {
        int loadingCogAngle = 0;

        Text storyText = new Text("Word has it that the ancient rellic, The Abodeth, lies deep within a hidden Egyptian pyramid.", Settings.ARIAL_FONT, 45);
        Text storyText2 = new Text("As an archaeologist and adventurer, you arm yourself with a pistol and delve into the darkness,", Settings.ARIAL_FONT, 45);
        Text storyText3 = new Text("determined to retrieve the rellic at any cost. As you enter the ancient tomb", Settings.ARIAL_FONT, 45);
        Text storyText4 = new Text(" you hear something crawl from within the shadows...", Settings.ARIAL_FONT, 45);
        Text continueText = new Text("Press any button to continue", Settings.ARIAL_FONT, 50);
        storyText.setPosition(200,200);
        storyText2.setPosition(180,300);
        storyText3.setPosition(330,400);
        storyText4.setPosition(530,500);
        continueText.setPosition(700, 900);
        Image loadingCog = new Image(70, 1000, "res/assets/loadingScreenCog.png");
        Image background = new Image(0, 0, "res/assets/menus/loadingScreenBackground.png");

        roomLoader.start();
        boolean waiter = true;
        while (roomLoader.isAlive() || waiter) {
            // Update the loading screen
            window.clear();
            if (loadingCogAngle >= 360) {
                loadingCogAngle = -1;
            }
            loadingCogAngle++;
            loadingCog.setRotation(loadingCogAngle);
            window.draw(background);
            if (roomLoader.isAlive()) {
                window.draw(loadingCog);
            } else {
                window.draw(continueText);
            }
            window.draw(storyText);
            window.draw(storyText2);
            window.draw(storyText3);
            window.draw(storyText4);
            window.display();

            for (Event e : window.pollEvents()) {
                Helper.checkCloseEvents(e, window);
                if (e.type == Event.Type.KEY_PRESSED) {
                    waiter = false;
                }
            }
        }

        Helper.printMatrix(rooms);
        currentRoom = rooms[roomRow][roomCol];
        Settings.BACKGROUND_AUDIO_PLAYING = false;
        String audioPath = currentRoom.getRoomName();
        //System.out.println("Audio Path: "+audioPath);
        if (Settings.MUSIC_ON && System.currentTimeMillis() - currentRoom.getLastAudioTrigger() >= Helper.getLengthOfAudioFile(audioPath)) {
            Settings.BACKGROUND_AUDIO_PLAYING = true;
            Helper.playAudio(audioPath);
            new AudioThread(audioPath);
            currentRoom.setLastAudioTrigger(System.currentTimeMillis());
        }

        currentRoom.runThreads();
    }

    public void run()
    {
        load();

        //kill loading screen thread
        int clocker = 0;
        Button time = new Button(120, 40, (Settings.WINDOW_WIDTH / 2) - 60, 10, "0");
        time.setTextXOffset(8);

        while (gameRunning && player.isCharacterAlive()) {
            time.setText(Settings.GAME_TIME.getFormattedTime());
            if (Settings.CROSSHAIR_VISIBLE) {
                window.setMouseCursorVisible(false);
            } else {
                window.setMouseCursorVisible(true);
            }
            if (Settings.MUSIC_ON && !Settings.BACKGROUND_AUDIO_PLAYING) {
                Settings.BACKGROUND_AUDIO_PLAYING = true;
                Helper.playAudio(currentRoom.getRoomName());
            }

            System.out.println("Clearing window");
            window.clear();
            // Draw the room
            System.out.println("Drawing room");
            window.draw(currentRoom);
            System.out.println("Drawing player");
            window.draw(player);
            System.out.println("Drawing drawable");
            drawables.forEach(window::draw);
            System.out.println("Drawing hud");
            window.draw(hud);
            if (meleeWave != null && meleeWave.isVisible()) {
                window.draw(meleeWave);
            }
            window.draw(time);
            System.out.println("Displaying window");
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
                                player.getCurrentWeapon() != null &&
                                (System.currentTimeMillis() - player.getLastTimeAttack()) >= player.getCurrentWeapon().getFireRate()
                ) {
                    if (!(player.getCurrentWeapon() instanceof Melee)) {
                        // The player character has fired a bullet
                        if (player.getFacingDirection() == Character.Facing.LEFT && e.asMouseEvent().position.x > player.getX()) {
                            continue;
                        } else if (player.getFacingDirection() == Character.Facing.RIGHT && e.asMouseEvent().position.x < player.getX()) {
                            continue;
                        }
                        player.setLastTimeAttack(System.currentTimeMillis());
                        if (0 < player.ammoProperty().getValue()) {
                            int max = player.getCurrentWeapon().getName().equals("shotgun") ? 3 : 1;
                            int[] angles = {0, -6, 6};
                            for (int i = 0; i < max; i++) {
                                Bullet b = new Bullet(
                                        (int) player.getPlayerCenter().x,
                                        (int) player.getPlayerCenter().y,
                                        Helper.getAngleBetweenPoints(new Vector2i(player.getVectorPosition()), e.asMouseEvent().position) + angles[i],
                                        player.getCurrentWeapon().getDamageDealt(),
                                        false,
                                        "bullet"
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

                        meleeWave = new MeleeRange(new Vector2f(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2), player);
                        new ExpandingWave(meleeWave).start();
                    }
                    if (Settings.SOUND_EFFECTS_ON && Helper.getTypeOfWeapon(player.getCurrentWeapon().getName()) == "Melee") {
                        Helper.playAudio(player.getCurrentWeapon().getName());
                    } else if (Settings.SOUND_EFFECTS_ON && player.getAmmo() > 0) {
                        Helper.playAudio(player.getCurrentWeapon().getName());
                    }
                } else if (e.type == Event.Type.KEY_PRESSED) {
                    if (e.asKeyEvent().key == Keyboard.Key.NUM1) {
                        if (player.switchWeapon(1)) {
                            weaponWheel.selectWeapon(player.getCurrentWeapon());
                            ammoCount.switchAmmo(player.getAmmoCount(player.getCurrentWeapon().getName()));
                        }
                    } else if (e.asKeyEvent().key == Keyboard.Key.NUM2) {
                        if (player.switchWeapon(2)) {
                            weaponWheel.selectWeapon(player.getCurrentWeapon());
                            ammoCount.switchAmmo(player.getAmmoCount(player.getCurrentWeapon().getName()));
                        }
                    } else if (e.asKeyEvent().key == Keyboard.Key.NUM3) {
                        if (player.switchWeapon(3)) {
                            weaponWheel.selectWeapon(player.getCurrentWeapon());
                            ammoCount.switchAmmo(player.getAmmoCount(player.getCurrentWeapon().getName()));
                        }
                    } else if (e.asKeyEvent().key == Keyboard.Key.E) {
                        // Player attempted to go through a door
                        if (doorInRange != null) {
                            // Go through the door
                            System.out.println("Going through the door " + doorInRange);
                            switchRoom(doorInRange.getLinkedDoor());
                            if (Settings.MUSIC_ON) {
                                Helper.stopAllAudio();
                                Settings.BACKGROUND_AUDIO_PLAYING = true;
                                Helper.playAudio(currentRoom.getRoomName());
                            }
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
            if (c instanceof TheAbodeth) {
                int overlap = Helper.checkOverlap(player, c);
                if (0 < overlap) {
                    gameWon.set(true);
                }
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
                    if (c instanceof HealthBoost && player.getHealth() >= 100) {
                        break;
                    } else {
                        ((Powerup) c).applyBuff(player);
                    }
                    drawables.remove(c);
                    collidables.remove(c);
                }
            }

            if (c instanceof Enemy) {
                int overlap = Helper.checkOverlap(player, c);
                if (0 < overlap) {
                    if (System.currentTimeMillis() - ((Enemy)c).getLastTimeAttack() >= ((Enemy) c).getAttackSpeed()) {
                        player.hit(((Enemy) c).getAttackPower());
                        ((Enemy)c).setLastTimeAttack(System.currentTimeMillis());
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
                    if (c instanceof Enemy && !b.isHurtsPlayer()) {
                        ((Enemy) c).decreaseHealth(player.getCurrentWeapon().getDamageDealt() + player.getAdditionalDamage());
                        if (!((Enemy) c).getEnemyName().equals("edemy")) {
                            new Thread((Character) c).start(); //pauses enemy movement
                        }
                    }
                    if (c instanceof Player && b.isHurtsPlayer()) {
                        ((Player) c).decreaseHealth(b.getDamage());
                    } else if (c instanceof Player) {
                        continue;
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
                        if (player.getCurrentWeapon() != null) {
                            if ((player.getCurrentWeapon() instanceof Melee && Helper.getTypeOfWeapon(((WeaponPickup) i).getName()).equals("Melee")) ||
                                    (player.getCurrentWeapon() instanceof OneHandedWeapon && Helper.getTypeOfWeapon(((WeaponPickup) i).getName()).equals("OneHandedWeapon")) ||
                                    (player.getCurrentWeapon() instanceof TwoHandedWeapon && Helper.getTypeOfWeapon(((WeaponPickup) i).getName()).equals("TwoHandedWeapon"))

                            ) {
                                WeaponPickup droppedWeapon = new WeaponPickup((int) player.getX() + (int) player.getWidth() / 2, (int) player.getY() + (int) player.getHeight() / 2, "res/assets/weapons/" + player.getCurrentWeapon().getName() + ".png", player.getCurrentWeapon().getName(), 0);
                                collidables.add(droppedWeapon);
                                interactables.add(droppedWeapon);
                                drawables.add(droppedWeapon);
                            }
                        }

                        weaponWheel.setWeapon(player.pickUpWeapon((WeaponPickup) i));
                        weaponWheel.selectWeapon(player.pickUpWeapon((WeaponPickup) i));
                        player.setWeaponImage(((WeaponPickup) i).getName());
                        ammoCount.switchAmmo(player.getAmmoCount(player.getCurrentWeapon().getName()));
                        ((WeaponPickup) i).remove();
                    } else if (player.hasWeapon(((WeaponPickup) i).getName())) {
                        // the player already has the weapon so add ammo
                        weaponWheel.setWeapon(player.pickUpWeapon((WeaponPickup) i));
                        ((WeaponPickup) i).remove();
                    } else {
                        // Player does not have the weapon and does not want it
                    }
                } else if (i instanceof Door) {
                    doorInRange = (Door) i;
                    localDoorRange = true;
                } else if (i instanceof TrapZone) {
                    if (System.currentTimeMillis() - ((ShootingArrows) i).getLastTimeTriggered() >= ((ShootingArrows) i).getFireRate()) {
                        ((TrapZone) i).trigger(movables, collidables, drawables, bullets, player);
                        ((ShootingArrows) i).setLastTimeTriggered(System.currentTimeMillis());
                    }
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
        Settings.BACKGROUND_AUDIO_PLAYING = false;
        String audioPath = currentRoom.getRoomName();
        if (Settings.MUSIC_ON && System.currentTimeMillis() - currentRoom.getLastAudioTrigger() >= Helper.getLengthOfAudioFile(audioPath)) {
            Settings.BACKGROUND_AUDIO_PLAYING = true;
            Helper.playAudio(audioPath);
            new AudioThread(audioPath);
            currentRoom.setLastAudioTrigger(System.currentTimeMillis());
        }
        collidables.clear();
        drawables.clear();
        enemies.clear();
        movables.clear();
        interactables.clear();
        bullets.clear();

        collidables.addAll(currentRoom.getCollidables());

        for (WeaponPickup w : currentRoom.getWeapons()) {
            interactables.add(w);
            drawables.add(w);
        }

        for (Enemy e : currentRoom.getEnemies()) {
            if (e instanceof Boss) {
                ((Boss) e).setLists(drawables, collidables);
            }
            e.setPlayer(player);
            movables.add(e);
            enemies.add(e);
            drawables.add(e);
        }


        for (Pickup p : currentRoom.getPickups()) {
            collidables.add(p);
            drawables.add(p);
        }

        collidables.add(player);
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

    /*public boolean saveGameScreenshot()
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
    }*/

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

        currentRoom.stopThreads();
        currentRoom = rooms[roomRow][roomCol];
        currentRoom.runThreads();

        // Test to see if this is the end room
        if (!currentRoom.getDoors()[0] && !currentRoom.getDoors()[1] && !currentRoom.getDoors()[2] && !currentRoom.getDoors()[3]) {
            player.setPosition(
                    200,
                    (int)(Settings.WINDOW_HEIGHT / 2 - (player.getWidth() / 2))
            );
        }
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
        Helper.stopAllAudio(); //ensures room audio is stopped before new room is loaded so music doesn't overlay
        scanRoom();
    }
}
