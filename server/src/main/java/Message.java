import java.io.Serializable;

public class Message implements Serializable {
    private String from;
    private String text;
    private final MessageConstants type;
    private String login;
    private String pass;
    private String oldName;
    private String newName;
    private String to;
    private static final long serialVersionUID = 7583547376216463224L;

    public Message(String from, MessageConstants type, String oldName, String newName) {
        this.from = from;
        this.type = type;
        this.oldName = oldName;
        this.newName = newName;
    }

    public Message(MessageConstants type) {
        this.type = type;
    }

    public Message(MessageConstants accepted, String name) {
        this.type = accepted;
        this.to = name;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }

    public Message(MessageConstants type, String from, String to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.type = type;
    }

    public Message(MessageConstants type, String oldName, String newName) {
        this.type = type;
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getText() {
        return from + ": " + text;
    }

    public MessageConstants getType() {
        return type;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
