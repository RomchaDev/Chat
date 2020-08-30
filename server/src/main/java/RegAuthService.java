import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class RegAuthService {
    private static final RegAuthService REG_AUTH_SERVICE = new RegAuthService();

    public static RegAuthService getInstance() {
        return REG_AUTH_SERVICE;
    }

    private RegAuthService() {
    }

    public String authorise(Account account, Socket connection, ObjectOutputStream output) {
        List<Account> accountsSQL = MainSQLWorker.getUsers();

        for (Account acc : accountsSQL)
            if (acc.equals(account) && !MainSQLWorker.isAuthorised(acc.getLogin())) {
                AccountNetwork net = new AccountNetwork(acc.getId(), connection);
                net.setOutput(output);
                Server.getInstance().addNetwork(MainSQLWorker.getIndexOf(acc.getLogin()), net);
                MainSQLWorker.setAuthorised(acc.getLogin(), true);
                return acc.getName();
            }
        return null;
    }

    public static void initializeUsers() {
        Server.startListening();

        MainSQLWorker.clearTable();

        MainSQLWorker.addUser("Nick1", "login1", "pass1");
        MainSQLWorker.addUser("Nick2", "login2", "pass2");
        MainSQLWorker.addUser("Nick3", "login3", "pass3");
    }

    public boolean register(Account account) {
        boolean isUnique = true;
        for (Account acc : MainSQLWorker.getUsers())
            isUnique &= !account.equals(acc) && !acc.getName().equals(account.getName());

        if (isUnique)
            MainSQLWorker.addUser(account.getName(), account.getLogin(), account.getPass());

        return isUnique;
    }
}
