package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.FileOperator;
import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.characters.Enemy;
import main.java.questfortheabodeth.interfaces.Collidable;
import main.java.questfortheabodeth.powerups.*;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Room implements Drawable
{
    private int type;
    private FileOperator roomFile;
    private ArrayList<ArrayList<String>> roomLayout = new ArrayList<>();
    private Environment[][] roomImages = new Environment[Settings.ROOM_DIVISION_ROWS][Settings.ROOM_DIVISION_COLUMNS];

    private ArrayList<int[]> enemyInfo = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();

    private ArrayList<int[]> pickupInfo = new ArrayList<>();
    private ArrayList<Pickup> pickups = new ArrayList<>();

    private ArrayList<Drawable> drawables = new ArrayList<>();

    /**
     * Creates a new room of the specified type
     *
     * @param type (int) main.java.questfortheabodeth.environments.Room type
     */
    public Room(int type)
    {
        this.type = type;
        roomFile = new FileOperator("res/roomCSVs/roomDataB.csv"); //needs to get path dynamically...

        readRoomData();
        loadRoomImages();
        spawnEnemies();
        spawnPickups();
    }

    public ArrayList<Collidable> getCollidables()
    {
        ArrayList<Collidable> c = new ArrayList<>();
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            for (int j = 0; j < Settings.ROOM_DIVISION_COLUMNS; j++) {
                if (roomImages[i][j] instanceof Collidable) {
                    c.add((Collidable) roomImages[i][j]);
                }
            }
        }
        //c.addAll(enemies);

        return c;
    }

    @Override
    public String toString()
    {
        return "<main.java.questfortheabodeth.environments.Room " + type + ">";
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
        drawables.forEach(renderTarget::draw);
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            for (int j = 0; j < Settings.ROOM_DIVISION_COLUMNS; j++) {
                renderTarget.draw(roomImages[i][j]);
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            renderTarget.draw(enemies.get(i));
        }
        for (int i = 0; i < pickups.size(); i++) {
            renderTarget.draw(pickups.get(i));
        }
    }

    private void readRoomData()
    {
        readToLayout();
        int startOfEnemyAndPickupData = Settings.WINDOW_HEIGHT / Settings.ROOM_DIVISION_SIZE;
        int[] enemyTypes = Arrays.stream(roomFile.readLine(startOfEnemyAndPickupData)).mapToInt(Integer::parseInt).toArray(); //read string array as int array
        int[] enemyQuantities = Arrays.stream(roomFile.readLine(startOfEnemyAndPickupData + 1)).mapToInt(Integer::parseInt).toArray(); //read string array as int array
        int[] pickupTypes = Arrays.stream(roomFile.readLine(startOfEnemyAndPickupData + 2)).mapToInt(Integer::parseInt).toArray(); //read string array as int array
        int[] pickupQuantities = Arrays.stream(roomFile.readLine(startOfEnemyAndPickupData + 3)).mapToInt(Integer::parseInt).toArray(); //read string array as int array

        for(int i = 0; i<enemyTypes.length; i++) {
            enemyInfo.add(new int[]{enemyTypes[i], enemyQuantities[i]});
        }

        for (int i = 0; i < pickupTypes.length; i++) {
            pickupInfo.add(new int[]{pickupTypes[i], pickupQuantities[i]});
        }
    }

    private void readToLayout()
    {
        roomLayout.clear(); //resets layout
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            roomLayout.add(new ArrayList<>());
            String[] currentLine = roomFile.readLine(i);
            for(int j = 0; j<currentLine.length; j++) {
                roomLayout.get(i).add(currentLine[j]);
            }
        }
    }

    private void loadRoomImages()
    {
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
                        roomImages[i][j] = new Environment(spacing * j, spacing * i, "res/assets/environment/floor/floor" + floorNum + ".png", false);
                        break;
                    case "door":
                        roomImages[i][j] = new InteractableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "water":
                        roomImages[i][j] = new InteractableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "quicksand":
                        roomImages[i][j] = new InteractableEnvironment(spacing * j, spacing * i, filePath);
                        break;
                    case "spikeTrap":
                        roomImages[i][j] = new CollidableEnvironment(spacing * j, spacing * i, filePath);
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
                        roomImages[i][j] = new InteractableEnvironment(spacing * j, spacing * i, filePath);
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
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 2));
                        break;
                    case "jackal":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 3));
                        break;
                    case "bat":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 5));
                        break;
                    case "spider":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 2));
                        break;
                    case "mummifiedSlave":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 1));
                        break;
                    case "giantSpider":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 1));
                        break;
                    case "giantZombie":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 1));
                        break;
                    case "egyptianMummy":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 2));
                        break;
                    case "crocodile":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath, 1));
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
                        if (roomImages[i][j].isCollidiable()) {
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
                        pickups.add(new HealthBoost(generatedSpawnLocation[0], generatedSpawnLocation[1], 15000));
                        break;
                    case "ammoPickup":
                        pickups.add(new AmmoPickup(generatedSpawnLocation[0], generatedSpawnLocation[1]));
                        break;
                    case "shieldPickup":
                        pickups.add(new ShieldPickup(generatedSpawnLocation[0], generatedSpawnLocation[1], 20000));
                        break;
                    case "speedPickupUp":
                        pickups.add(new SpeedPickupUp(generatedSpawnLocation[0], generatedSpawnLocation[1], 15000));
                        break;
                    case "speedPickupDown":
                        pickups.add(new SpeedPickupDown(generatedSpawnLocation[0], generatedSpawnLocation[1], 10000));
                        break;
                    case "damagePickup":
                        pickups.add(new DamagePlus(generatedSpawnLocation[0], generatedSpawnLocation[1]));
                        break;
                }
            }
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}