import javax.swing.*;

public class MyNickField extends NickField {
    private static final MyNickField field = new MyNickField();

    private MyNickField() {
        super("Me(" + Client.getInstance().getName() + ')');
    }

    @Override
    protected void pressedAction() {
        String answer = JOptionPane.showInputDialog(null, "Enter new nickname", "Nick changing", JOptionPane.INFORMATION_MESSAGE);
        if (answer != null) {
            if (Client.getInstance().changeName(answer))
                field.getTextField().setText("Me(" + answer + ')');
            else JOptionPane.showMessageDialog(null, "You are trying to use someone's else's nick");
        }
    }

    public static synchronized MyNickField getInstance() {
        return field;
    }
}
