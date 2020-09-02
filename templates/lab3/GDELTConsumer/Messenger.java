package lab3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.websocket.Session;

import lab3.WebSocket;

public class Messenger extends Thread {
    public Map<String, Long> messageQueue = Collections.synchronizedMap(new LinkedHashMap<String, Long>());
    public Map<String, Long> histogram = Collections.synchronizedMap(new LinkedHashMap<String, Long>());
    protected List<Session> connections = Collections.synchronizedList(new ArrayList<>());

    public void run() {
        while (true) {
            try {
                synchronized(this) {
                    String message = this.getMessage(this.messageQueue);
                    if (message.length() > 0) this.sendAll(message);
                    this.messageQueue.clear();
                }
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                break;
            }
        }
    }

    protected String getMessage(Map<String, Long> records) {
        String message = "";
        for (Map.Entry<String, Long> entry : records.entrySet()) {
            message += "{\"name\": \"" + entry.getKey() + "\", \"count\": " + entry.getValue() + "},\n";
        }
        if (message.length() > 0) {
            return "[" + message.substring(0, message.length() - 2) + "]";
        }
        else {
            return "";
        }
    }

    protected void send(String message, Session session) {
        session.getAsyncRemote().sendText(message);
    }

    protected void sendAll(String message) {
        for (Session session : this.connections) {
            this.send(message, session);
        }
    }

    public void register(Session session) {
        this.connections.add(session);
        synchronized(this.histogram) {
            String message = getMessage(this.histogram);
            if (message.length() > 0) this.send(message, session);
        }
    }

    public void deregister (Session session) {
        this.connections.remove(session);
    }
}
