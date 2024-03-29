import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;

/** Demonstrate the client side of a simple client-server system. */
public class CodeClient {

    public static final int PORT = 32566;

    public static final int MAX_BUF_SIZE = 256;


    /**
     * Open a TCP connection and send a String to the
     * server on the other end.  Then, read the server's reply,
     * which should be a translation of this string into texting
     * shorthand.
     *
     * @param args
     *            server name
     */
    public static void main(String[] args) {
        // Get server name from command line
        String server = args[0];
        Scanner scan = new Scanner(System.in);

        // Enter first input
        System.out.println("Please enter some text:");
        String toTranslate = scan.nextLine();

        while (!toTranslate.equals("END")) {

          /*
           * Open a TCP connection with a remote socket address of
           * server:PORT.  The local socket address is determined
           * automagically by the Socket constructor.
           */
            Socket connection = null;
            try {
                connection = new Socket(server, PORT);
            } catch (UnknownHostException e) {
                System.out.println("Unknown server " + server + ".");
                System.exit(-1);
            } catch (IOException e) {
                System.out.println("Error opening socket.");
                System.exit(-1);
            }

          /*
           * At this point, the connection is established,
           * but we have no way of reading and writing to it.
           * Attach the input and output parts of the
           * connection to streams.
           */
            DataInputStream in = null;
            DataOutputStream out = null;

          /*
           * The socket may have closed between the time we
           * established the connection and the time we attach
           * to its input side. If so, the DataInputStream
           * constructor will throw an IOException.
           */
            try {
                in = new DataInputStream(connection.getInputStream());
            } catch (IOException e) {
                System.out.println("Error attaching to input side of socket.");
                System.exit(-1);
            }

            try {
                out = new DataOutputStream(connection.getOutputStream());
            } catch (IOException e) {
                System.out.println("Error attaching to output side of socket.");
                System.exit(-1);
            }

          /*
           * Any read or write from the socket will through
           * an exception if the other end closed the socket
           * unexpectedly (for example, if the server crashed.)
           * So, all IO operations need to be done in a try/catch block.
           */
            try {
                writeStringToSock(out, toTranslate);
            } catch (IOException e) {
                System.out.println("Error writing String to socket.");
                System.exit(-1);
            }

            String reply = null;
            try {
                reply = readStringFromSock(in);
            } catch (IOException e) {
                System.out.println("Error reading String from socket.");
                System.exit(-1);
            }

            System.out.println("TRANSLATION: " + reply);

            // Clean up
            in = null;
            out = null;

            try {
                connection.close();
            } catch (IOException e) {
                System.out.println("Error closing socket.");
                System.exit(-1);
            }

            // Enter first input
            System.out.println("Please enter some text:");
            toTranslate = scan.nextLine();

        }
    }

    /**
     * Send the size of a String and then the String itself to
     * the server on the other end of the connection.
     *
     * @param out
     *            the output stream of the socket
     * @param s
     *            the string
     * @throws IOException
     */

    static void writeStringToSock(DataOutputStream out, String s)
            throws IOException {
      /*
       * Remember that the TCP connection will only carry bytes.
       * DataOuputStream.write() will help us here by transforming 
       * the primitive types (boolean, char, int, long, etc.) into 
       * bytes. However, we need to use other methods to transmit 
       * objects. WriteChars() is used to transform and then write
       * a String object to the socket.
       */
        out.writeInt(s.length());
        out.writeChars(s);
    }

    /**
     * Read a size and a String of that size from a socket attached
     * to a DataInputStream.
     *
     * @param in the DataInputStream attached to a socket.
     * @return the String
     * @throws IOException
     */
    static String readStringFromSock(DataInputStream in)
            throws IOException {
        // First, determine the size of the String being sent.
        int wordLength = in.readInt();
        // Now, read the string itself, one character at a time.
        String reply = "";
        for (int i = 0; i < wordLength; i++) {
            reply += in.readChar();
        }
        return reply;
    }

}
