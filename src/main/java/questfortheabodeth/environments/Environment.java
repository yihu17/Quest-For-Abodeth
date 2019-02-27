package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.Sprite;

public class Environment extends Image
{
    private int xPos, yPos;
    private int width = Settings.ROOM_DIVISION_SIZE;
    private int height = Settings.ROOM_DIVISION_SIZE;
    private boolean collidiable;
    private boolean interactable;
    private static long lastAudioTrigger;

    /**
     * Creates a new environment object
     *
     * @param xPos          (int) X position of this environment object
     * @param yPos          (int) Y position of this environment object
     * @param imageFilePath (String) Image file to load
     * @param collidiable   (boolean) Is this object collidable
     * @param interactable  (boolean) Is this object interactable
     */
    public Environment(int xPos, int yPos, String imageFilePath, boolean collidiable, boolean interactable)
    {
        super(xPos, yPos, imageFilePath);
        this.xPos = xPos;
        this.yPos = yPos;
        this.collidiable = collidiable;
        this.interactable = interactable;
    }

    /**
     * Returns the X coordinate of the top left most point of this object
     * @return (float) X position of this environment object
     */
    public float getX()
    {
        return this.xPos;
    }

    /**
     * Returns the Y coordinate of the top left most point of this object
     * @return (float) Y position of this environment object
     */
    public float getY()
    {
        return this.yPos;
    }

    /**
     * Returns the width of this environment object as seen by
     * {@link Sprite#getGlobalBounds()}
     * @return (float) Width of this environment object
     */
    public float getWidth()
    {
        return super.getWidth();
    }

    /**
     * Returns the height of this environment object as seen by
     * {@link Sprite#getGlobalBounds()}
     * @return (float) Height of this environment object
     */
    public float getHeight()
    {
        return super.getHeight();
    }

    /**
     * Is this object collidable
     * @return (boolean) True if this object is collidable, false otherwise
     */
    public boolean isCollidiable()
    {
        return collidiable;
    }

    /**
     * Is this object interactable
     * @return (boolean) True if this object is interactable, false otherwise
     */
    public boolean isInteractable()
    {
        return interactable;
    }

    /**
     * Provide a more informational view of this environment object
     * @return (String) Object information
     */
    @Override
    public String toString()
    {
        return "<" + getClass() + " @ [" + getX() + ", " + getY() + "]>";
    }

    /**
     * Returns the last time that an audio file was played by this object
     * @return (long) Last time audio was player
     */
    public long getLastAudioTrigger()
    {
        return lastAudioTrigger;
    }

    /**
     * Sets the last time that this object played and audio file
     * @param time (long) Last time this object played an audio file
     */
    public void setLastAudioTrigger(long time)
    {
        lastAudioTrigger = time;
    }
}