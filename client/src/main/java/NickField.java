import javafx.geometry.NodeOrientation;
import javafx.scene.control.TextField;


public class NickField {
    private final TextField field;

    public NickField(String name) {
        field = new TextField(name);
        field.setEditable(false);
        field.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        field.setOnMouseClicked(event -> pressedAction());
    }

    protected void pressedAction() {
        MainWindowController.getClient().setGetter(field.getText());
    }


    public final TextField getTextField() {
        return field;
    }

    public final void setActive(boolean b) {
        if (b) {
            field.setStyle("-fx-background-color: #0000ff");
        } else {
            field.setStyle("-fx-background-color: #fafafa");
        }
    }

    public void changeNameTo(String newName) {
        this.field.setText(newName);
    }
}
