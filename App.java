public class App
{
    public static void main(String[] args) 
    {
        String input = "http://www.google.com/";
        try
        {
            URLPair url = new URLPair(input, 0);

            Request request = new Request(url);
            request.sendGET("/");
            HTTPResponce responce = new HTTPResponce(request.getBufferedReader());

            int statusCode = responce.getStatusCode();
            switch (statusCode)
            {
                case 200:
                    System.out.println("Success get");
                    break;
                default:
                    throw new Exception("main: status code " + statusCode);
            }
            request.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}