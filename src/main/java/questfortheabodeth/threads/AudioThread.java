package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;


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
