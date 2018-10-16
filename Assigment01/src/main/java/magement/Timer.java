package magement;

/**
 *  Simple class to manager execution time.
 * 
 *  @author André Cardoso, 65069 & Ivo Angélico, 41351
 */
public class Timer {

    private long startTime;
    private long stopTime;

    public Timer() {
        this.startTime = -1;
        this.stopTime = -1;
    }

    /**
     *  Save the time that method was invoked.
     */
    public void startTimer() {
        this.startTime = System.nanoTime();
    }

    /**
     *  Save the time that method was invoked.
     */
    public void stopTimer() {
        this.stopTime = System.nanoTime();
    }

    /**
     *  Print time duration since the startTimer() method was last invoked or error otherwise.
     *  If stopTimer() method wasn't call, this method will take care off it.
     */
    public void printDuration() {

        if(this.startTime == -1){
            System.err.println("Timer not started!");
            return ;
        }

        if(this.stopTime == -1){
            stopTimer();
        }

        System.out.println("Execution duration: "+ ((this.stopTime - this.startTime) / 1000000) +"ms");
    }
}