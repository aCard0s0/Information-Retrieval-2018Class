package magement;

/**
 *  To access the memory currently being consumed.
 * 
 *  @author André Cardoso, 65069 & Ivo Angélico, 41351
 */
public class Memory {
    
    private Runtime runtime;
    
    public Memory()
    {
        runtime = Runtime.getRuntime();
    }
    
    /**
     * Get the amount of memory currently being consumed.
     * @return Memory in Mbs.
     */
    public long getCurrentMemory()
    {
        return (runtime.totalMemory() - runtime.freeMemory()) / 1000000;
    }
    
    /**
     * Print the amount of memory currently being consumed in Mbs.
     */
    public void printMemory(){
        System.out.println("Memory consumed: "+ ((runtime.totalMemory() - runtime.freeMemory()) / 1000000) +"Mbs");
    }
}