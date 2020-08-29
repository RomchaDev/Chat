import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static final Server server = new Server();
    private final int PORT = 8080;
    private final RegAuthService regAuthService = RegAuthService.getInstance();
    private final Thread newClientListener = new Thread(this::listenToNewClients);
    private final Map<Integer, AccountNetwork> networks = new HashMap<>();

    public static void startListening() {
        server.newClientListener.start();
    }


    public static synchronized Server getInstance() {
        return server;
    }

    private Server() {

    }

    private void listenToNewClients() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("New connection");
                    Thread newClientThread = new Thread(() -> {
                        try {
                            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                            output.flush();
                            while (true) {
                                try {
                                    Message message = (Message) input.readObject();
                                    if (message.getType() == MessageConstants.AUTH) {
                                        String name = regAuthService.authorise(new Account(message.getLogin(), message.getPass()), socket, output);
                                        if (name != null) {
                                            System.out.println(name + " Connected");
                                            output.writeObject(new Message(MessageConstants.ACCEPTED, name));
                                            message.setFrom(name);
                                            sendMessageToAllUsers(new Message(MessageConstants.AUTH, name, null, null));
                                            sendUsersInfo(output);
                                        } else {
                                            System.out.println(message.getFrom() + " wasn't accepted");
                                            output.writeObject(new Message(MessageConstants.NOT_ACCEPTED));
                                        }
                                        break;

                                    } else if (message.getType() == MessageConstants.REGISTRATION) {
                                        System.out.println(message.getFrom() + " " + message.getLogin() + " " + message.getPass());
                                        boolean accepted = regAuthService.register(new Account(message.getFrom(), message.getLogin(), message.getPass()));
                                        if (accepted)
                                            output.writeObject(new Message(MessageConstants.ACCEPTED));
                                        else
                                            output.writeObject(new Message(MessageConstants.NOT_ACCEPTED));
                                        break;
                                    }
                                } catch (IOException | ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                    newClientThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendUsersInfo(ObjectOutputStream output) throws IOException {
        List<Account> SQLSubscribers = MainSQLWorker.getUsers();

        for (Account acc : SQLSubscribers) {
            if (MainSQLWorker.isAuthorised(acc.getLogin()))
                output.writeObject(new Message(MessageConstants.AUTH, acc.getName(), null, null));
        }
    }

    public void notifyAboutClientMessage(Message message) throws IOException {
        List<Account> SQLSubscribers = MainSQLWorker.getUsers();
        if (message.getType() == MessageConstants.SIMPLE) {
            if (message.getTo().equals("ALL"))
                sendMessageToAllUsers(message);
            else {

                for (Account acc : SQLSubscribers) {
                    if (message.getTo().equals(acc.getName()))
                        networks.get(acc.getId()).sendMessageToThisClient(message);
                }
            }
        } else if (message.getType() == MessageConstants.NICK_CHANGING) {
            MainSQLWorker.changeNick(message.getOldName(), message.getNewName());
            System.out.println(message.getOldName() + " " + message.getNewName());
            Message serverMessage = new Message(MessageConstants.NICK_CHANGING, message.getOldName(), message.getNewName());
            serverMessage.setFrom("server");
            server.sendMessageToAllUsers(serverMessage);
        }
    }

    private void sendMessageToAllUsers(Message message) throws IOException {
        List<Account> SQLSubscribers = MainSQLWorker.getUsers();

        for (Account account : SQLSubscribers) {
            if (MainSQLWorker.isAuthorised(account.getLogin())) {
                int id = account.getId();
                networks.get(id).sendMessageToThisClient(message);
            }
        }
    }

    void deleteSubscriber(int id) {
        try {
            sendMessageToAllUsers(new Message(MessageConstants.EXIT, MainSQLWorker.getNameOf(id + 1), null, null));
            networks.remove(id + 1);
            MainSQLWorker.setAuthorised(id + 1, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNetwork(int id, AccountNetwork net) {
        networks.put(id, net);
        System.out.println(id);
    }
}
