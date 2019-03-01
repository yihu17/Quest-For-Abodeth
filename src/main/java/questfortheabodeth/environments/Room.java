package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.FileOperator;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Boss;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.environments.interactables.Door;
import main.java.questfortheabodeth.environments.traps.*;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.powerups.*;
import main.java.questfortheabodeth.threads.ImageSwitchThread;
import main.java.questfortheabodeth.weapons.WeaponPickup;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that models a room object in the game. Every room contains all
 * the environment objects to draw and any enemies that are to be spawned in the room
 * <p>
 * TODO: Check the stopping of the animation threads when a room switch occurs
 */
public class Room implements Drawable
{
    private int type;
    private FileOperator roomFile;
    private ArrayList<ArrayList<String>> roomLayout = new ArrayList<>();
    private Environment[][] roomImages = new Environment[Settings.ROOM_DIVISION_ROWS][Settings.ROOM_DIVISION_COLUMNS];
    private boolean[] doors = new boolean[4];

    private ArrayList<int[]> enemyInfo = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();

    private ArrayList<int[]> pickupInfo = new ArrayList<>();
    private ArrayList<Pickup> pickups = new ArrayList<>();

    private ArrayList<int[]> weaponInfo = new ArrayList<>();
    private ArrayList<WeaponPickup> weapons = new ArrayList<>();

    private long lastAudioTrigger = 0;
    private String roomName;

    private ImageSwitchThread waterAnimationThread = new ImageSwitchThread("res/assets/environment/water.png", "res/assets/environment/waterB.png", 500);
    private ImageSwitchThread lavaAnimationThread = new ImageSwitchThread("res/assets/environment/lava.png", "res/assets/environment/lavaB.png", 500);
    private ImageSwitchThread quicksandAnimationThread = new ImageSwitchThread("res/assets/environment/quickSand.png", "res/assets/environment/quicksandB.png", 500);


    /**
     * Creates a new room of the specified type
     *
     * @param type  (int) Room type
     * @param up    (boolean) Is the a door at the top of the room
     * @param down  (boolean) Is the a door at the bottom of the room
     * @param left  (boolean) Is the a door at the left of the room
     * @param right (boolean) Is the a door at the right of the room
     */
    public Room(int type, boolean up, boolean down, boolean left, boolean right)
    {
        doors[0] = up;
        doors[1] = down;
        doors[2] = left;
        doors[3] = right;
        roomName = "roomDataNull";
        this.type = type;

        // A room type of 1 means it is the start room
        // A room type of 3 means it is the end room
        if (type == 1) {
            roomFile = new FileOperator("res/assets/CSVs/roomCSVs/roomDataA.csv");
            roomName = "roomDataA";
        } else if (type == 3) {
            roomFile = new FileOperator("res/assets/CSVs/roomCSVs/roomDataZ.csv");
            doors = new boolean[]{false, false, false, false};
            roomName = "roomDataJ";
        } else if (type < 0) {
            // Allows an array of rooms to be created initially without
            // having generate the room or load file.
            return;
        } else {
            String[] rooms = new String[]{"B","C","D","F","G","H","I","J"};
            int index = Settings.GENERATOR.nextInt(rooms.length);
            roomFile = new FileOperator("res/assets/CSVs/roomCSVs/roomData" + rooms[index] + ".csv");
            roomName = "roomData" + rooms[index];
        }

        // Load the room
        readRoomData();
        loadRoomImages();
        spawnEnemies();
        spawnPickups();
        spawnWeapons();

        // Now that the room has been generated make some doors
        if (doors[0]) {
            // Ensure there is a door at the top and that the blocks immediately below them
            // are normal environment tiles
            int topDoorStart = 19;
            roomImages[0][topDoorStart] = new Door(Settings.ROOM_DIVISION_SIZE * topDoorStart, 0, "res/assets/environment/leftDoor.png", -2);
            roomImages[0][topDoorStart + 1] = new Door(Settings.ROOM_DIVISION_SIZE * (topDoorStart + 1), 0, "res/assets/environment/rightDoor.png", -2);

            roomImages[1][topDoorStart] = roomImages[1][topDoorStart] instanceof Collidable ? new Environment(
                    Settings.ROOM_DIVISION_SIZE * topDoorStart,
                    Settings.ROOM_DIVISION_SIZE,
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            ) : roomImages[1][topDoorStart];
            roomImages[1][topDoorStart + 1] = roomImages[1][topDoorStart + 1] instanceof Collidable ? new Environment(
                    Settings.ROOM_DIVISION_SIZE * (topDoorStart + 1),
                    Settings.ROOM_DIVISION_SIZE,
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            ) : roomImages[1][topDoorStart + 1];
        }
        if (doors[1]) {
            // Ensure there is a door at the bottom and that the blocks immediately above of them
            // are normal environment tiles
            int bottomDoorStart = 19;
            roomImages[Settings.ROOM_DIVISION_ROWS - 1][bottomDoorStart] = new Door(Settings.ROOM_DIVISION_SIZE * bottomDoorStart, Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_ROWS - 1), "res/assets/environment/leftDoor.png", -4);
            roomImages[Settings.ROOM_DIVISION_ROWS - 1][bottomDoorStart + 1] = new Door(Settings.ROOM_DIVISION_SIZE * (bottomDoorStart + 1), Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_ROWS - 1), "res/assets/environment/rightDoor.png", -4);

