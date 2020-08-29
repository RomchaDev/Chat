import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RegistrationController {
    @FXML
    private TextField nickTEXT;

    @FXML
    private TextField passwordTEXT;

    @FXML
    private TextField loginTEXT;
    private static RegistrationController registrationController;

    @FXML
    private void registerPressed() {
        try {
            Socket conn = new Socket("127.0.0.1", 8080);
            ObjectOutputStream out = new ObjectOutputStream(conn.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(conn.getInputStream());
            out.flush();
            out.writeObject(new Message(MessageConstants.REGISTRATION, nickTEXT.getText(), loginTEXT.getText(), passwordTEXT.getText()));
            Message message = (Message) in.readObject();

            if (message.getType() == MessageConstants.ACCEPTED) {
                JOptionPane.showMessageDialog(null, "Success!", "Success", JOptionPane.INFORMATION_MESSAGE);
                ClientStarter.getInstance().loadScene(Scenes.AUTHORISATION);
            }

            in.close();
            out.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Problems with server", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        registrationController = this;
    }

    public static RegistrationController getInstance() {
        return registrationController;
    }

}
