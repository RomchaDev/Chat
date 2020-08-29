import java.io.Serializable;

public class Message implements Serializable {
    private String from;
    private String text;
    private final MessageConstants type;
    private String readyMessage;
    private String login;
    private String pass;
    private String to;
    private String oldName;
    private String newName;
    private static final long serialVersionUID = 7583547376216463224L;

    public Message(MessageConstants type, String from, String to, String text) {
        this.type = type;
        this.from = from;

        if (type == MessageConstants.REGISTRATION) {
            this.login = to;
            this.pass = text;
        } else {
            this.to = to;
            this.text = text;
        }
    }

    public Message(String from, MessageConstants type, String oldName, String newName) {
        this.from = from;
        this.type = type;
        this.oldName = oldName;
        this.newName = newName;
    }


    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }

    public Message(MessageConstants type, String login, String pass) {
        this.type = type;

        if (type == MessageConstants.NICK_CHANGING) {
            this.oldName = login;
            this.newName = pass;
        } else {
            this.login = login;
            this.pass = pass;
        }
    }

    public Message(MessageConstants type, String name) {
        this.type = type;
        this.to = name;
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
