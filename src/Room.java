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

    private ArrayList<Drawable> drawables = new ArrayList<>();

    /**
     * Creates a new room of the specified type
     *
     * @param type (int) Room type
     */
    public Room(int type)
    {
        this.type = type;
        roomFile = new FileOperator("res/roomCSVs/roomData.csv"); //needs to get path dynamically...

        readRoomData();
		loadRoomImages();
        spawnEnemies();
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
        return "<Room " + type + ">";
    }

    /**
     * Makes the room drawable. Method takes all the drawables in this Room class
     * and draw them all to the screen
     *
     * @param renderTarget (RenderTarget) The window to draw to
     * @param renderStates (RenderStates) Again, what are these
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates)
    {
        drawables.forEach(renderTarget::draw);
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++)
        {
            for (int j = 0; j < Settings.ROOM_DIVISION_COLUMNS; j++)
			{
				renderTarget.draw(roomImages[i][j]);
			}
		}
        for (int i = 0; i < enemies.size(); i++) {
            renderTarget.draw(enemies.get(i));
        }
    }

    private void readRoomData()
    {
        readToLayout();

        int[] enemyTypes = Arrays.stream(roomFile.readLine(9)).mapToInt(Integer::parseInt).toArray(); //read string array as int array
        int[] enemyQuantities = Arrays.stream(roomFile.readLine(10)).mapToInt(Integer::parseInt).toArray(); //read string array as int array
        int[] pickupTypes =  Arrays.stream(roomFile.readLine(11)).mapToInt(Integer::parseInt).toArray(); //read string array as int array
        int[] pickupQuantities = Arrays.stream(roomFile.readLine(12)).mapToInt(Integer::parseInt).toArray(); //read string array as int array

		for(int i = 0; i<enemyTypes.length; i++)
		{
            enemyInfo.add(new int[]{enemyTypes[i], enemyQuantities[i]});
		}

		for(int i = 0; i<pickupQuantities.length; i++)
		{
            enemyInfo.add(new int[]{pickupTypes[i], pickupQuantities[i]});
		}
    }

    private void readToLayout()
    {
        roomLayout.clear(); //resets layout
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++)
        {
            roomLayout.add(new ArrayList<>());
            String[] currentLine = roomFile.readLine(i);
            for(int j = 0; j<currentLine.length; j++)
            {
                roomLayout.get(i).add(currentLine[j]);
            }
        }
    }
	
	private void loadRoomImages()
	{
        for (int i = 0; i < Settings.ROOM_DIVISION_ROWS; i++) {
            for (int j = 0; j < Settings.ROOM_DIVISION_COLUMNS; j++)
			{
                String elementRead = Settings.CSV_KEYS.get(Integer.parseInt(roomLayout.get(i).get(j)));
                String filePath = "res/assets/" + elementRead + ".png";
                switch (elementRead) {
                    case "wall":
                        roomImages[i][j] = new CollidableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "floor":
                        roomImages[i][j] = new Environment(120 * j, 120 * i, filePath);
                        break;
                    case "door":
                        roomImages[i][j] = new InteractableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "water":
                        roomImages[i][j] = new InteractableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "quicksand":
                        roomImages[i][j] = new InteractableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "spikeTrap":
                        roomImages[i][j] = new CollidableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "switchPuzzle":
                        roomImages[i][j] = new CollidableInteractableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "shootingArrowTrap":
                        roomImages[i][j] = new CollidableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "swingingAxeTrap":
                        roomImages[i][j] = new CollidableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "boilingOil":
                        roomImages[i][j] = new CollidableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "rollingBoulderTrap":
                        roomImages[i][j] = new CollidableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "fierySphinx":
                        roomImages[i][j] = new CollidableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "graveyard":
                        roomImages[i][j] = new InteractableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    case "crushingWalls":
                        roomImages[i][j] = new CollidableEnvironment(120 * j, 120 * i, filePath);
                        break;
                    default:
                        throw new AssertionError("Unknown image" + elementRead);
                }
			}
		}
	}

    private void spawnEnemies() {
        System.out.println(enemyInfo.size());
        for (int i = 0; i < enemyInfo.size(); i++) {
            String enemyRead = Settings.CSV_KEYS.get(enemyInfo.get(i)[0]);
            String filePath = "res/assets/enemies/" + enemyRead + ".png";
            for (int y = 0; y < enemyInfo.get(i)[1]; y++) {
                int[] generatedSpawnLocation = generateSpawnLocation();
                switch (enemyRead) {
                    case "zombie":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath));
                        break;
                    case "jackal":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath));
                        break;
                    case "bat":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath));
                        break;
                    case "spider":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath));
                        break;
                    case "mummifiedSlave":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath));
                        break;
                    case "giantSpider":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath));
                        break;
                    case "giantZombie":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath));
                        break;
                    case "egyptianMummy":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath));
                        break;
                    case "crocodile":
                        enemies.add(new Enemy(generatedSpawnLocation[0], generatedSpawnLocation[1], 100, filePath));
                        break;
                }
            }
        }
    }

    private int[] generateSpawnLocation() {
        int[] spawnPositionGenerated = new int[2];
        Random rand = new Random();
        spawnPositionGenerated[0] = rand.nextInt(1920 - 240) + 120;
        spawnPositionGenerated[1] = rand.nextInt(1080 - 240) + 120;
        while (!spawnLocationGeneratedIsValid(spawnPositionGenerated)) {
            spawnPositionGenerated[0] = rand.nextInt(1920 - 240) + 120;
            spawnPositionGenerated[1] = rand.nextInt(1080 - 240) + 120;
        }
        return spawnPositionGenerated;
    }

    private boolean spawnLocationGeneratedIsValid(int[] generatedPositions) {
        return true;
    }
}
