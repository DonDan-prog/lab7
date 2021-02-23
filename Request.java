import java.net.Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

public class Request 
{
    /** The request socket; must be closed after request */
    private Socket socket;
    /** The streams for read/write in socket */
    private PrintWriter out;
    private BufferedReader in;
    /** URLPair handle for particular request */
    private URLPair url;

    /** Constructor that takes URLPair and tries to make socket with given url and port */
    Request(URLPair url) throws Exception
    {
        this.url = url;

        this.socket = new Socket(this.url.getHost(), this.url.getPort());
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }
    public BufferedReader getBufferedReader() { return this.in; }
    public void sendGET(String path)
    {
        out.println("GET " + this.url.getPath() + " HTTP/1.1");
        out.println("Host: " + this.url.getHost() + ':' + this.url.getPort());
        out.println("Connection: Close");
        out.println("");
        out.flush();
    }

    public void close() throws Exception
    {
        this.out.close();
        this.in.close();
        this.socket.close();
    }
}
