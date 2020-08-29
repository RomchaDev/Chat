import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainSQLWorker {
    private static Connection conn;
    private static Statement stat;

    public static void connect() {
        try {
            String URL = "jdbc:sqlite:UsersBase.db";
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(URL);
            stat = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void disconnect() {
        try {
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(String name, String login, String pass) {
        try {

            PreparedStatement ps = conn.prepareStatement("INSERT INTO Users (Nick, Login, Pass)" +
                    "VALUES ( ?, ?, ? )");

            ps.setString(1, name);
            ps.setString(2, login);
            ps.setString(3, pass);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void deleteUser(String nick) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("DELETE FROM Users WHERE Nick = ?");
            ps.setString(1, nick);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static void changeNick(String oldNick, String newNick) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE Users SET Nick = ? WHERE Nick = ?");
            ps.setString(1, newNick);
            ps.setString(2, oldNick);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<Account> getUsers() {
        List<Account> accounts = new ArrayList<>();
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM Users");

            while (rs.next()) {
                    accounts.add(new Account(rs.getInt("id"), rs.getString("Nick"),
                            rs.getString("Login"),
                            rs.getString("Pass")));
            }

            rs.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return accounts;
    }

    private static AccountNetwork convertBytesToNetwork(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream objIn = new ObjectInputStream(in);
        return (AccountNetwork) objIn.readObject();
    }


    public static void addNetwork(String name, AccountNetwork net) {
        try {
            byte[] bytes = convertNetworkToBytes(net);
            PreparedStatement ps = conn.prepareStatement("UPDATE Users SET Network = ? WHERE Nick = ?");
            ps.setBytes(1, bytes);
            ps.setString(2, name);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    private static byte[] convertNetworkToBytes(AccountNetwork net) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(net);
        return byteOut.toByteArray();
    }

    public static void clearTable() {
        try {
            stat.executeUpdate("DELETE FROM Users");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String getNameOf(int id) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT * FROM Users WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String name = rs.getString("Nick");
            rs.close();
            return name;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void setAuthorised(String login, boolean isAuthorised) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE Users SET isAuthorised = ? WHERE Login = ?");
            ps.setBoolean(1, isAuthorised);
            ps.setString(2, login);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void setAuthorised(int id, boolean isAuthorised) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE Users SET isAuthorised = ? WHERE id = ?");
            ps.setBoolean(1, isAuthorised);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean isAuthorised(String login) {
        login = "'" + login + "'";
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM Users WHERE Login = " + login);
            rs.next();
            boolean isAuthorised = rs.getBoolean("isAuthorised");
            rs.close();
            return isAuthorised;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    public static int getIndexOf(String login) {
        login = "'" + login + "'";
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM Users WHERE Login = " + login);
            rs.next();
            int id = rs.getInt("id");
            rs.close();
            return id;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return 0;
    }
}
