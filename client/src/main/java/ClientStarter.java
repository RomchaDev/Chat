import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientStarter extends Application {
    private Scene mainSENE;
    private Scene authSCENE;
    private Scene regSCENE;
    private Stage stage;
    private static ClientStarter clientStarter;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
        Parent auth = FXMLLoader.load(getClass().getResource(Scenes.AUTHORISATION.getRoot()));
        Parent root = FXMLLoader.load(getClass().getResource(Scenes.MAIN.getRoot()));
        Parent reg = FXMLLoader.load(getClass().getResource(Scenes.REGISTRATION.getRoot()));
        mainSENE = new Scene(root, 900, 600);
        authSCENE = new Scene(auth, 900, 600);
        regSCENE = new Scene(reg, 900, 600);
        stage.setTitle("Chat");
        stage.setScene(authSCENE);
        stage.show();
        clientStarter = this;
        stage.setOnCloseRequest(e -> {
            MainWindowController.getInstance().endLogging();
            System.exit(0);
        });
    }

    public static synchronized ClientStarter getInstance() {
        return clientStarter;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void loadScene(Scenes scene) {
        if (scene == Scenes.AUTHORISATION)
            stage.setScene(authSCENE);
        else if (scene == Scenes.MAIN)
            stage.setScene(mainSENE);
        else
            stage.setScene(regSCENE);
    }
}
