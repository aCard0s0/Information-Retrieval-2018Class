package main;

import magement.Memory;
import magement.Timer;

/**
 *  Frist Assigment for Retrivel Information Class.
 * 
 *  @author André Cardoso, 65069 & Ivo Angélico, 41351
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
        Memory mem = new Memory();
        Timer time = new Timer();
        mem.printMemory();
        time.startTimer();

        App app = new App();
        app.start();

        time.printDuration();
        mem.printMemory();
    }
}
