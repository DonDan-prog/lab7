import java.io.BufferedReader;
import java.util.ArrayDeque;

public class HtmlParser 
{
    /** Constant prefixes */
    private final String APREFIX = "<a";
    private final String HREFPREFIX = "href=\"";
    /** The queue of crawler */
    private ArrayDeque<URLPair> queue;
    /** Simple - keep the link of queue in parser field */
    HtmlParser(ArrayDeque<URLPair> queue)
    {
        this.queue = queue;
    }
    /** Perfoming parse on the fly of inputted data */
    public void parse(BufferedReader in, URLPair urlPair, int depth) throws Exception
    {
        String line = "";
        while((line = in.readLine()) != null)
        {
            int aTagStart = line.indexOf(APREFIX);
            if(aTagStart == -1) continue;
            int hrefStart = line.indexOf(HREFPREFIX, aTagStart);
            if(hrefStart == -1) continue;
            int hrefEnd = line.indexOf("\"", hrefStart + HREFPREFIX.length());
            if(hrefEnd == -1) continue;

            String url = line.substring(hrefStart + HREFPREFIX.length(), hrefEnd);
            if(url.startsWith("#")) continue;
            /** This means that URL with hidden protocol */
            if(url.startsWith("//")) url = urlPair.getProtocol() + "://" + url;
            /** This means that URL with hidden domen */
            if(url.startsWith("/")) url = urlPair.getFullUrl() + url.substring(1);

            try
            {
                queue.add(new URLPair(url, depth));
            }
            catch(Exception e)
            {
                ErrorLogger.log("WebCrawler::WebCrawler: " + e + " caused from " + url);
            }
        }
    }
}