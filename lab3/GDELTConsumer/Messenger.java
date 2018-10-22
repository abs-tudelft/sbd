package lab3;

import java.io.IOException;
import java.util.Map;

import javax.websocket.Session;

import lab3.WebSocket;

public class Messenger extends Thread {
    protected Session session;

    public Messenger(Session session) {
        this.session = session;
    }

    public void run() {

        while (true) {
            try {
                String message = "";
                synchronized(WebSocket.messageQueue) {
                    for (Map.Entry<String, Long> entry : WebSocket.messageQueue.entrySet()) {
                        message += "{\"name\": \"" + entry.getKey() + "\", \"count\": " + entry.getValue() + "},\n";
                    }
                    WebSocket.messageQueue.clear();
                }

                if (message.length() > 0) {
                    try {
                        this.session.getBasicRemote().sendText("[" + message.substring(0, message.length() - 2) + "]");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                break;
            }
        }
    }
}
