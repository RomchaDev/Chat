import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class AccountNetwork implements Serializable {
    private transient final Socket socket;
    private final int id;
    private transient ObjectOutputStream output;
    private static final Server server = Server.getInstance();
    private boolean hasClientExited = false;

    public int getId() {
        return id;
    }

    public AccountNetwork(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
        Thread inputMessagesListener = new Thread(this::listenMessage);
        inputMessagesListener.start();
    }

    private void listenMessage() {
        Message message;
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            while (!hasClientExited) {
                    message = (Message) input.readObject();
                    System.out.println(message.getText());
                    server.notifyAboutClientMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            hasClientExited = true;
            server.deleteSubscriber(id - 1);
            System.out.println(MainSQLWorker.getNameOf(id) + " disconnected");
        }
    }

    public void sendMessageToThisClient(Message message) throws IOException {
        if (!hasClientExited) {
            output.flush();
            output.writeObject(message);
        }
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }
}
