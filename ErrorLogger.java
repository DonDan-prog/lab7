import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorLogger 
{
    private static FileWriter log = null;
    private static SimpleDateFormat formatter = new SimpleDateFormat("[yyy-MM-dd 'at' HH:mm::ss] ");

    static void init(String logName) 
    {
        try 
        {
            log = new FileWriter(logName);
        } 
        catch (Exception e) 
        {
            log = null;
        }
    }

    public static void log(String toWrite) 
    {
        if(log == null) return;
        try 
        {
            log.write(formatter.format(new Date(System.currentTimeMillis())));
            log.write(toWrite);
            log.write("\r\n");
            log.flush();
        } 
        catch (Exception e) {}
    }

    public static void close()
    {
        if(log == null) return;
        try { log.close(); } 
        catch (Exception e) { }
    }
}