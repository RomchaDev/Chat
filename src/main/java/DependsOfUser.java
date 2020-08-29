import java.io.Serializable;

public class DependsOfUser implements Serializable {

    private User user = null;

    public void setUser(User user) {
        this.user = user;
    }

    public DependsOfUser() {
        new Thread(() -> {
            int counter = 0;
            while (true) {
                counter++;
                System.out.println(user.getName() + " : " + counter);
                if (counter >= 5) {
                    Main.notifyMain(user.getName());
                    counter = 0;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
