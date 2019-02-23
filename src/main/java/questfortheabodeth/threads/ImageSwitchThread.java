package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.RenderTarget;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

//import java.awt.*;

public class ImageSwitchThread extends Thread {
    private String pathA;
    private String pathB;
    private int interval;
    private ArrayList<Image> traps = new ArrayList<Image>();

    public ImageSwitchThread(String pathA, String pathB, int interval) {
        this.pathA = pathA;
        this.pathB = pathB;
        this.interval = interval;
    }

    public void run() {
        while (true) {
            traps.forEach((i) -> i.loadImageFromFile(pathB));
            try {
                Thread.sleep(interval);
                traps.forEach((i) -> i.loadImageFromFile(pathA));
                Thread.sleep(interval);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addTrap(Image trap) {
        traps.add(trap);
    }
}
