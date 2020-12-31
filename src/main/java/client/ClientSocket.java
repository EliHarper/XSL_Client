package client;

import msg.AuditMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientSocket {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            System.out.println("Caught an unexpected exception while starting the connection: "
            + Arrays.toString(e.getStackTrace()));
        }
    }

    public String sendMessage(String msg) {
        try {
            out.println(msg);
            String response = in.readLine();
            return response;
        } catch (IOException ioe) {
            System.out.println("Caught an IOException in sendMessage: " +
                    Arrays.toString(ioe.getStackTrace()));
            return "Failed to send message..";
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException ioe) {
            System.out.println("Shit hit the fan when closing.. ioe msg: " +
                    ioe.getMessage());
        }
    }

    // Static, non-sockety funcers:
    public static AuditMessage unmarshal(String msg) {
        System.out.println("In unmarshal, msg: " + msg + "\n\n");

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AuditMessage.class);
            Unmarshaller jaxUnmarshaller = jaxbContext.createUnmarshaller();

            InputStream msgStream = new ByteArrayInputStream(msg.getBytes());
            return (AuditMessage) jaxUnmarshaller.unmarshal(msgStream);
        } catch (JAXBException jax) {
            System.out.println("Porblums hit while unmarshalling: " +
                    Arrays.toString(jax.getStackTrace()));
        }

        return null;
    }

    public static String decode(AuditMessage msg) {
        Base64.Decoder decoder =  Base64.getDecoder();
        return new String(decoder.decode(msg.getContents()));
    }

    public static boolean isXMLLike(String inXMLStr) {
        boolean retBool = false;

        if (inXMLStr.startsWith("<")) {
            retBool = true;
        }

        return retBool;
    }

    public static String readIn() {
        Scanner s = new Scanner(System.in);
        System.out.print("Gimme a message:\t");
        String origMsg = s.nextLine();
        s.close();

        return origMsg;
    }

    public static void main(String[] args) {
        ClientSocket client = new ClientSocket();

        // Take input from the client to send to the server/encoded and set back/decoded:
        String origMsg = readIn();
        String ip = "127.0.0.1";
        int port = 8008;

        client.startConnection(ip, port);

        String xmlResponse;

        while (true) {
            xmlResponse = client.sendMessage(origMsg);

            if (isXMLLike(xmlResponse))
                break;
        }

        AuditMessage encodedMsgObj = unmarshal(xmlResponse);

        assert encodedMsgObj != null;
        String plainMsg = decode(encodedMsgObj);

        System.out.println("Here's your original message: " + plainMsg);
    }
}
