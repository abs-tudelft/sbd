package lab3;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import lab3.Messenger;

@ServerEndpoint(value = "")
public class WebSocket {
    public static Messenger messenger = new Messenger();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        System.out.println("Connection opened.");
        messenger.register(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // Handle new messages
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        System.out.println("Connection closed.");
        messenger.deregister(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
}
