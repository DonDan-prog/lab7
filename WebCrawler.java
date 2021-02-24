import java.util.HashMap;
import java.util.Set;
import java.util.ArrayDeque;

public class WebCrawler 
{
    /** Queue of remaining sites to crawl */
    private ArrayDeque<URLPair> queue;
    /** The map of visited; used Map instead of Set because I'll port this program into multithread */
    private HashMap<URLPair, Integer> map;
    /** Single parser for current WebCrawler */
    private HtmlParser parser;
    /** The specified start url pair */
    private URLPair urlStart;
    /** The specified maxDepth */
    private int maxDepth;
    /** Is the crawler was created */
    private boolean isCreated;

    WebCrawler(String urlString, int maxDepth)
    {
        /** We try to initilaze all the class variables;
         *  If something failed, we null everything and set the isCreated to false
         */
        try 
        {
            this.urlStart = new URLPair(urlString, 0);
            this.maxDepth = maxDepth;

            this.queue = new ArrayDeque<URLPair>();
            this.map = new HashMap<URLPair, Integer>();

            this.parser = new HtmlParser(queue);

            this.isCreated = true;
        }
        catch (Exception e) 
        {
            this.queue = null;
            this.map = null;
            this.parser = null;
            this.urlStart = null;
            this.maxDepth = 0;

            this.isCreated = false;

            ErrorLogger.log("WebCrawler::WebCrawler: " + e);
        }
    }
    /** Method for crawl the whole site */
    public void crawlSite()
    {
        /** If not created just return */
        if(this.isCreated == false) return;
        /** Init the logger */
        WorkLogger.log("WebCrawler start at site " + this.urlStart.getFullUrl() + " and maxDepth = " + this.maxDepth);
        WorkLogger.log("====== START ======");
        WorkLogger.log("");
        /** Save the start time of crawling */
        long startTime = System.currentTimeMillis();
        /** Add the first page to crawl - it's the start url itself */
        this.queue.add(this.urlStart);
        for(int i = 0; i < this.maxDepth && this.queue.size() > 0; i++)
        {
            WorkLogger.log("===== DEPTH " + i + " ======");

            int remainLen = this.queue.size();
            for(int j = 0; j < remainLen; j++)
                this.crawlOne(i + 1);

            WorkLogger.log("");
        }

        WorkLogger.log("====== END ======");
        WorkLogger.log("Time elapsed: " + (System.currentTimeMillis() - startTime)/100.);
        WorkLogger.log("Total visited sites: " + this.map.size());

    }
    public Set<URLPair> getVisited() { return map.keySet(); }

    private void crawlOne(int currentDepth)
    {
        try 
        {
            URLPair urlPair = queue.pop();

            if(map.containsKey(urlPair) == true) 
                throw new Exception("already visited");
            map.put(urlPair, 0);

            WorkLogger.log(urlPair.toString());

            Request request = new Request(urlPair);
            request.sendGET();
            HTTPResponce responce = new HTTPResponce(request.getBufferedReader());
            int statusCode = responce.getStatusCode();

            if(statusCode == 200)
                this.parser.parse(request.getBufferedReader(), urlPair, currentDepth);
            else if(statusCode == 301)
                queue.add(new URLPair(responce.getParameter("Location:"), currentDepth));
            request.close();
        }
        catch (Exception e) 
        {
            ErrorLogger.log("WebCrawler::WebCrawler: " + e);
        }
    }
}