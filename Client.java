import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
private JTextField userText;
private JTextArea chatWindow;
private ObjectOutputStream output;
private ObjectInputStream input;
private String message="";
private String serverIP;
private Socket connection;
//constructor
public Client(String host)
{
super("Client");
serverIP=host;
userText=new JTextField();
userText.setEditable(false);
userText.addActionListener(
	new ActionListener(){
		public void actionPerformed(ActionEvent e)
		{
			sendMessage(e.getActionCommand());
			userText.setText("");
		}
	}
	
);
add(userText,BorderLayout.NORTH);
chatWindow=new JTextArea();
add(new JScrollPane(chatWindow),BorderLayout.CENTER);
setSize(500,350);
setVisible(true);
}
//running the client
public void startRunning(){
	try{
		connectToServer();
		setupStreams();
		whileChatting();
		
	}catch(EOFException eofException){
		showMessage("\nClient closed the connection");
	}catch(IOException ioException){
		ioException.printStackTrace();
	}finally{
		closeProg();
	}	
}
//connect to server
private void connectToServer() throws IOException{
	showMessage("\nAttempting connection...");
	connection=new Socket(InetAddress.getByName(serverIP),6789);
	showMessage("\nConnected to "+connection.getInetAddress().getHostName());
	
}
//setting up streams
private void setupStreams() throws IOException{
	output=new ObjectOutputStream(connection.getOutputStream());
	output.flush();
	input=new ObjectInputStream(connection.getInputStream());
	showMessage("\nStreams are setup");
}
//while chatting method
private void whileChatting() throws IOException{
	ableToType(true);
do{
	try{

		message=(String)input.readObject();
		showMessage("\n"+message);
	}catch(ClassNotFoundException classNotFoundException){
		showMessage("\nUnable to read object\n");
	}
}while(!message.equals("SERVER - END"));
}
//closing stuff
private void closeProg(){
	showMessage("\nclosing program\n");
	ableToType(false);
	try{
		input.close();
		output.close();
		connection.close();
	}catch(IOException ioException){
		ioException.printStackTrace();
	}
}
//sending message to server
private void sendMessage(String message){
	try{
		output.writeObject("CLIENT - "+message);
		output.flush();
		showMessage("\nCLIENT - "+message);
	}catch(IOException ioException){
		chatWindow.append("\n Problem in sending message\n");
	}	
}
//able to type method
private void ableToType(boolean ch){
	SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					userText.setEditable(ch);
				}
			}
			);	
}
//show message method--update chat window
private void showMessage(final String msg){
	SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append(msg);
				}
			}
			);
}
//-----------
}
