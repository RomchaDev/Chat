import javafx.application.Platform;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Network {
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8080;
    private static Socket connection;
    private boolean isAuthorised = false;
    private static final Network network = new Network();

    private Network() {
    }

    public static synchronized Network getInstance() {
        return network;
    }

    public void sendMessage(Message message) throws IOException {
        if (!isAuthorised)
            output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        output.writeObject(message);
        if (message.getType() == MessageConstants.SIMPLE) {
            MainWindowController.getInstance().addMessage(message);
        }
        isAuthorised = true;
    }

    private static void listenToMessages() {
        while (true) {
            Message message;
            try {
                message = (Message) input.readObject();
                if (message.getFrom() != null) {
                    if (!message.getFrom().equals(Client.getInstance().getName())) {
                        if (message.getType().equals(MessageConstants.SIMPLE))
                            MainWindowController.getInstance().addMessage(message);
                        else if (message.getType() == MessageConstants.AUTH)
                            Platform.runLater(() -> Client.getInstance().addNick(message.getFrom()));
                        else if (message.getType() == MessageConstants.EXIT)
                            Client.getInstance().deleteNick(message.getFrom());
                        else if (message.getType() == MessageConstants.NICK_CHANGING)
                            Client.getInstance().changeElseNick(message.getOldName(), message.getNewName());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Check your connection", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static String sendAuthMessage(String login, String password) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(HOST, PORT);
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        out.writeObject(new Message(MessageConstants.AUTH, login, password));
        Message message = (Message) in.readObject();
        if (message.getType() == MessageConstants.ACCEPTED) {
            connection = socket;
            input = in;
            Client.getInstance().setName(message.getTo());
            new Thread(Network::listenToMessages).start();
            return message.getTo();
        } else {
            return null;
        }

    }
}
