package magement;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Simple class to manager execution time.
 * 
 *  @author André Cardoso, 65069 & Ivo Angélico, 41351
 */
public class Timer {

    private long startTime;
    private long stopTime;

    public Timer() {
        this.startTime =System.nanoTime();
        this.stopTime = -1;
    }

    /**
     *  Save the time that method was invoked.
     */
    public void stopTimer() {
        this.stopTime = System.nanoTime();
    }

    public String getCurrentTime() {

        return new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(
            (System.nanoTime() - this.startTime) / 1000000
        ));
    }

    public void printCurrentTime() {
        
        System.out.println(
            new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(
                (System.nanoTime() - this.startTime) / 1000000
            ))
        );
    }

    /**
     *  Print time duration since the startTimer() method was last invoked or error otherwise.
     *  If stopTimer() method wasn't call, this method will take care off it.
     */
    public void printTotalDuration() {

        stopTimer();
        System.out.println("Execution duration: "+ ((this.stopTime - this.startTime) / 1000000) +"ms");
    }
}