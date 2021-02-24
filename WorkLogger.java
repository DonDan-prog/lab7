import java.io.FileWriter;

/** The logger to write the url's that was visited */
public class WorkLogger
{
    /** Log file descriptor */
    private static FileWriter log = null;
    /** The flag to keep track is logger initialized
     *  Because if not, we do not perfom any operations with file
     */
    private static boolean isCreated = false;

    /** Initialize with given name */
    static void init(String logName) 
    {
        try 
        {
            log = new FileWriter(logName);
            isCreated = true;
        } 
        catch (Exception e) 
        {
            isCreated = false;
        }
    }
    /** Log the given string */
    public static void log(String toWrite) 
    {
        if (isCreated == false)
            return;
        try 
        {
            log.write(toWrite);
            log.write("\r\n");
            log.flush();
        } 
        catch (Exception e) 
        {
            ErrorLogger.log("WorkLogger::write: " + e);
        }
    }
    /** Close the file */
    public static void close()
    {
        if(isCreated == false) return;
        try { log.close(); } 
        catch (Exception e) { ErrorLogger.log("WorkLogger::close: " + e); 
        }
    }
}