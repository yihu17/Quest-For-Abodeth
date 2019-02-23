package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.sprites.Image;
import org.jsfml.graphics.RenderTarget;

import java.util.concurrent.CyclicBarrier;

//import java.awt.*;

public class ImageSwitchThread extends Thread {
    private RenderTarget window;
    private String pathA;
    private String pathB;
    private int interval;
    private Image trap;

    public ImageSwitchThread(Image trap, String pathA, String pathB, int interval) {
        this.trap = trap;
        this.pathA = pathA;
        this.pathB = pathB;
        this.interval = interval;
    }

    public void run() {
        while (true) {
            System.out.println("this is running");
            trap.loadImageFromFile(pathB);
            try {
                Thread.sleep(interval);
                trap.loadImageFromFile(pathA);
                Thread.sleep(interval);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
