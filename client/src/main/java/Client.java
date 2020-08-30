import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private static final Client CLIENT = new Client();
    private String name;
    private String getter = "ALL";
    private final Map<String, NickField> nicks = new HashMap<>();

    private Client() {
    }

    public String getName() {
        return name;
    }

    public void sendSimpleMessage(String text) {
        if (text != null && !text.equals(""))
            try {
                Network.getInstance().sendMessage(new Message(MessageConstants.SIMPLE, name, getter, text));
                if (!getter.equals("ALL")) {
                    nicks.get(getter).setActive(false);
                    getter = "ALL";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void setGetter(String getter) {
        if (!this.getter.equals(getter)) {
            for (String s : nicks.keySet())
                nicks.get(s).setActive(false);
            nicks.get(getter).setActive(true);
            this.getter = getter;
        } else {
            this.getter = "ALL";
            nicks.get(getter).setActive(false);
        }
    }

    public static Client getInstance() {
        return CLIENT;
    }

    public void addNick(String name) {
        NickField field = new NickField(name);
        nicks.put(name, field);
        MainWindowController.getInstance().addUser(field);
    }

    public void setName(String name) {
        this.name = name;
        MainWindowController.getInstance().addUser(MyNickField.getInstance());
    }

    public void deleteNick(String name) {
        MainWindowController.getInstance().deleteUser(nicks.get(name));
        nicks.remove(name);
    }


    public boolean changeName(String newName) {
        if (nicks.get(newName) == null) {
            try {
                Network.getInstance().sendMessage(new Message(MessageConstants.NICK_CHANGING, this.name, newName));
                this.name = newName;
                MainWindowController.getInstance().renameHistoryFileTo(newName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    public void changeElseNick(String oldName, String newName) {
        System.out.println(oldName + " " + newName);
        if (!oldName.equals(name) && !newName.equals(name)) {
            NickField updatedField = nicks.get(oldName);
            updatedField.changeNameTo(newName);
            nicks.remove(oldName);
            nicks.put(newName, updatedField);
        }
    }
}
