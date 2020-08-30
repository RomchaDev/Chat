import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AuthorisationController {

    @FXML
    private Button enterBUTTON;

    @FXML
    private PasswordField passwordTEXT;

    @FXML
    private TextField loginTEXT;

    private boolean timeOut = false;

    private final Thread timeThread = new Thread(() -> {
        final int TIMEOUT_TIME = 120000;
        long time = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - time >= TIMEOUT_TIME) {
                timeOut = true;
                Platform.runLater(() -> showMessage("You waited to long...\nNow you have to restart the app"));
                break;
            }
        }
    });

    @FXML
    void initialize() {
        timeThread.start();
    }

    public void enterPressed() {
        if (!timeOut) {
            try {
                String name = Network.sendAuthMessage(loginTEXT.getText(), passwordTEXT.getText());
                if (name != null) {
                    timeThread.stop();
                    ClientStarter.getInstance().loadScene(Scenes.MAIN);
                    MainWindowController.getInstance().initHistoryFile();
                } else {
                    System.out.println("error");
                    JOptionPane.showMessageDialog(null, "This user has been already using the app or you have entered wrong data");
                }
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Some problems with server or your connection", "Problem", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            Platform.runLater(() -> showMessage("You can't connect now, restart the app to connect"));
        }
    }

    private void showMessage(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Problem");
        alert.setContentText(text);
        alert.showAndWait();
        loginTEXT.setDisable(true);
        passwordTEXT.setDisable(true);
        enterBUTTON.setDisable(true);
    }

    public void regPressed() {
        ClientStarter.getInstance().loadScene(Scenes.REGISTRATION);
    }
}
