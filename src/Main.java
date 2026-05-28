import controller.Controller;
import gui.LoginFrame;
public class Main {
    public static void main(String[] args){
        Controller controller = new Controller();
        LoginFrame login = new LoginFrame(controller);
        login.setVisible(true);
    }
}
