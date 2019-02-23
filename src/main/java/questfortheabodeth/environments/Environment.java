package main.java.questfortheabodeth.environments;

import main.java.questfortheabodeth.Settings;
import main.java.questfortheabodeth.sprites.Image;

import javax.sound.sampled.Clip;

public class Environment extends Image
{
    private int xPos, yPos;
    private int width = Settings.ROOM_DIVISION_SIZE;
    private int height = Settings.ROOM_DIVISION_SIZE;
    private boolean collidiable;
    private boolean interactable;
    private static long lastAudioTrigger;

    public Environment(int xPos, int yPos, String imageFilePath, boolean collidiable, boolean interactable)
    {
        super(xPos, yPos, imageFilePath);
        this.xPos = xPos;
        this.yPos = yPos;
        this.collidiable = collidiable;
        this.interactable = interactable;
    }

    public float getX()
    {
        return this.xPos;
    }

    public float getY()
    {
        return this.yPos;
    }

    public float getWidth()
    {
        return super.getWidth();
    }

    public float getHeight()
    {
        return super.getHeight();
    }

    public boolean isCollidiable()
    {
        return collidiable;
    }

    public boolean isInteractable()
    {
        return interactable;
    }

    @Override
    public String toString()
    {
        return "<" + getClass() + " @ [" + getX() + ", " + getY() + "]>";
    }

    public long getLastAudioTrigger()
    {
        return lastAudioTrigger;
    }

    public void setLastAudioTrigger(long time)
    {
        lastAudioTrigger = time;
    }
}