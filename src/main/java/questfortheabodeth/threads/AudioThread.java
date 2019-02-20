package main.java.questfortheabodeth.threads;

import jdk.nashorn.internal.runtime.ECMAException;
import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;

import java.util.Set;

public class AudioThread extends Thread {
    private String sound;

    public AudioThread(String sound) {
        this.sound = sound;
    }

    public void run() {
        if (Settings.BACKGROUND_AUDIO_PLAYING) {
            try {
                Thread.sleep((int) Helper.getLengthOfAudioFile(sound));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Settings.BACKGROUND_AUDIO_PLAYING = false;
        }
    }
}