            roomImages[Settings.ROOM_DIVISION_ROWS - 2][bottomDoorStart] = roomImages[Settings.ROOM_DIVISION_ROWS - 2][bottomDoorStart] instanceof Collidable ? new Environment(
                    Settings.ROOM_DIVISION_SIZE * bottomDoorStart,
                    Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_ROWS - 2),
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            ) : roomImages[Settings.ROOM_DIVISION_ROWS - 2][bottomDoorStart];
            roomImages[Settings.ROOM_DIVISION_ROWS - 2][bottomDoorStart + 1] = roomImages[Settings.ROOM_DIVISION_ROWS - 2][bottomDoorStart + 1] instanceof Collidable ? new Environment(
                    Settings.ROOM_DIVISION_SIZE * (bottomDoorStart + 1),
                    Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_ROWS - 2),
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            ) : roomImages[Settings.ROOM_DIVISION_ROWS - 2][bottomDoorStart + 1];
        }
        if (doors[2]) {
            // Ensure there is a door at the left and that the blocks immediately right of them
            // are normal environment tiles
            int leftDoorStart = 8;
            roomImages[leftDoorStart][0] = new Door(0, Settings.ROOM_DIVISION_SIZE * leftDoorStart, "res/assets/environment/topDoor.png", -1);
            roomImages[leftDoorStart + 1][0] = new Door(0, Settings.ROOM_DIVISION_SIZE * (leftDoorStart + 1), "res/assets/environment/bottomDoor.png", -1);

            roomImages[leftDoorStart][1] = roomImages[leftDoorStart][1] instanceof Collidable ? new Environment(
                    Settings.ROOM_DIVISION_SIZE,
                    Settings.ROOM_DIVISION_SIZE * leftDoorStart,
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            ) : roomImages[leftDoorStart][1];
            roomImages[leftDoorStart + 1][1] = roomImages[leftDoorStart + 1][1] instanceof Collidable ? new Environment(
                    Settings.ROOM_DIVISION_SIZE,
                    Settings.ROOM_DIVISION_SIZE * (leftDoorStart + 1),
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            ) : roomImages[leftDoorStart + 1][1];
        }
        if (doors[3]) {
            // Ensure there is a door at the right and that the blocks immediately left of them
            // are normal environment tiles
            int rightDoorStart = 8;
            roomImages[rightDoorStart][Settings.ROOM_DIVISION_COLUMNS - 1] = new Door(Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_COLUMNS - 1), Settings.ROOM_DIVISION_SIZE * rightDoorStart, "res/assets/environment/topDoor.png", -3);
            roomImages[rightDoorStart + 1][Settings.ROOM_DIVISION_COLUMNS - 1] = new Door(Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_COLUMNS - 1), Settings.ROOM_DIVISION_SIZE * (rightDoorStart + 1), "res/assets/environment/bottomDoor.png", -3);

            roomImages[rightDoorStart][Settings.ROOM_DIVISION_COLUMNS - 2] =  roomImages[rightDoorStart][Settings.ROOM_DIVISION_COLUMNS - 2] instanceof Collidable ? new Environment(
                    Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_COLUMNS - 2),
                    Settings.ROOM_DIVISION_SIZE * rightDoorStart,
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            ) : roomImages[rightDoorStart][Settings.ROOM_DIVISION_COLUMNS - 2];
            roomImages[rightDoorStart + 1][Settings.ROOM_DIVISION_COLUMNS - 2] = roomImages[rightDoorStart + 1][Settings.ROOM_DIVISION_COLUMNS - 2] instanceof Collidable ? new Environment(
                    Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_COLUMNS - 2),
                    Settings.ROOM_DIVISION_SIZE * (rightDoorStart + 1),
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            ) : roomImages[rightDoorStart + 1][Settings.ROOM_DIVISION_COLUMNS - 2];
        }
    }

    /**
     * Returns a list of what doors there are in the room
     * The indexes are as follows:
     *     0: Top door
     *     1: Bottom door
     *     2: Left door
     *     3: Right door
     * @return
     */
    public boolean[] getDoors()
    {
        return doors;
    }

    /**
     * Returns the type of the room
     * The types are as follows:
     *     1: Start room
     *     2: Intermediate room
     *     3: End room
     * @return (int) This rooms tyoe
     */
    public int getType()
    {
        return this.type;
    }

    /**
     * Returns a list of all the collidable objects in this room
     * @see Collidable
     *
     * @return (ArrayList) List of Collidable objects
     */
    public ArrayList<Collidable> getCollidables()
    {
        ArrayList<Collidable> c = new ArrayList<>();
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            for (int j = 0; j < Settings.ROOM_DIVISION_COLUMNS; j++) {
                if (roomImages[i][j] instanceof Collidable && !(roomImages[i][j] instanceof Interactable)) {
                    // Add any object that is collidable and not interactable
                    c.add((Collidable) roomImages[i][j]);
                }
                if (roomImages[i][j] instanceof Door) {
                    /*
                     * If a door is found in the room, place two wall blocks directly behind it to prevent
                     * the player from walking over the door and off the screen
                     */
                    float x, y;
                    Door door = (Door) roomImages[i][j];
                    if (door.getLinkedDoor() == -1 || door.getLinkedDoor() == -3) {
                        x = door.getLinkedDoor() == -1 ? (door.getX() - Settings.ROOM_DIVISION_SIZE) : (door.getX() + Settings.ROOM_DIVISION_SIZE);
                        y = door.getY();
                    } else {
                        x = door.getX();
                        y = door.getLinkedDoor() == -2 ? (door.getY() - Settings.ROOM_DIVISION_SIZE) : (door.getY() + Settings.ROOM_DIVISION_SIZE);
                    }
                    c.add(new CollidableEnvironment(
                            (int) x,
                            (int) y,
                            "res/assets/environment/wall.png"
                    ));
                }
            }
        }

        // All enemies, pickups and weapons are seen as collidables
        c.addAll(enemies);
        c.addAll(pickups);
        c.addAll(weapons);
        return c;
    }

    /**
     * Returns a list of all the interactable objects in this room
     * @return (ArrayList) List of all interactables in the room
     */
    public ArrayList<Interactable> getInteractables()
    {
        ArrayList<Interactable> iList = new ArrayList<>();
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            for (int j = 0; j < Settings.ROOM_DIVISION_COLUMNS; j++) {
                if (roomImages[i][j] instanceof Interactable) {
                    iList.add((Interactable) roomImages[i][j]);
                }
            }
        }
        return iList;
    }

    /**
     * Provide a more informational view of the room when it is printed
     * @return (String) Room information
     */
    @Override
    public String toString()
    {
        return "<Room " + type + " with doors " + (doors[0] ? "top, " : "") + (doors[1] ? "bottom, " : "") + (doors[2] ? "left, " : "") +
                (doors[3] ? "right, " : "") + ">";
    }

    /**
     * Makes the room drawable. Method takes all the drawables in this main.java.questfortheabodeth.environments.Room class
     * and draw them all to the screen
     *
     * @param renderTarget (RenderTarget) The window to draw to
     * @param renderStates (RenderStates) Again, what are these
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            for (int j = 0; j < Settings.ROOM_DIVISION_COLUMNS; j++) {
                renderTarget.draw(roomImages[i][j]);
            }
        }
    }

    /**
     * Read the data from the rooms CSV file for this room
     */
    private void readRoomData()
    {
        readToLayout();
        int startOfData = Settings.WINDOW_HEIGHT / Settings.ROOM_DIVISION_SIZE;

        // Convert all the CSV lines about room meta data into integer arrays
        // readLine returns an array of Strings so convert it to a stream and convert each
        // element in the stream to an integer and write it back to an integer array
        int[] enemyTypes = Arrays.stream(roomFile.readLine(startOfData)).mapToInt(Integer::parseInt).toArray();
        int[] enemyQuantities = Arrays.stream(roomFile.readLine(startOfData + 1)).mapToInt(Integer::parseInt).toArray();
        int[] pickupTypes = Arrays.stream(roomFile.readLine(startOfData + 2)).mapToInt(Integer::parseInt).toArray();
        int[] pickupQuantities = Arrays.stream(roomFile.readLine(startOfData + 3)).mapToInt(Integer::parseInt).toArray();
        int[] weaponTypes = Arrays.stream(roomFile.readLine(startOfData + 4)).mapToInt(Integer::parseInt).toArray();
        int[] weaponQuantities = Arrays.stream(roomFile.readLine(startOfData + 5)).mapToInt(Integer::parseInt).toArray();

        // Parse out the enemy types and counts into a new array
        for (int i = 0; i < enemyTypes.length; i++) {
            enemyInfo.add(new int[]{enemyTypes[i], enemyQuantities[i]});
        }

        // Parse out the pickup types and counts into a new array
        for (int i = 0; i < pickupTypes.length; i++) {
            pickupInfo.add(new int[]{pickupTypes[i], pickupQuantities[i]});
        }

        // Parse out the weapon types and counts into a new array
        for (int i = 0; i < weaponTypes.length; i++) {
            weaponInfo.add(new int[]{weaponTypes[i], weaponQuantities[i]});
        }
    }

    /**
     * Reads the contents of the CSV file into an array of arrays
     */
    private void readToLayout()
    {
        // Reset the room layout
        roomLayout.clear();

        // Read in the map part of the CSV file
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            roomLayout.add(new ArrayList<>());
            String[] currentLine = roomFile.readLine(i);
            for (int j = 0; j < currentLine.length; j++) {
                roomLayout.get(i).add(currentLine[j]);
            }
        }
    }

    /**
     * Iterates over all the Environment objects in this room and loads the images into
     * a new 2D array of Environment objects
     */
    private void loadRoomImages()
    {
        int spacing = Settings.ROOM_DIVISION_SIZE;
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            for (int j = 0; j < Settings.ROOM_DIVISION_COLUMNS; j++) {

                // Parse the integer value of the block from the CSV file
                // and convert it into a filename
                String elementRead;
                try {
                    elementRead = Settings.CSV_KEYS.get(Integer.parseInt(roomLayout.get(i).get(j)));
                } catch (Exception e) {
                    elementRead = "wall";
                    e.printStackTrace();
                }

                String filePath = "res/assets/environment/" + elementRead + ".png";
                switch (elementRead) {
                    case "wall":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "floor":
                        int floorNum = Settings.GENERATOR.nextInt(4) + 1;
                        //System.out.println(floorNum);
                        roomImages[i][j] = new Environment(spacing * j, spacing * i, "res/assets/environment/floor/floor" + floorNum + ".png", false, false);
                        break;
                    case "leftDoor":
                        roomImages[i][j] = new Door(spacing * j, spacing * i, "res/assets/environment/door.png", -1);
                        break;
                    case "topDoor":
                        roomImages[i][j] = new Door(spacing * j, spacing * i, "res/assets/environment/door.png", -2);
                        break;
                    case "rightDoor":
                        roomImages[i][j] = new Door(spacing * j, spacing * i, "res/assets/environment/door.png", -3);
                        break;
                    case "lowerDoor":
                        roomImages[i][j] = new Door(spacing * j, spacing * i, "res/assets/environment/door.png", -4);
                        break;
                    case "water":
                        roomImages[i][j] = new Water(spacing * j, spacing * i, filePath);
                        waterAnimationThread.addTrap(roomImages[i][j]);
                        break;
                    case "quicksand":
                        roomImages[i][j] = new Quicksand(spacing * j, spacing * i, filePath);
                        quicksandAnimationThread.addTrap(roomImages[i][j]);
                        break;
                    case "spikeTrap":
                        roomImages[i][j] = new SpikeTrap(spacing * j, spacing * i, filePath);
                        break;
                    case "switchPuzzle":
                        roomImages[i][j] = new CollidableInteractableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "shootingArrowTrap":
                        roomImages[i][j] = new ShootingArrows(spacing * j, spacing * i, filePath);
                        break;
                    /*case "swingingAxeTrap":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
                        break;*/
                    case "lava":
                        roomImages[i][j] = new Lava(spacing * j, spacing * i, filePath);
                        lavaAnimationThread.addTrap(roomImages[i][j]);
                        break;
                    /*case "rollingBoulderTrap":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "fierySphinx":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "graveyard":
                        roomImages[i][j] = new Environment(spacing * j, spacing * i, filePath, false, false);
                        break;
                    case "crushingWalls":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
                        break;*/
                    default:
                        roomImages[i][j] = new Environment(spacing * j, spacing * i, "res/assets/environment/wall.png", false, false);
                        //throw new AssertionError("Unknown image" + elementRead);
                }
            }
        }
    }

    /**
     * Creates all the enemies from the list of enemies read from the CSV file
     */
    private void spawnEnemies()
    {
        for (int i = 0; i < enemyInfo.size(); i++) {
            // Convert the enemy nuber to a filename
            String enemyRead = Settings.CSV_KEYS.get(enemyInfo.get(i)[0]);
            String filePath = "res/assets/enemies/" + enemyRead + ".png";
            for (int y = 0; y < enemyInfo.get(i)[1]; y++) {
                // Create a random spawn location for enemies
                int[] generatedSpawnLocation = generateSpawnLocation();
                switch (enemyRead) {
                    case "zombie":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 2, "zombie", 600, 15));
                        break;
                    case "jackal":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 60, filePath, 4, "jackal", 500, 15));
                        break;
                    case "bat":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 40, filePath, 4, "bat", 400, 15));
                        break;
                    case "spider":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 10, filePath, 3, "spider", 400, 5));
                        break;
                    case "mummifiedSlave":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 2, "mummifiedSlave", 900, 20));
                        break;
                    case "giantSpider":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 140, filePath, 1, "giantSpider", 900, 20));
                        break;
                    case "giantZombie":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 200, filePath, 1, "giantZombie", 1200, 25));
                        break;
                    case "egyptianMummy":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 120, filePath, 2, "egyptianMummy", 750, 25));
                        break;
                    case "crocodile":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 160, filePath, 1, "crocodile", 1000, 25));
                        break;
                }
            }
        }
    }

    public Boss spawnBoss() {
        String fPath;
        if (Settings.DANK_VERSION) {
            fPath = "res/assets/enemies/supremeOverlord.png";
        } else {
            fPath = "res/assets/enemies/edemy.png";
        }
        Boss finalBoss = new Boss(1600, Settings.WINDOW_HEIGHT / 2 + 50, 1000, fPath, 1, "edemy", 400, 50);
        enemies.add(finalBoss);
        return finalBoss;
    }

    /**
     * Generate a random spawn location for the enemies
     * @return (int[]) Array of X and Y coordinates
     */
    private int[] generateSpawnLocation()
    {
        int[] spawnPositionGenerated = new int[2];

        // Create the initial spawn positions
        spawnPositionGenerated[0] = Settings.GENERATOR.nextInt(Settings.WINDOW_WIDTH) + Settings.ROOM_DIVISION_SIZE;
        spawnPositionGenerated[1] = Settings.GENERATOR.nextInt(Settings.WINDOW_HEIGHT) + Settings.ROOM_DIVISION_SIZE;

        // Keep generating positions until the position is valid
        while (!spawnLocationGeneratedIsValid(spawnPositionGenerated)) {
            spawnPositionGenerated[0] = Settings.GENERATOR.nextInt(Settings.WINDOW_WIDTH) + Settings.ROOM_DIVISION_SIZE;
            spawnPositionGenerated[1] = Settings.GENERATOR.nextInt(Settings.WINDOW_HEIGHT) + Settings.ROOM_DIVISION_SIZE;
        }

        return spawnPositionGenerated;
    }

    /**
     * Checks whether or not the given spawn position is valid or not.
     * Validity is checked against:
     *     - Overlapping part of the environment
     *     - Too close to a door
     * @param generatedPositions  (int[]) Array of 2 elements, X and Y positions
     * @return (boolean) True if the position is valid, false otherwise
     */
    private boolean spawnLocationGeneratedIsValid(int[] generatedPositions)
    {
        return !spawnPointOverlapEnvironment(generatedPositions[0], generatedPositions[1]) && !nearDoor(generatedPositions[0], generatedPositions[1]);
    }

    /**
     * Checks whether or not the given coordinates overlap an environment object
     * @param x (int) X coordinate of enemy
     * @param y (int) Y coordinate of enemy
     * @return (boolean) True if the coordinate overlaps or false otherwise
     */
    private boolean spawnPointOverlapEnvironment(int x, int y)
    {
        // Iterate over all the room images (the environment objects)
        for (int i = 0; i < roomImages.length; i++) {
            for (int j = 0; j < roomImages[i].length; j++) {
                // Get the upper left and lower right points of the image
                int[] posA = {(int) roomImages[i][j].getX(), (int) roomImages[i][j].getY()};
                int[] posB = {(int) roomImages[i][j].getX() + Settings.ROOM_DIVISION_SIZE, (int) roomImages[i][j].getY() + Settings.ROOM_DIVISION_SIZE};

                // Create a 2D array of the corners of the enemy
                // [ [upper left], [upper right], [lower left], [lower right] ]
                int[][] points = {{x, y}, {x + Settings.ROOM_DIVISION_SIZE, y}, {x + Settings.ROOM_DIVISION_SIZE, y + Settings.ROOM_DIVISION_SIZE}, {x, y + Settings.ROOM_DIVISION_SIZE}};

                // Check to see if there is an overlap between the enemies coordinates and the environment objects coordinates
                for (int k = 0; k < 4; k++) {
                    if ((posA[0] <= points[k][0] && points[k][0] <= posB[0]) && (posA[1] <= points[k][1] && points[k][1] <= posB[1])) { //is spawn location overlapping with environment object
                        if (roomImages[i][j].isCollidiable() || roomImages[i][j].isInteractable()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks whether or not the enemies coordinates are close to a door
     * @param x (int) X coordinate of enemy
     * @param y (int) Y coordinate of enemy
     * @return (boolean) True if the enemy is near a door or false otherwise
     */
    private boolean nearDoor(int x, int y)
    {
        // Iterate over the room layout
        for (int i = 0; i < roomLayout.size(); i++) {
            for (int j = 0; j < roomLayout.get(i).size(); j++) {
                // Check for doors
                if (roomLayout.get(i).get(j).equals("3")) {
                    // Find the center of the door
                    int[] doorPos = {(Settings.ROOM_DIVISION_SIZE * j) + (Settings.ROOM_DIVISION_SIZE / 2), (Settings.ROOM_DIVISION_SIZE * i) + (Settings.ROOM_DIVISION_SIZE / 2)}; //centre of door

                    // Get the upper left and lower right of the door box/zone
                    int[] doorPosA = {doorPos[0] - 600, doorPos[1] - 600};
                    int[] doorPosB = {doorPos[0] + 600, doorPos[1] + 600};

                    // Is the spawn location of the door inside the enemy zone
                    if ((doorPosA[0] <= x && x <= doorPosB[0]) && (doorPosA[1] <= y && y <= doorPosB[1])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Spawns all the pickups from the CSV file
     */
    private void spawnPickups()
    {
        // Iterate over the pickups
        for (int i = 0; i < pickupInfo.size(); i++) {
            // Convert the pickup integer to a filename
            String pickupRead = Settings.CSV_KEYS.get(pickupInfo.get(i)[0]);

            // Create X number of pickups as specified in the CSV file
            for (int y = 0; y < pickupInfo.get(i)[1]; y++) {

                // Generate a random spawn location
                int[] generatedSpawnLocation = generateSpawnLocation();
                switch (pickupRead) {
                    case "healthPickup":
                        pickups.add(new HealthBoost(generatedSpawnLocation[0], generatedSpawnLocation[1]));
                        break;
                    case "ammoPickup":
                        pickups.add(new AmmoPickup(generatedSpawnLocation[0], generatedSpawnLocation[1]));
                        break;
                    case "shieldPickup":
                        pickups.add(new ShieldPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], 5000));
                        break;
                    case "speedPickupUp":
                        pickups.add(new SpeedPickupUp(generatedSpawnLocation[0], generatedSpawnLocation[1], 15000));
                        break;
                    case "damagePickup":
                        pickups.add(new DamagePlus(generatedSpawnLocation[0], generatedSpawnLocation[1], 10000));
                        break;
                    default:
                        break;

                }
            }
        }
    }

    /**
     * Spawns all the weapons from the CSV file
     */
    private void spawnWeapons()
    {
        // Iterate over the pickups
        for (int i = 0; i < weaponInfo.size(); i++) {
            // Convert the pickup integer to a filename
            String weaponRead = Settings.CSV_KEYS.get(weaponInfo.get(i)[0]);
            String baseFilePath = "res/assets/weapons/";

            // Create X number of pickups as specified in the CSV file
            for (int y = 0; y < weaponInfo.get(i)[1]; y++) {

                // Generate a random spawn location
                int[] generatedSpawnLocation = generateSpawnLocation();
                switch (weaponRead) {
                    case "machete":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead, 0));
                        break;
                    case "revolver":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead, 25));
                        break;
                    case "shotgun":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead, 25));
                        break;
                    case "ar15":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead, 25));
                        break;
                    case "uzi":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead, 25));
                        break;
                    case "sniper":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead, 8));
                        break;
                    case "whip":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead, 0));
                        break;
                }
            }
        }
    }

    /**
     * Returns all the enemies in this room in a list
     * @return (ArrayList) List of enemies
     */
    public ArrayList<Enemy> getEnemies()
    {
        return enemies;
    }

    /**
     * Returns all the pickups in this room in a list
     * @return (ArrayList) List of pickups
     */
    public ArrayList<Pickup> getPickups()
    {
        return pickups;
    }

    /**
     * Returns all the weapons in this room in a list
     * @return (ArrayList) List of weapons
     */
    public ArrayList<WeaponPickup> getWeapons()
    {
        return weapons;
    }

    /**
     * Sets the last time the room played an audio file
     * @param time (long) Last time audio was played
     */
    public void setLastAudioTrigger(long time) {
        this.lastAudioTrigger = time;
    }

    /**
     * Returns the last time which this room played an audio file
     * @return (long) Last time audio was played
     */
    public long getLastAudioTrigger() {
        return lastAudioTrigger;
    }

    /**
     * Returns the name of this room. This is the name of the CSV file
     * that was read to create the room
     * @return (String) Room name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Create the animation threads to run water, quicksand and lava animations
     */
    public void runThreads() {

        waterAnimationThread.start();
        lavaAnimationThread.start();
        quicksandAnimationThread.start();
    }

    public void stopThreads()
    {
        waterAnimationThread.interrupt();
        lavaAnimationThread.interrupt();
        quicksandAnimationThread.interrupt();
    }

    public boolean areAnimationsRunning() {
        return waterAnimationThread.isAlive() && lavaAnimationThread.isAlive() && quicksandAnimationThread.isAlive();
    }
}
