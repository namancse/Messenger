import javax.swing.JFrame;

public class ClientTest {

	public static void main(String[] args) {
      Client naman;
      naman = new Client("127.0.0.1");//IP --here of own computer i.e. local host
      naman.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      naman.startRunning();
	}
}