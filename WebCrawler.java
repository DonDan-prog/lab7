import java.util.HashMap;
import java.util.Set;
import java.util.ArrayDeque;

public class WebCrawler 
{
    private ArrayDeque<URLPair> queue;
    private HashMap<URLPair, Integer> map;

    private HtmlParser parser;

    private URLPair urlStart;
    private int maxDepth;

    private boolean isCreated;

    private long startTime;

    WebCrawler(String urlString, int maxDepth)
    {
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

    public void crawlSite()
    {
        if(this.isCreated == false) return;

        WorkLogger.log("WebCrawler start at site " + this.urlStart.getFullUrl() + " and maxDepth = " + this.maxDepth);
        WorkLogger.log("====== START ======");
        WorkLogger.log("");

        this.startTime = System.currentTimeMillis();
        this.queue.add(this.urlStart);
        for(int i = 0; i < this.maxDepth && this.queue.size() > 0; i++)
        {
            WorkLogger.log("===== DEPTH " + i + " ======");
            WorkLogger.log("");

            int remainLen = this.queue.size();
            for(int j = 0; j < remainLen; j++)
                this.crawlOne(i + 1);

            WorkLogger.log("");
        }

        WorkLogger.log("====== END ======");
        WorkLogger.log("Time elapsed: " + (System.currentTimeMillis() - this.startTime)/100.);
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