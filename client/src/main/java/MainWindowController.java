import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.*;

public class MainWindowController {
    private static final Client client = Client.getInstance();
    private static MainWindowController mainWindowController;
    private File log = new File("log.txt");
    private PrintWriter logWriter;

    @FXML
    private VBox nickBox;

    @FXML
    private TextArea viewMessageTEXT;

    @FXML
    private TextArea sendMessageTEXT;

    public static Client getClient() {
        return client;
    }

    public static MainWindowController getInstance() {
        return mainWindowController;
    }

    private

    @FXML
    void initialize() {
        mainWindowController = this;
    }

    public void addUser(NickField field) {
        nickBox.getChildren().add(field.getTextField());
    }

    public void addMessage(Message message) {
        viewMessageTEXT.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        viewMessageTEXT.appendText(message.getText() + '\n');
        logWriter.write(message.getText() + '\n');
    }

    public void sendButtonPressed() {
        client.sendSimpleMessage(sendMessageTEXT.getText());
        sendMessageTEXT.clear();
    }

    public void deleteUser(NickField field) {
        for (Node node : nickBox.getChildren()) {
            TextField fieldFromBox = (TextField) node;
            if (fieldFromBox.getText().equals(field.getTextField().getText()))
                Platform.runLater(() -> nickBox.getChildren().remove(field.getTextField()));
        }

    }

    public void endLogging() {
        logWriter.close();
    }
}
