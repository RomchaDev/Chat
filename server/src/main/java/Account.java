import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Account implements Serializable {
    private final String name;
    private final String login;
    private final String pass;
    private final int id;

    public Account(int id, String name, String login, String pass) {
        this.name = name;
        this.login = login;
        this.pass = pass;
        this.id = id;
    }

    public Account(String name, String login, String pass) {
        this(0, name, login, pass);
    }

    public Account(String login, String pass) {
        this(0, null, login, pass);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        Account account = (Account) obj;
        return account.login.equals(this.login) && account.pass.equals(this.pass);
    }


    public String getLogin() {
        return this.login;
    }

    public int getId() {
        return id;
    }

    public String getPass() {
        return pass;
    }
}