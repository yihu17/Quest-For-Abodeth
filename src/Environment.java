public class Environment extends Image {
    private int xPos, yPos;
    private int width = Settings.roomDivisionSize;
    private int height = Settings.roomDivisionSize;

    public Environment(int xPos, int yPos, String imageFilePath) {
        super(xPos, yPos, imageFilePath);
        this.xPos = xPos;
        this.yPos = yPos;
    }
}