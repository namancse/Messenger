import javax.swing.JFrame;
public class ServerTest {

	public static void main(String[] args) {
		Server myServer = new Server();
		myServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myServer.startRunning();
		}

}
