import java.io.FileWriter;

public class WorkLogger 
{
    private static FileWriter log = null;

    private static boolean isCreated = false;

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

    public static void close()
    {
        if(isCreated == false) return;
        try { log.close(); } 
        catch (Exception e) { ErrorLogger.log("WorkLogger::close: " + e); 
        }
    }
}