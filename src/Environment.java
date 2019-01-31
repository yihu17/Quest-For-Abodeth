public class Environment extends Image {
    private int xPos, yPos;
    private int width = Settings.ROOM_DIVISION_SIZE;
    private int height = Settings.ROOM_DIVISION_SIZE;

    public Environment(int xPos, int yPos, String imageFilePath) {
        super(xPos, yPos, imageFilePath);
        this.xPos = xPos;
        this.yPos = yPos;
    }
}