package main.java.questfortheabodeth;

import java.util.Timer;
import java.util.TimerTask;

public class StopClock extends Thread {
    private String formattedTime;
    private int time = 0;
    private Timer t = new Timer();
    private TimerTask task = new TimerTask() {
        public void run() {
            time++;
            int minutes = time / 6000;
            int seconds = (time - (minutes * 6000)) / 100;
            int centiseconds = time % 100;
            String minutesS;
            String secondsS;
            String centisecondsS;
            if (minutes < 10) {
                minutesS = "0" + minutes;
            } else {
                minutesS = Integer.toString(minutes);
            }
            if (seconds < 10) {
                secondsS = "0" + seconds;
            } else {
                secondsS = Integer.toString(seconds);
            }
            if (centiseconds < 10) {
                centisecondsS = "0" + centiseconds;
            } else {
                centisecondsS = Integer.toString(centiseconds);
            }

            formattedTime = minutesS + ":" + secondsS + ":" + centisecondsS;
        }
    };

    public void run() {
        t.scheduleAtFixedRate(task, 0, 10);
    }

    public StopClock() {
    }

    public String getFormattedTime() {
        return this.formattedTime == null ? "" : this.formattedTime;
    }
}