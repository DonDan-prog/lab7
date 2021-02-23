import java.io.BufferedReader;
import java.util.HashMap;

public final class HTTPResponce 
{
    private int statusCode;
    private HashMap<String, Object> values;

    public HTTPResponce(BufferedReader in) throws Exception
    {
        this.values = new HashMap<>();
        /** Read the first line, in HTTP it MUST contain status code */
        String line = in.readLine();
        statusCode = Integer.parseInt(line.split(" ")[1]);
        /** Read the rest lines of responce */
        while((line = in.readLine()).equals("") == false)
        {
            StringBuilder tempLine = new StringBuilder(line);
            int parameterSeparator = tempLine.indexOf(":");
            String key = tempLine.substring(0, parameterSeparator);
            String value = tempLine.substring(parameterSeparator + 2);
            values.put(key, value);
        }
    }
    public int getStatusCode() { return this.statusCode; }
    public Object getParameter(final String key)
    {
        return this.values.get(key);
    }
}