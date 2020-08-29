import javax.jws.soap.SOAPBinding;
import java.io.*;
import java.sql.*;

public class Main {
    private static Connection conn;

    private static Statement stat;

    public static void main(String[] args) {
        try {
            connect();
            workWithDataBase();
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    private static void workWithDataBase() throws SQLException, IOException, ClassNotFoundException {
/*        stat.executeUpdate("DELETE FROM Users");
        User user = new User("someName", "", "", new DependsOfUser());
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(user);
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Users (Nick, User) VALUES ( ?, ? )");
        ps.setString(1, "someNick");
        ps.setBytes(2, byteOut.toByteArray());
        ps.executeUpdate();

        ResultSet rs = stat.executeQuery("SELECT * FROM Users");
        rs.next();
        byte[] bytes = rs.getBytes(2);
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream objIn = new ObjectInputStream(in);
        User user1 = (User) objIn.readObject();
        System.out.println(user1.toString());
        user1.sayHello();*/

        String s1;
        String s2 = "Hello";
        s1 = s2;
        s2 = "Hey!";
        System.out.println(s1);
    }

    private static void disconnect() {
        try {
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void connect() throws ClassNotFoundException, SQLException {
        String URL = "jdbc:sqlite:UsersBase.db";
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection(URL);
        stat = conn.createStatement();
    }

    public static void notifyMain(String name) {
        System.out.println(name + " works");
    }

    public static void makeSQL(String sql) throws SQLException {
        stat.executeUpdate(sql);
    }
}
