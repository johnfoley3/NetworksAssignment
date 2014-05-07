import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class CodeServer {

   // Don't everybody use this port number!  It will cause havoc.
   public static final int PORT = 32566;

   private static final boolean DEBUG = true;

   public static void main(String[] args)
   {

      boolean goodSock = true;

      /* Create a new ServerSocket object.  A ServerSocket object
       * is used to perform a passive open.
       */
      ServerSocket serverSocket = null;
      try {
         serverSocket = new ServerSocket(PORT);
      } catch (IOException e) {
         System.out.println ("Error creating server's socket.");
         System.exit(-1);
      }

      if (DEBUG) {
         System.out.println ("Server starting up ...");
      }

      /* The server will run forever. */
      while (true) {

         /* Wait for a client to connect to the server socket.
          * Accept() will wait here until a client connects from
          * the remote end. 
          */
         if (DEBUG) {
            System.out.println (
                  "Server waiting for connection for remote client.");
         }

         Socket connection = null;
         try {
            connection = serverSocket.accept();
         } catch (IOException e) {
            System.out.println ("Error creating connection.");
            goodSock = false;
         }

         /* Once the client connects, the connection will be established
          * and we can read and write on the servSock connection.
          * The accept() method returns the identity of the client
          * socket (remote IP number:remove port).  We may not care
          * who is on the other end.  Here, we use it for debugging.
          */
         if (DEBUG && goodSock) {
            System.out.println ("Server accepting connection from client \"" 
                  + connection.getRemoteSocketAddress()
                  + "\"");
         }

         /* Create streams and attach them to the connection so we can
          * read and write on it. */
         DataInputStream in = null;
         DataOutputStream out = null;

         try {
            in = new DataInputStream(connection.getInputStream());
            out = new DataOutputStream (connection.getOutputStream());
         } catch (IOException e) {
            System.out.println (
                  "Can't attach one of the streams to the connection.");
            /* Don't exit.  We are a server.  It's just this one request
             * that has failed.
             */
            goodSock = false;
         }

         if (goodSock) {

            /* Grab an int and then a String of that length, translate,
             * and send the translated word back.
             */
            String originalWord;
            try {
               originalWord = readStringFromSock (in);
            } catch (IOException e) {
               System.out.println ("Error reading String.");
               originalWord = "--BOGUS--";
            }

            if (DEBUG) {
               System.out.println ("Server recieved \""
                     + originalWord 
                     + "\" from client.");
            }

            try {
               writeStringToSock (
                     out, 
                     Translator.piglatin( originalWord ) );
            } catch (IOException e) {
               System.out.println (
                     "Error writing translated String to socket.");
            }

            if (DEBUG) {
               System.out.println ("Server sending \""
                     + Translator.piglatin( originalWord )
                     + "\" to client.");
            }

            /* Close the socket */
            try {
               connection.close();
            } catch (IOException e) {
               System.out.println ("Error closing socket.");
               // Just ditch the connection object.
               connection = null;
            }

         }


      }

   }

   /** Send the size of a String and then the String itself
    * to the server on the other end of the connection. 
    * @param out the output stream of the socket
    * @param s the string
    * @throws IOException
    */ 
   static void writeStringToSock (DataOutputStream out, String s)
         throws IOException
         {
      /* Remember that the TCP connection will only carry bytes.  
       * DataOuputStream.write() will help us here by transforming the
       * primitive types (boolean, char, int, long, etc.) into
       * bytes.  However, we need to use other methods to transmit
       * objects.  WriteChars() is used to transform and then
       * write a String object to the socket.
       */
      out.writeInt (s.length());
      out.writeChars (s);
         }

   /** Read a size and a String of that size from a socket attached
    * to a DataInputStream.
    * @param in the DataInputStream attached to a socket.
    * @return the String
    * @throws IOException
    */
   static String readStringFromSock (DataInputStream in) 
         throws IOException	
         { 
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
