package main.java.questfortheabodeth.threads;

import main.java.questfortheabodeth.Helper;
import main.java.questfortheabodeth.Settings;

/**
 * Creates a new thread that plays an audio file
 * and waits for the length of the file. Once the
 * file has finised playing it toggles the global
 * music playing boolean
 */
public class AudioThread extends Thread {
    private String sound;

    /**
     * Creates a new audio thread
     *
     * @param sound (String) The sound file to play
     */
    public AudioThread(String sound) {
        this.sound = sound;
    }

    /**
     * Waits for a set amountn of time and then toggles the global
     * music player variable
     */
    @Override
    public void run() {
        if (Settings.BACKGROUND_AUDIO_PLAYING) {
            try {
                Thread.sleep((int) Helper.getLengthOfAudioFile(sound));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Settings.BACKGROUND_AUDIO_PLAYING = false;
            }
        }
    }
}
