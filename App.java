public class App
{
    public static void main(String[] args) 
    {
        ErrorLogger.init("error.txt");

        if(args.length > 1)
        {
            if(args.length == 3) WorkLogger.init(args[2]);
            else WorkLogger.init("log.txt");

            WebCrawler crawler = new WebCrawler(args[0], Integer.parseInt(args[1]));
            crawler.crawlSite();

            for(URLPair i : crawler.getVisited())
                System.out.println(i);
            
            WorkLogger.close();
        }
        else
        {
            ErrorLogger.log("Error in launcging program: didn't use command args");
            System.out.println("Usage: java App.java <full url> <maxDepth> [log name]");
        }
        ErrorLogger.close();
    }
}