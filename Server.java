import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
 private JTextField userText;
 private JTextArea chatWindow;
 private ObjectOutputStream output;
 private ObjectInputStream input;
 private ServerSocket server;
 private Socket connection;
 public Server(){
	 super("TUX Messenger");
	 userText = new JTextField();
	 userText.setEditable(false);
	 //chatWindow.setEditable(false);
	 userText.addActionListener(
		 new ActionListener()
		 	{
			 public void actionPerformed(ActionEvent event)
			 {
				 sendMessage(event.getActionCommand());
				 userText.setText("");
			 }
			 
		 	
	 	}
	);
	 add(userText,BorderLayout.NORTH);
	 chatWindow =new JTextArea();
	 add(new JScrollPane(chatWindow));
	 setSize(500,350);
	 setVisible(true);
 } //set up and run the server
 public void startRunning(){
	 try{
		 server = new ServerSocket(6789,100);//port,no_of_users
		while(true)
		{
			try{
				//connect and start conversation
				waitForConnection();
				setupStreams();
				whileChatting();
			}catch(EOFException eofException)
			{
				showMessage("\nUser ended the connection\n");
			}finally{
				closeProg();
			}
		}
	 }catch(IOException ioException)
	 	{
		 ioException.printStackTrace();
	 	}
	 
	 
	 
 }
 //wait for connection and print message
 private void waitForConnection() throws IOException {
	 showMessage("\nWaiting for someone to connect...\n");
	 connection=server.accept();
	 showMessage("\nConnected to "+connection.getInetAddress().getHostName());
 }
//get streams for i/o
private void  setupStreams() throws IOException{
	output=new ObjectOutputStream(connection.getOutputStream());
	output.flush();
	input=new ObjectInputStream(connection.getInputStream());
	showMessage("\n streams are set up");
}
//during chatting
private void whileChatting() throws IOException{
			String message= "You are now connected";
			sendMessage(message);
			ableToType(true);
			do{
				try{
					message=(String)input.readObject();//read string from input stream
					showMessage("\n"+message);
				}catch(ClassNotFoundException classNotFoundException){
					showMessage("\n Unknown type of message send by user");
				}
			}while(!message.equals("CLIENT - END"));
}
//close streams and sockets after chatting
private void closeProg(){
	showMessage("\n Closing the connection");
	ableToType(false);
	try{
		input.close();
		output.close();
		connection.close();
	}catch(IOException ioException){
		 	ioException.printStackTrace();
	}
}
//send message method
private void sendMessage(String message)
	{
	try{
	output.writeObject("SERVER - "+message);//write to output stream after concatenating  with server word
	output.flush();//always flush the output bites 
	showMessage("\nSERVER - "+message);//showing it in my own window
	}catch(IOException ioException){
	chatWindow.append("\nError,I can't send this message\n");
		}	
	}

//update chat window
private void showMessage(final String text){
	//update whole GUI by changing chat window by a thread
	SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append(text);
				}
			}
			);
	
	}

//letting the user type the message
private  void  ableToType(final boolean tof){
	SwingUtilities.invokeLater(//thread
			new Runnable(){
				public void run(){
					userText.setEditable(tof);;
				}
			}
			);
	
}
}
