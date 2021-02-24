public class App
{
    public static void main(String[] args) 
    {
        ErrorLogger.init("error.txt");

        String input = "http://bio.acousti.ca/";

        WorkLogger.init("log.txt");
        WebCrawler crawler = new WebCrawler(input, 2);
        crawler.crawlSite();
        for(URLPair i : crawler.getVisited())
           System.out.println(i);

        WorkLogger.close();
        ErrorLogger.close();
    }
}