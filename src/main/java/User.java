import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 163008928632146737L;
    private final String name;
    private final String login;
    private final String pass;
    private final DependsOfUser depends;

    public DependsOfUser getDepends() {
        return depends;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public User(String name, String login, String pass, DependsOfUser depends) {
        this.depends = depends;
        this.name = name;
        this.login = login;
        this.pass = pass;
        depends.setUser(this);
    }

    public void sayHello() {
        System.out.println("Hello");
    }
}
