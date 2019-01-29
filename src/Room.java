import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import java.util.*;

import java.util.ArrayList;

public class Room implements Drawable
{
    private int type;
    private FileOperator roomFile;
    private ArrayList<ArrayList<String>> roomLayout = new ArrayList<ArrayList<String>>();
	private Image[][] roomImages = new Image[9][16];
    private ArrayList<int[]> enemeyInfo = new ArrayList<int[]>();
    private Hashtable objectReferenceCSV = new Hashtable();

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
		for(int i = 0; i<9; i++) //remove hardcode
		{
			for(int j = 0; j<16; j++)
			{
				renderTarget.draw(roomImages[i][j]);
			}
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
			enemeyInfo.add(new int[] {enemyTypes[i], enemyQuantities[i]});
		}

		for(int i = 0; i<pickupQuantities.length; i++)
		{
			enemeyInfo.add(new int[] {enemyTypes[i], enemyQuantities[i]});
		}

}

    private void readToLayout()
    {
        roomLayout.clear(); //resets layout
        for(int i = 0; i<9; i++)
        {
            roomLayout.add(new ArrayList<String>());
            String[] currentLine = roomFile.readLine(i);
            for(int j = 0; j<16; j++)
            {
                roomLayout.get(i).add(currentLine[j]);
            }
        }
    }
	
	private void loadRoomImages()
	{
		for(int i = 0; i<9; i++)
		{
			for(int j = 0; j<16; j++)
			{
				try{
					String filePath = "res/assets/"+Settings.CSV_KEYS.get(roomLayout.get(i).get(j))+".png";
					roomImages[i][j] = new Image(120*j, 120*i, "res/assets/wall.png"/*filePath*/);
					System.out.println("Created image @ [" + (120*i) + ", " + (120*j) + "]");
				} catch(Exception e)
				{
					System.out.print("yeeticus boi");
				}
			}
		}
	}
}
