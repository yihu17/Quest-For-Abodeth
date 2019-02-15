package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.FileOperator;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.environments.Interactables.Door;
import main.java.questfortheabodeth.environments.traps.Quicksand;
import main.java.questfortheabodeth.environments.traps.SpikeTrap;
import main.java.questfortheabodeth.environments.traps.Water;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.interfaces.Interactable;
import main.java.questfortheabodeth.powerups.*;
import main.java.questfortheabodeth.weapons.WeaponPickup;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Room implements Drawable {
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

    private ArrayList<Drawable> drawables = new ArrayList<>();

    /**
     * Creates a new room of the specified type
     * @param type (int) Room type
     * @param up (boolean) Is the a door at the top of the room
     * @param down (boolean) Is the a door at the bottom of the room
     * @param left (boolean) Is the a door at the left of the room
     * @param right (boolean) Is the a door at the right of the room
     */
    public Room(int type, boolean up, boolean down, boolean left, boolean right) {
        doors[0] = up;
        doors[1] = down;
        doors[2] = left;
        doors[3] = right;

        this.type = type;
        if (type == 1) {
            roomFile = new FileOperator("res/assets/CSVs/roomCSVs/roomDataA.csv"); //needs to get path dynamically...
        } else if (type < 0) {
            return;
        } else{
            String[] rooms = new String[]{"A", "B"};
            int index = Settings.GENERATOR.nextInt(rooms.length);
            roomFile = new FileOperator("res/assets/CSVs/roomCSVs/roomData" + rooms[index] + ".csv"); //needs to get path dynamically...
        }
        readRoomData();
        loadRoomImages();
        spawnEnemies();
        spawnPickups();
        spawnWeapons();

        // Now that the room has been generated make some doors
        if (up) {
            int topDoorStart = Settings.GENERATOR.nextInt(10) + 10;
            roomImages[0][topDoorStart] = new Door(Settings.ROOM_DIVISION_SIZE * topDoorStart, 0, "res/assets/environment/leftDoor.png", -2);
            roomImages[0][topDoorStart + 1] = new Door(Settings.ROOM_DIVISION_SIZE * (topDoorStart + 1), 0, "res/assets/environment/rightDoor.png", -2);

            roomImages[1][topDoorStart] = new Environment(
                    Settings.ROOM_DIVISION_SIZE * topDoorStart,
                    Settings.ROOM_DIVISION_SIZE,
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            );
            roomImages[1][topDoorStart + 1] = new Environment(
                    Settings.ROOM_DIVISION_SIZE * (topDoorStart + 1),
                    Settings.ROOM_DIVISION_SIZE,
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            );
        } if (down) {
            int bottomDoorStart = Settings.GENERATOR.nextInt(10) + 10;
            roomImages[Settings.ROOM_DIVISION_ROWS - 1][bottomDoorStart] = new Door(Settings.ROOM_DIVISION_SIZE * bottomDoorStart, Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_ROWS - 1), "res/assets/environment/leftDoor.png", -4);
            roomImages[Settings.ROOM_DIVISION_ROWS - 1][bottomDoorStart + 1] = new Door(Settings.ROOM_DIVISION_SIZE * (bottomDoorStart + 1), Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_ROWS - 1), "res/assets/environment/rightDoor.png", -4);

            roomImages[Settings.ROOM_DIVISION_ROWS - 2][bottomDoorStart] = new Environment(
                    Settings.ROOM_DIVISION_SIZE * bottomDoorStart,
                    Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_ROWS - 2),
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            );
            roomImages[Settings.ROOM_DIVISION_ROWS - 2][bottomDoorStart + 1] = new Environment(
                    Settings.ROOM_DIVISION_SIZE * (bottomDoorStart + 1),
                    Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_ROWS - 2),
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            );
        } if (left) {
            int leftDoorStart = Settings.GENERATOR.nextInt(10) + 5;
            roomImages[leftDoorStart][0] = new Door(0, Settings.ROOM_DIVISION_SIZE * leftDoorStart, "res/assets/environment/topDoor.png", -1);
            roomImages[leftDoorStart + 1][0] = new Door(0, Settings.ROOM_DIVISION_SIZE * (leftDoorStart + 1), "res/assets/environment/bottomDoor.png", -1);

            roomImages[leftDoorStart][1] = new Environment(
                    Settings.ROOM_DIVISION_SIZE,
                    Settings.ROOM_DIVISION_SIZE * leftDoorStart,
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            );
            roomImages[leftDoorStart + 1][1] = new Environment(
                    Settings.ROOM_DIVISION_SIZE,
                    Settings.ROOM_DIVISION_SIZE * (leftDoorStart + 1),
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            );
        } if (right) {
            int rightDoorStart = Settings.GENERATOR.nextInt(10) + 5;
            roomImages[rightDoorStart][Settings.ROOM_DIVISION_COLUMNS - 1] = new Door(Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_COLUMNS - 1), Settings.ROOM_DIVISION_SIZE * rightDoorStart, "res/assets/environment/topDoor.png", -3);
            roomImages[rightDoorStart + 1][Settings.ROOM_DIVISION_COLUMNS - 1] = new Door(Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_COLUMNS - 1), Settings.ROOM_DIVISION_SIZE * (rightDoorStart + 1), "res/assets/environment/bottomDoor.png", -3);

            roomImages[rightDoorStart][Settings.ROOM_DIVISION_COLUMNS - 2] = new Environment(
                    Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_COLUMNS - 2),
                    Settings.ROOM_DIVISION_SIZE * rightDoorStart,
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            );
            roomImages[rightDoorStart + 1][Settings.ROOM_DIVISION_COLUMNS - 2] = new Environment(
                    Settings.ROOM_DIVISION_SIZE * (Settings.ROOM_DIVISION_COLUMNS - 2),
                    Settings.ROOM_DIVISION_SIZE * (rightDoorStart + 1),
                    "res/assets/environment/floor/floor1.png",
                    false,
                    false
            );
        }
    }

    public int getType()
    {
        return this.type;
    }

    public ArrayList<Collidable> getCollidables() {
        ArrayList<Collidable> c = new ArrayList<>();
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            for (int j = 0; j < Settings.ROOM_DIVISION_COLUMNS; j++) {
                if (roomImages[i][j] instanceof Collidable && !(roomImages[i][j] instanceof Interactable)) {
                    c.add((Collidable) roomImages[i][j]);
                }
            }
        }
        c.addAll(enemies);
        c.addAll(pickups);
        c.addAll(weapons);

        return c;
    }

    public ArrayList<Interactable> getInteractables() {
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

    @Override
    public String toString() {
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
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        drawables.forEach(renderTarget::draw);
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            for (int j = 0; j < Settings.ROOM_DIVISION_COLUMNS; j++) {
                renderTarget.draw(roomImages[i][j]);
            }
        }
    }

    private void readRoomData() {
        readToLayout();
        int startOfData = Settings.WINDOW_HEIGHT / Settings.ROOM_DIVISION_SIZE;
        int[] enemyTypes = Arrays.stream(roomFile.readLine(startOfData)).mapToInt(Integer::parseInt).toArray(); //read string array as int array
        int[] enemyQuantities = Arrays.stream(roomFile.readLine(startOfData + 1)).mapToInt(Integer::parseInt).toArray(); //read string array as int array
        int[] pickupTypes = Arrays.stream(roomFile.readLine(startOfData + 2)).mapToInt(Integer::parseInt).toArray(); //read string array as int array
        int[] pickupQuantities = Arrays.stream(roomFile.readLine(startOfData + 3)).mapToInt(Integer::parseInt).toArray(); //read string array as int array
        int[] weaponTypes = Arrays.stream(roomFile.readLine(startOfData + 4)).mapToInt(Integer::parseInt).toArray(); //read string array as int array
        int[] weaponQuantities = Arrays.stream(roomFile.readLine(startOfData + 5)).mapToInt(Integer::parseInt).toArray(); //read string array as int array

        for (int i = 0; i < enemyTypes.length; i++) {
            enemyInfo.add(new int[]{enemyTypes[i], enemyQuantities[i]});
        }

        for (int i = 0; i < pickupTypes.length; i++) {
            pickupInfo.add(new int[]{pickupTypes[i], pickupQuantities[i]});
        }

        for (int i = 0; i < weaponTypes.length; i++) {
            weaponInfo.add(new int[]{weaponTypes[i], weaponQuantities[i]});
        }
    }

    private void readToLayout() {
        roomLayout.clear(); //resets layout
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            roomLayout.add(new ArrayList<>());
            String[] currentLine = roomFile.readLine(i);
            for (int j = 0; j < currentLine.length; j++) {
                roomLayout.get(i).add(currentLine[j]);
            }
        }
    }

    private void loadRoomImages() {
        int spacing = Settings.ROOM_DIVISION_SIZE;
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            for (int j = 0; j < Settings.ROOM_DIVISION_COLUMNS; j++) {
                String elementRead = Settings.CSV_KEYS.get(Integer.parseInt(roomLayout.get(i).get(j)));
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
                        //roomImages[i][j] = new InteractableEnvironment(spacing * j, spacing * i, filePath);
                        roomImages[i][j] = new Water(spacing * j, spacing * i, filePath);
                        break;
                    case "quicksand":
                        roomImages[i][j] = new Quicksand(spacing * j, spacing * i, filePath);
                        break;
                    case "spikeTrap":
                        roomImages[i][j] = new SpikeTrap(spacing * j, spacing * i, filePath);
                        break;
                    case "switchPuzzle":
                        roomImages[i][j] = new CollidableInteractableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "shootingArrowTrap":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "swingingAxeTrap":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "boilingOil":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "rollingBoulderTrap":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "fierySphinx":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "graveyard":
                        //roomImages[i][j] = new InteractableEnvironment(spacing * j, spacing * i, filePath);
                        roomImages[i][j] = new Environment(spacing * j, spacing * i, filePath, false, false);
                        break;
                    case "crushingWalls":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    default:
                        throw new AssertionError("Unknown image" + elementRead);
                }
            }
        }
    }

    private void spawnEnemies() {
        for (int i = 0; i < enemyInfo.size(); i++) {
            String enemyRead = Settings.CSV_KEYS.get(enemyInfo.get(i)[0]);
            String filePath = "res/assets/enemies/" + enemyRead + ".png";
            for (int y = 0; y < enemyInfo.get(i)[1]; y++) {
                int[] generatedSpawnLocation = generateSpawnLocation();
                switch (enemyRead) {
                    case "zombie":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 2, "zombie", 700, 10));
                        break;
                    case "jackal":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 3, "jackal", 500, 15));
                        break;
                    case "bat":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 5, "bat", 400, 3));
                        break;
                    case "spider":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 2, "spider", 650, 8));
                        break;
                    case "mummifiedSlave":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 1, "mummifiedSlave", 900, 12));
                        break;
                    case "giantSpider":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 1, "giantSpider", 900, 15));
                        break;
                    case "giantZombie":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 1, "giantZombie", 1200, 18));
                        break;
                    case "egyptianMummy":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 2, "egyptianMummy", 750, 11));
                        break;
                    case "crocodile":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 1, "crocodile", 550, 13));
                        break;
                }
            }
        }
    }

    private int[] generateSpawnLocation() {
        int[] spawnPositionGenerated = new int[2];
        Random rand = new Random();
        spawnPositionGenerated[0] = rand.nextInt(Settings.WINDOW_WIDTH) + Settings.ROOM_DIVISION_SIZE;
        spawnPositionGenerated[1] = rand.nextInt(Settings.WINDOW_HEIGHT) + Settings.ROOM_DIVISION_SIZE;
        while (!spawnLocationGeneratedIsValid(spawnPositionGenerated)) { //keep trying to create a spawn location until valid
            spawnPositionGenerated[0] = rand.nextInt(Settings.WINDOW_WIDTH) + Settings.ROOM_DIVISION_SIZE;
            spawnPositionGenerated[1] = rand.nextInt(Settings.WINDOW_HEIGHT) + Settings.ROOM_DIVISION_SIZE;
        }
        return spawnPositionGenerated;
    }

    private boolean spawnLocationGeneratedIsValid(int[] generatedPositions) {
        return !spawnPointOverlapEnvironment(generatedPositions[0], generatedPositions[1]) && !nearDoor(generatedPositions[0], generatedPositions[1]);
    }

    private boolean spawnPointOverlapEnvironment(int x, int y) {
        for (int i = 0; i < roomImages.length; i++) {
            for (int j = 0; j < roomImages[i].length; j++) {
                int[] posA = {(int) roomImages[i][j].getX(), (int) roomImages[i][j].getY()}; //upper left point of environment object
                int[] posB = {(int) roomImages[i][j].getX() + Settings.ROOM_DIVISION_SIZE, (int) roomImages[i][j].getY() + Settings.ROOM_DIVISION_SIZE}; //lower right point of environment object

                int[][] points = {{x, y}, {x + Settings.ROOM_DIVISION_SIZE, y}, {x + Settings.ROOM_DIVISION_SIZE, y + Settings.ROOM_DIVISION_SIZE}, {x, y + Settings.ROOM_DIVISION_SIZE}}; //upper left, upper right, lower right, lower left points of enemy

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

    private boolean nearDoor(int x, int y) {
        for (int i = 0; i < roomLayout.size(); i++) {
            for (int j = 0; j < roomLayout.get(i).size(); j++) {
                if (roomLayout.get(i).get(j).equals("3")) { //find door object
                    int[] doorPos = {(Settings.ROOM_DIVISION_SIZE * j) + (Settings.ROOM_DIVISION_SIZE / 2), (Settings.ROOM_DIVISION_SIZE * i) + (Settings.ROOM_DIVISION_SIZE / 2)}; //centre of door
                    int[] doorPosA = {doorPos[0] - 350, doorPos[1] - 250}; //upper left of no enemy zone/ box
                    int[] doorPosB = {doorPos[0] + 350, doorPos[1] + 250}; //lower right of no enemy zone/ box
                    if ((doorPosA[0] <= x && x <= doorPosB[0]) && (doorPosA[1] <= y && y <= doorPosB[1])) { //is spawn location in zone?
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void spawnPickups() {
        for (int i = 0; i < pickupInfo.size(); i++) {
            String pickupRead = Settings.CSV_KEYS.get(pickupInfo.get(i)[0]);
            for (int y = 0; y < pickupInfo.get(i)[1]; y++) {
                int[] generatedSpawnLocation = generateSpawnLocation();
                switch (pickupRead) {
                    case "healthPickup":
                        pickups.add(new HealthBoost(generatedSpawnLocation[0], generatedSpawnLocation[1]));
                        break;
                    case "ammoPickup":
                        pickups.add(new AmmoPickup(generatedSpawnLocation[0], generatedSpawnLocation[1]));
                        break;
                    case "shieldPickup":
                        pickups.add(new ShieldPickup(generatedSpawnLocation[0], generatedSpawnLocation[1]));
                        break;
                    case "speedPickupUp":
                        pickups.add(new SpeedPickupUp(generatedSpawnLocation[0], generatedSpawnLocation[1], 15000));
                        break;
                    case "speedPickupDown":
                        pickups.add(new SpeedPickupDown(generatedSpawnLocation[0], generatedSpawnLocation[1], 10000));
                        break;
                    case "damagePickup":
                        pickups.add(new DamagePlus(generatedSpawnLocation[0], generatedSpawnLocation[1], 10000));
                        break;
                }
            }
        }
    }

    private void spawnWeapons() {
        for (int i = 0; i < weaponInfo.size(); i++) {
            String weaponRead = Settings.CSV_KEYS.get(weaponInfo.get(i)[0]);
            String baseFilePath = "res/assets/weapons/";
            for (int y = 0; y < weaponInfo.get(i)[1]; y++) {
                int[] generatedSpawnLocation = generateSpawnLocation();
                switch (weaponRead) {
                    case "machete":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead));
                        break;
                    case "revolver":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead));
                        break;
                    case "shotgun":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead));
                        break;
                    case "ar15":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead));
                        break;
                    case "uzi":
                        weapons.add(new WeaponPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], baseFilePath + weaponRead + ".png", weaponRead));
                        break;
                }
            }
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Pickup> getPickups() {
        return pickups;
    }

    public ArrayList<WeaponPickup> getWeapons() {
        return weapons;
    }

    public void playMusic() {
        try {
            int r = Settings.GENERATOR.nextInt(16) + 1;
            System.out.println(r);
            String fileName = "res/assets/audio/roomMusic/" + r + ".wav";
            File soundFile = new File(fileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Settings.AUDIO_STREAMER = AudioSystem.getClip();
            Settings.AUDIO_STREAMER.open(audioInputStream);
            Settings.AUDIO_STREAMER.start();
        } catch (Exception e) {
            System.out.println("Audio error");
            e.printStackTrace();
        }
    }
}
