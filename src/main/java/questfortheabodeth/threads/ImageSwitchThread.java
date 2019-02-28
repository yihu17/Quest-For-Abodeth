package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.interfaces.Trap;
import main.java.questfortheabodeth.sprites.Image;

import java.util.ArrayList;

/**
 * Thread that handles the switching of images to simulate an
 * animation
 */
public class ImageSwitchThread extends Thread {
    private String pathA;
    private String pathB;
    private int interval;
    private ArrayList<Image> traps = new ArrayList<Image>();

    /**
     * Creates a new image switch thread
     *
     * @param pathA    (String) Initial image
     * @param pathB    (String) Image to swap to
     * @param interval (int) Time between image switches in milliseconds
     */
    public ImageSwitchThread(String pathA, String pathB, int interval) {
        this.pathA = pathA;
        this.pathB = pathB;
        this.interval = interval;
    }

    /**
     * For every trap this thread has been given, switch between the two images
     */
    @Override
    public void run() {
        while (true) {
            // Load in image from path B
            traps.forEach((i) -> i.loadImageFromFile(pathB));

            // Sleep before loading in image A again
            try {
                Thread.sleep(interval);
                traps.forEach((i) -> i.loadImageFromFile(pathA));
                Thread.sleep(interval);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a {@link Trap} to the list of traps this thread switches between
     *
     * @param trap (Trap) The trap object to switches images for
     */
    public void addTrap(Trap trap)
    {
        traps.add((Image) trap);
    }
}
