public class ServerStarter {
    public static void main(String[] args) {
        MainSQLWorker.connect();
        RegAuthService.initializeUsers();
    }
}
