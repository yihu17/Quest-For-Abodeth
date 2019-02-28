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

/**
 * The main game class. This class is responsible for all of the main
 * games logic such as moving the players and enemies.
 */
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

    /**
     * Creates a new instance of the game. This sets ups the window and initialises the player
     * and map of rooms. Various bindings are set up here to watch for game changing events such
     * as the player winning, dying, pausing or quitting.
     *
     * @param window
     */
    public Game(RenderWindow window)
    {
        Settings.LOADED_IMAGES.clear(); //still needed?
        this.window = window;
        //loading screen thread

        this.window.clear();
        this.gameRunning = true;
        this.player = new Player(window);
        collidables.add(this.player);

        // Bind to the game won property so we can launch the player wins menu
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

        // Read the CSV file of all the rooms
        FileOperator ops = new FileOperator("res/assets/CSVs/roomLayout.csv");
        ArrayList<String> file = ops.readToList();
        // Create a 2D array to hold all the rooms
        int rows = file.size();
        int cols = file.get(0).split(",").length;
        rooms = new Room[rows][cols];

        /*
         * Create a dummy version of all the rooms first.
         * This is done because before all the rooms are made properly
         * the door scanning must take place. This is because the doors
         * are generated using other values in the array adn if the array
         * hasn't been fully populated yet, logical errors occur everywhere
         */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int roomCode = Integer.parseInt(file.get(i).split(",")[j]);
                if (roomCode == 0) {
                    rooms[i][j] = null;
                } else {
                    rooms[i][j] = new Room(roomCode * -1, false, false, false, false);
                }

                // Keep track of the start room
                if (roomCode == 1) {
                    roomCol = j;
                    roomRow = i;
                }

                // Keep track of the end room
                if (roomCode == 3) {
                    endX = j;
                    endY = i;
                }
            }
        }

        // If there is no start room, something has gone wrong
        if (roomRow < 0 || roomCol < 0) {
            throw new IllegalStateException("Player has no start room");
        }

        // Create all the elements required for the players HUD
        miniMap = new MiniMap(rows, cols, roomRow, roomCol, endX, endY);
        healthBar = new HealthBar(player);
        weaponWheel = new WeaponWheel(player.getMeleeWeapon(), player.getCurrentOneHandedWeapon(), player.getCurrentTwoHandedWeapon());
        ammoCount = new AmmoCount(player.ammoProperty());
        hud = new HudElements(player, healthBar, miniMap, weaponWheel, ammoCount);

        // Make a room loader thread but don't start it yet
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
    }

    /**
     * While the room loader thread is running (takes a long time to generate all the rooms)
     * display a loading screen to the player with a brief intro to the game. Once the game has
     * loaded fully the player can press a key to start the game
     */
    public void load()
    {
        int loadingCogAngle = 0;

        // Make all the relevant loading screen objects
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

        // Start loading rooms
        roomLoader.start();
        boolean waiter = true;

        // As long as the thread is still alive or the player is waiting
        // keep showing the loading screen
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

            // Handle a users key press
            for (Event e : window.pollEvents()) {
                Helper.checkCloseEvents(e, window);
                if (e.type == Event.Type.KEY_PRESSED) {
                    waiter = false;
                }
            }
        }

        // Do some preliminary setup from the scanRoom function
        // This is because the room thread does most but not all of
        // this function
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

        // Start the rooms animations
        currentRoom.runThreads();
        // Start the game timer
        new Thread(Settings.GAME_TIME).start();
    }

    /**
     * The main game function. When running, most of the games executing will
     * be called or run from this function.
     */
    public void run()
    {
        // Run the loading screen
        load();

        int clocker = 0;
        Button time = new Button(120, 40, (Settings.WINDOW_WIDTH / 2) - 60, 10, "0");
        time.setTextXOffset(8);

        // Keep running the game as long as it hasn't been won or the is not dead
        while (gameRunning && player.isCharacterAlive()) {
            time.setText(Settings.GAME_TIME.getFormattedTime());

            // Are we still showing the crosshair
            if (Settings.CROSSHAIR_VISIBLE) {
                window.setMouseCursorVisible(false);
            } else {
                window.setMouseCursorVisible(true);
            }

            // Are we still playin music
            if (Settings.MUSIC_ON && !Settings.BACKGROUND_AUDIO_PLAYING) {
                Settings.BACKGROUND_AUDIO_PLAYING = true;
                Helper.playAudio(currentRoom.getRoomName());
            }

            window.clear();
            // Draw the room
            window.draw(currentRoom);
            window.draw(player);
            drawables.forEach(window::draw);
            window.draw(hud);

            // Are we drawing the melee shockwave around the player
            if (meleeWave != null && meleeWave.isVisible()) {
                window.draw(meleeWave);
            }
            window.draw(time);
            window.display();

            // Move every single movable object (enemy movements, bullets etc.)
            // Once they have moved check to ensure they are still in the bounds of the window
            this.moveMovables();

            // Check for windowevents
            for (Event e : window.pollEvents()) {
                Helper.checkCloseEvents(e, window);

                // The player is attempting to fire their weapon
                if (
                        e.type == MouseEvent.Type.MOUSE_BUTTON_PRESSED &&
                                Mouse.isButtonPressed(Mouse.Button.LEFT) &&
                                player.getCurrentWeapon() != null &&
                                (System.currentTimeMillis() - player.getLastTimeAttack()) >= player.getCurrentWeapon().getFireRate()
                ) {
                    if (!(player.getCurrentWeapon() instanceof Melee)) {
                        // The player character has fired a bullet, check they are firing in the correct direction
                        /*
                        if (player.getFacingDirection() == Character.Facing.LEFT && e.asMouseEvent().position.x > player.getX()) {
                            continue;
                        } else if (player.getFacingDirection() == Character.Facing.RIGHT && e.asMouseEvent().position.x < player.getX()) {
                            continue;
                        }
                        */
                        // Update the last time they attacked
                        player.setLastTimeAttack(System.currentTimeMillis());

                        // If the player still has ammo, let them fire a bullet
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
                        // The player used their melee weapon
                        // Create a larger than normal FloatRect for the player to act as a hit range
                        FloatRect meleeRange = new FloatRect(
                                player.getX() - player.getWidth(),
                                player.getY() - player.getHeight(),
                                3 * player.getWidth(),
                                3 * player.getHeight()
                        );
                        // Damage all enemies in the area
                        enemies.forEach(enemy -> enemy.decreaseHealth(
                                0 < Helper.checkOverlap(enemy, meleeRange) ? player.getCurrentWeapon().getDamageDealt() : 0
                        ));

                        // Make a new melee shockwave to illustrate the player using their weapon
                        meleeWave = new MeleeRange(new Vector2f(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2), player);
                        new ExpandingWave(meleeWave).start();
                    }

                    // Play the relevant sound effect for the wepaon they just used
                    if (Settings.SOUND_EFFECTS_ON && Helper.getTypeOfWeapon(player.getCurrentWeapon().getName()).equals("Melee")) {
                        Helper.playAudio(player.getCurrentWeapon().getName());
                    } else if (Settings.SOUND_EFFECTS_ON && player.getAmmo() > 0) {
                        Helper.playAudio(player.getCurrentWeapon().getName());
                    }
                } else if (e.type == Event.Type.KEY_PRESSED) {
                    // Check for key presses from the user
                    if (e.asKeyEvent().key == Keyboard.Key.NUM1) {
                        // Switching to a melee weapon
                        if (player.switchWeapon(1)) {
                            weaponWheel.selectWeapon(player.getCurrentWeapon());
                            ammoCount.switchAmmo(player.getAmmoCount(player.getCurrentWeapon().getName()));
                        }
                    } else if (e.asKeyEvent().key == Keyboard.Key.NUM2) {
                        // Switchin to a OneHandedWeapon
                        if (player.switchWeapon(2)) {
                            weaponWheel.selectWeapon(player.getCurrentWeapon());
                            ammoCount.switchAmmo(player.getAmmoCount(player.getCurrentWeapon().getName()));
                        }
                    } else if (e.asKeyEvent().key == Keyboard.Key.NUM3) {
                        // Switching to a TwohandedWeapon
                        if (player.switchWeapon(3)) {
                            weaponWheel.selectWeapon(player.getCurrentWeapon());
                            ammoCount.switchAmmo(player.getAmmoCount(player.getCurrentWeapon().getName()));
                        }
                    } else if (e.asKeyEvent().key == Keyboard.Key.E) {
                        // Player attempted to go through a door
                        if (doorInRange != null) {
                            // Go through the door so switch rooms
                            switchRoom(doorInRange.getLinkedDoor());

                            // Change the currently playing music track to reflect the new room
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

            // If the user pressed escape open up the in game menu
            // Clocker is require to prevent flickering
            if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE) && clocker > 10) { //used to prevent screen flickering
                openInGameMenu();
                clocker = 0;
            }

            // Update the games state
            int moveValues = runPlayerCollisions();
            runBulletCollisions();
            runEnemyCollisions();
            runPlayerInteracts();
            runEnemyInteracts();

            // The player is attempting to move so check if they can before moving the character
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

        // If the player died open up the died menu
        if (!player.isCharacterAlive()) {
            openPlayerDiedMenu();
        }

        // Reset the game time
        Settings.GAME_TIME = null;

    }

    /**
     * Checks all the players collisions against in game objects
     * @return (int) Directions the player can move in
     */
    private int runPlayerCollisions()
    {
        int moveValues = 0;
        HashSet<Integer> playerCanMove = new HashSet<>();
        for (Collidable c : collidables) {
            // Don't check against the player or player bullets
            if (c instanceof Player || c instanceof Bullet) {
                continue;
            }
            // If the collide with the abodeth the game is over
            if (c instanceof TheAbodeth) {
                int overlap = Helper.checkOverlap(player, c);
                if (0 < overlap) {
                    gameWon.set(true);
                }
            }
            // If we are checking an enironment object make sure
            // they aren't overlapping it
            if (c instanceof Environment) {
                int overlap = Helper.checkOverlap(player, c);
                if (0 < overlap) {
                    playerCanMove.add(overlap);
                }
            }

            // If they just hit a powerup, pick it up and apply the buff
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

            // If they hit an enemy, damage the player
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

        // Sum up the player movements
        for (Integer i : playerCanMove) {
            moveValues += i;
        }

        // Return the directions in which the player can move
        return moveValues;
    }

    /**
     * Runs the collision for all the bullets in the game.
     */
    private void runBulletCollisions()
    {
        for (Bullet b : bullets) {
            for (Collidable c : collidables) {
                // Bullets dno't collide with other bullets, powerups or weapon pickups
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

                    // The bullet hit something so remove it from play
                    b.setX(2 * Settings.WINDOW_WIDTH);
                    b.setY(2 * Settings.WINDOW_HEIGHT);
                }
            }
        }
    }

    /**
     * Check all the collision for enemies against collidable
     * objects
     */
    private void runEnemyCollisions()
    {
        for (Enemy e : enemies) {
            HashSet<Integer> values = new HashSet<>();
            for (Collidable c : collidables) {
                // Enemies don't collide with other enemies, weapon pickups or powerups
                if (c instanceof Enemy || c instanceof WeaponPickup || c instanceof Powerup) {
                    continue;
                }
                values.add(Helper.checkOverlap(e, c));
            }

            int value = 0;
            for (Integer i : values) {
                value += i;
            }

            // Set the move value for the enemy
            e.setMoveValue(value);
        }
    }

    /**
     * Run the players interacts e.g. what are they currently interacting with
     * and do we need to do anything about it
     */
    private void runPlayerInteracts()
    {
        HashSet<Class<? extends Interactable>> currentInteracts = new HashSet<>();
        boolean localDoorRange = false;
        for (Interactable i : interactables) {
            int overlap;
            // If we are checking a trapzone, use the trapzone increased "range"
            if (i instanceof TrapZone) {
                overlap = Helper.checkOverlap(player, ((TrapZone) i).getGlobalBounds());
            } else {
                overlap = Helper.checkOverlap(player, i);
            }
            if (0 < overlap) {
                // Player is on top of something
                // Check for weapon pickups
                if (i instanceof WeaponPickup) {
                    // If the user is pressing E and they don't have the weapon, pick it up
                    if (Keyboard.isKeyPressed(Keyboard.Key.E) && !player.hasWeapon(((WeaponPickup) i).getName())) {
                        if (player.getCurrentWeapon() != null) {
                            if ((player.getCurrentWeapon() instanceof Melee && Helper.getTypeOfWeapon(((WeaponPickup) i).getName()).equals("Melee")) ||
                                    (player.getCurrentWeapon() instanceof OneHandedWeapon && Helper.getTypeOfWeapon(((WeaponPickup) i).getName()).equals("OneHandedWeapon")) ||
                                    (player.getCurrentWeapon() instanceof TwoHandedWeapon && Helper.getTypeOfWeapon(((WeaponPickup) i).getName()).equals("TwoHandedWeapon"))

                            ) {
                                // If the player is swapping a weapon, put the old one down
                                WeaponPickup droppedWeapon = new WeaponPickup((int) player.getX() + (int) player.getWidth() / 2, (int) player.getY() + (int) player.getHeight() / 2, "res/assets/weapons/" + player.getCurrentWeapon().getName() + ".png", player.getCurrentWeapon().getName(), 0);
                                collidables.add(droppedWeapon);
                                interactables.add(droppedWeapon);
                                drawables.add(droppedWeapon);
                            }
                        }

                        // Update the weapon wheel to reflect player inventory changes
                        weaponWheel.setWeapon(player.pickUpWeapon((WeaponPickup) i));
                        weaponWheel.selectWeapon(player.pickUpWeapon((WeaponPickup) i));
                        player.setWeaponImage(((WeaponPickup) i).getName());
                        ammoCount.switchAmmo(player.getAmmoCount(player.getCurrentWeapon().getName()));
                        ((WeaponPickup) i).remove();
                    } else if (player.hasWeapon(((WeaponPickup) i).getName())) {
                        // The player already has the weapon so add ammo
                        weaponWheel.setWeapon(player.pickUpWeapon((WeaponPickup) i));
                        ((WeaponPickup) i).remove();
                    } else {
                        // Player does not have the weapon and does not want it
                    }
                } else if (i instanceof Door) {
                    // Player is near a door so keep track of it
                    doorInRange = (Door) i;
                    localDoorRange = true;
                } else if (i instanceof TrapZone) {
                    // If they are in range of a trapzone, trigger the trap
                    if (System.currentTimeMillis() - ((ShootingArrows) i).getLastTimeTriggered() >= ((ShootingArrows) i).getFireRate()) {
                        ((TrapZone) i).trigger(movables, collidables, drawables, bullets, player);
                        ((ShootingArrows) i).setLastTimeTriggered(System.currentTimeMillis());
                    }
                } else {
                    // Its something else so jsut run the interact function attached to it
                    i.interact(player);
                    currentInteracts.add(i.getClass());
                }
            }
        }

        // The door is not in range anymore so stop tracking it
        if (!localDoorRange) {
            doorInRange = null;
        }

        // Update the players currently applied interacts
        player.resetInteracts(currentInteracts);
    }

    /**
     * Run the enemies interacts e.g. what are they currently interacting with
     * and do we need to do anything about it
     */
    private void runEnemyInteracts()
    {
        for (Enemy e : enemies) {
            HashSet<Class<? extends Interactable>> currentInteracts = new HashSet<>();
            for (Interactable i : interactables) {
                // Ignore most of the normal interacts
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

            // Update enemy interacts
            e.resetInteracts(currentInteracts);
        }
    }

    /**
     * Called when the player moves between rooms. This method
     * updates all the lists of objects currently visible and
     * in play.
     */
    private void scanRoom()
    {
        // Change over the audio
        Settings.BACKGROUND_AUDIO_PLAYING = false;
        String audioPath = currentRoom.getRoomName();
        if (Settings.MUSIC_ON && System.currentTimeMillis() - currentRoom.getLastAudioTrigger() >= Helper.getLengthOfAudioFile(audioPath)) {
            Settings.BACKGROUND_AUDIO_PLAYING = true;
            Helper.playAudio(audioPath);
            new AudioThread(audioPath);
            currentRoom.setLastAudioTrigger(System.currentTimeMillis());
        }

        // Clear all the lists for the previous room
        collidables.clear();
        drawables.clear();
        enemies.clear();
        movables.clear();
        interactables.clear();
        bullets.clear();

        // Add all the objects from the next room into the current lists
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

    /**
     * Move all the movable objects on the screen. If an object if outside of
     * the current visible coordinate space we need to remove it from the
     * lists of objects. This is done by removing it from the lsits and letting
     * the garbage collector sort it out
     */
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

    /**
     * Called when the player opens up the in game menu by pressing escape.
     * This menu has options to quit or continue the game
     */
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

    /**
     * Open up the player died menu and show the time that the player survived in the temple for
     * Shows a button to take them back to the main menu
     */
    private void openPlayerDiedMenu()
    {
        PlayerDiedMenu playerDiedMenu = new PlayerDiedMenu(window, Settings.GAME_TIME.getFormattedTime());
        playerDiedMenu.displayMenu();
        Button b = playerDiedMenu.getChosenButton();
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

    /**
     * The player has moved between rooms so we need to update the minimap
     * and update the pointer to the current room
     * @param direction (int) Direction the player moved in
     */
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

        // Stop animating the old room
        currentRoom.stopThreads();
        // Switch the rooms over
        currentRoom = rooms[roomRow][roomCol];
        // Start animating the new room
        currentRoom.runThreads();

        // Test to see if this is the end room
        if (!currentRoom.getDoors()[0] && !currentRoom.getDoors()[1] && !currentRoom.getDoors()[2] && !currentRoom.getDoors()[3]) {
            player.setPosition(
                    200,
                    (int)(Settings.WINDOW_HEIGHT / 2 - (player.getWidth() / 2))
            );
        }

        // Figure out which door the player came through and place them just in-front of it
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

        // Stop the current rooms audio
        Helper.stopAllAudio();

        // Scan the new room and update lists
        scanRoom();
    }
}
