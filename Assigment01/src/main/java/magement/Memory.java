package magement;

/**
 *  To access the memory currently being consumed.
 * 
 *  @author André Cardoso, 65069 & Ivo Angélico, 41351
 */
public class Memory {
    
    private Runtime runtime;
    private int maxMem;                 /** max memory reach */
    private double maxUsage;            /** max memory usage precentagem */
    
    public Memory()
    {
        runtime = Runtime.getRuntime();
    }

    public Memory(int maxMem, double maxUsage)
    {
        this.maxMem = maxMem;
        this.maxUsage = maxUsage;
        runtime = Runtime.getRuntime();
    }

    /**
     * 
     * @return true if max memory defined is reaching 85%
     */
    public boolean isHighUsage() {
        return getCurrentMemory() >= (this.maxMem * this.maxUsage);
    }
    
    /**
     * Get the amount of memory currently being consumed.
     * @return Memory in Mbs.
     */
    public long getCurrentMemory()
    {
        return (runtime.totalMemory() - runtime.freeMemory()) / (1024L * 1024L);
    }
    
    /**
     * Print the amount of memory currently being consumed in Mbs.
     */
    public void printMemory(){
        System.out.println("Memory consumed: "+ ((runtime.totalMemory() - runtime.freeMemory()) / (1024L * 1024L)) +"Mbs");
    }
}