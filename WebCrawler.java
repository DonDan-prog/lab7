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
            /** Log new step */
            WorkLogger.log("===== DEPTH " + i + " ======");
            /** Get the remain length of queued pages, than run exactly this amount of times the crawlOne */
            int remainLen = this.queue.size();
            for(int j = 0; j < remainLen; j++)
                this.crawlOne(i + 1);
            /** Add extra skip in log file */
            WorkLogger.log("");
        }
        /** End the log file and add the time elapsed and total sites visited */
        WorkLogger.log("====== END ======");
        WorkLogger.log("Time elapsed: " + (System.currentTimeMillis() - startTime)/100.);
        WorkLogger.log("Total visited sites: " + this.map.size());

    }
    /** Method that returns the set of sites to print them all in console */
    public Set<URLPair> getVisited() { return map.keySet(); }
    /** The method to crawl the one page */
    private void crawlOne(int currentDepth)
    {
        try 
        {
            /** Extract the crawling page from the queue */
            URLPair urlPair = queue.pop();
            /** Check if this site already visited */
            if(map.containsKey(urlPair) == true) 
                throw new Exception("already visited");
            /** If not - add to visited */
            map.put(urlPair, 0);
            /** Add to log that site is visiting */
            WorkLogger.log(urlPair.toString());
            /** Make new request */
            Request request = new Request(urlPair);
            /** Send GET request */
            request.sendGET();
            /** Parse the responce */
            HTTPResponce responce = new HTTPResponce(request.getBufferedReader());
            /** Get the status code of responce */
            int statusCode = responce.getStatusCode();
            /** Check for responces that we can handle with;
             *  Status code 200 - OK, parse the responce
             *  Status code 301 - redirect, we can handle it
             */
            if(statusCode == 200)
                this.parser.parse(request.getBufferedReader(), urlPair, currentDepth);
            else if(statusCode == 301)
                queue.add(new URLPair(responce.getParameter("Location:"), currentDepth));
            /** Close the request as we already done all we can with it */
            request.close();
        }
        catch (Exception e) 
        {
            /** Log the error */
            ErrorLogger.log("WebCrawler::WebCrawler: " + e);
        }
    }
}