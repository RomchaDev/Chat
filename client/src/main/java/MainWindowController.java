import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class MainWindowController {
    private static final Client client = Client.getInstance();
    private static MainWindowController mainWindowController;
    private File historyFile;
    private int currentLoggingStringsAmount = 0;
    private final List<String> historyList = new LinkedList<>();

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
        currentLoggingStringsAmount++;
        viewMessageTEXT.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        viewMessageTEXT.appendText(message.getText() + '\n');

        int MAX_LOGGING_STRINGS_AMOUNT = 100;
        if (currentLoggingStringsAmount >= MAX_LOGGING_STRINGS_AMOUNT)
            historyList.remove(0);

        historyList.add(message.getText() + '\n');
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
        try {
            PrintWriter historyWriter = new PrintWriter(new FileOutputStream(historyFile));


            for (String s : historyList)
                historyWriter.write(s);

            historyWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {
        }

    }

    public void initHistoryFile() throws IOException {
        historyFile = new File(Client.getInstance().getName() + "MessagesHistory.txt");
        if (!historyFile.createNewFile()) {
            BufferedReader historyReader = new BufferedReader(new FileReader(historyFile));
            while (true) {
                String s = historyReader.readLine();
                if (s == null) {
                    historyReader.close();
                    break;
                }

                viewMessageTEXT.appendText(s);
                viewMessageTEXT.appendText("\n");
                currentLoggingStringsAmount++;
            }
        }
    }

    public void renameHistoryFileTo(String newName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(historyFile));
            StringBuilder oldFileText = new StringBuilder();

            while (true) {
                String s = reader.readLine();

                if (s == null) {
                    reader.close();
                    break;
                }

                oldFileText.append(s);
            }

            historyFile.delete();
            historyFile = new File(newName + "MessagesHistory.txt");
            PrintWriter pw = new PrintWriter(historyFile);
            pw.write(oldFileText.toString());
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
