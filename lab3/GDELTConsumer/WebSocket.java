package lab3;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collections;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import lab3.Messenger;

@ServerEndpoint(value = "")
public class WebSocket {
    public static Map<String, Long> messageQueue = Collections.synchronizedMap(new LinkedHashMap<String, Long>());
    public static int numConnections = 0;
    private Messenger messenger;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Connection opened.");
        WebSocket.numConnections += 1;
        this.messenger = new Messenger(session);
        this.messenger.start();
        // WebSocket.messageQueue.offer("{\"name\": \"Donald Trump\", \"increment\": 5}");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Handle new messages
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("Connection closed.");
        WebSocket.numConnections -= 1;
        this.messenger.interrupt();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
}
