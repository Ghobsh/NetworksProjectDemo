import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client { //TODO STILL, MAKE SURE ERROR STATEMENTS ARE PRINTED WHEN THERE IS ONLY ONE SERVER

	Socket client;
	BufferedReader inFromUser;
	DataOutputStream outToServer;
	BufferedReader inFromServer;
	String name = "";

	ClientFrame frame;
	boolean inFromGUI = false;
	String sendingmessage;

	boolean accepted = false;

	int serverPort=6000;//default value is 6000, just in case
	
	public Client() throws IOException, InterruptedException{
		frame = new ClientFrame();
		frame.client = this;


		frame.ChatLog.setText(frame.ChatLog.getText()+ "Welcome, Client" +'\n');
		//		System.out.println("Welcome, Client");

		//Input from user
		inFromUser = new BufferedReader(new InputStreamReader(System.in));

		//		System.out.println("Please Enter your name, so we can contact the server.");
		frame.ChatLog.setText(frame.ChatLog.getText()+ "Please Enter your name, so we can contact the server." +'\n');


		//Socket, connect to any server socket at port 6000 (el server beat3na ya3ny).
		//XXX Equivalent to "localhost" for doing both ends on the same PC
		//XXX Or can be changed to the server computer's network name.
		try{client = new Socket("0.0.0.0", 6000);}catch(Exception e){frame.ChatLog.setText(frame.ChatLog.getText()+ "Server down at the moment, please try again later" +'\n');}

		//Input and output connections between client and server
		inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
		outToServer = new DataOutputStream(client.getOutputStream());


		//		while(!accepted){
		//			if(inFromGUI){
		//				sendingmessage = frame.input.getText();
		//				name = sendingmessage.trim();
		//				inFromGUI = false;
		//				System.out.println("choosen name is " + name);//XXX debug can keep
		//				Join(name);
		//				if(!accepted){System.out.println("Name already taken, please enter another name to attempt logging in.");frame.ChatLog.setText(frame.ChatLog.getText()+ "Name already taken, please enter another name to attempt logging in." +'\n');}
		//			}
		//		}
		accepted=false;
		while(true){
			System.out.println("being productive");
			if(!accepted)
				continue;
			else 
				Order();}
	}


	public void help() throws UnknownHostException, IOException{
		if(!accepted){
			//			while(!accepted){
			if(inFromGUI){
				sendingmessage = frame.input.getText();
				name = sendingmessage.trim();
				inFromGUI = false;
				System.out.println("choosen name is " + name);//XXX debug can keep
				Join(name);
				if(!accepted){System.out.println("Name already taken, please enter another name to attempt logging in.");frame.ChatLog.setText(frame.ChatLog.getText()+ "Name already taken, please enter another name to attempt logging in." +'\n');}else{System.out.println("Connection accepted, please connect to a chat session or type \"h::\" for further help \n to quit at anytime, please type in \"quit\" or \"bye\".");
				frame.ChatLog.setText(frame.ChatLog.getText()+ "Connection accepted, please connect to a chat session or type \"h::\" for further help \nto quit at anytime, please type in \"quit\" or \"bye\"." +'\n');
				}
			}
			//			}
		} else {

			if(inFromGUI){
				String message = sendingmessage;
				inFromGUI = false;
				System.out.println(name + ": " +  message);
				frame.ChatLog.setText(frame.ChatLog.getText()+ name + ": " +  message +'\n');

				if(message.length()>3 && message.substring(0,3).equals("w::")){
					destination = message.substring(3).trim();
					System.out.println("changed destination to " + destination );
					frame.ChatLog.setText(frame.ChatLog.getText()+ "changed destination to " + destination +'\n');
				} else{
					if(message.equals("l::")){
						GetMemberList();
					}else {
						if(message.equals("h::")){
							System.out.println("To change destination, type in w:: then a space then followed by the recipient's name: note that you have to keep only one white space between the code and the name"
									+ "\n To see the list of online users, type in l::"
									+ "\n To quit at any given time, type in \"quit\"");
							frame.ChatLog.setText(frame.ChatLog.getText()+ "To change destination, type in w:: then a space then followed by the recipient's name: note that you have to keep only one white space between the code and the name"
									+ "\n To see the list of online users, type in l::"
									+ "\n To quit at any given time, type in \"quit\"" +'\n');
						}else {
							Chat(name, destination,1 , message);

							//Law el user wrote bye or quit it quits, kollo null.
							if(message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("QUIT")){
								Quit();
							}
						}
					}
				}
			}
		}
	}

	public void Join(String name) throws UnknownHostException, IOException{


		String response = "";
		outToServer.writeBytes(name + '\n');

		while(true){
			if(inFromServer.ready()){
				response = inFromServer.readLine();
				break;
			}
		}
		accepted=response.equals("y");
		System.out.println(accepted);
	}
	

	String destination = "";

	public void Order() throws IOException{
		System.out.println("Reached ordering status");
		while(true){
			if(inFromServer.ready()){
				String message = inFromServer.readLine();
				System.out.println(message);
				frame.ChatLog.setText(frame.ChatLog.getText()+ message +'\n');
				
				frame.ChatLog.repaint();
				frame.ChatLog.validate();

				//Law el server wrote bye or quit it quits, kollo null.
				if(message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("QUIT")){
					Quit();
				}
			}

			//Law el user wants to send, send their message. and print it here as well 3lshan ne3raf el chat history kollo.
			//			if(inFromGUI){
			//				String message = sendingmessage;
			//				inFromGUI = false;
			//				System.out.println(name + ": " +  message);
			//				frame.ChatLog.setText(frame.ChatLog.getText()+ name + ": " +  message +'\n');
			//
			//				if(message.length()>2 && message.substring(0,3).equals("w::")){
			//					destination = message.substring(3).trim();
			//					System.out.println("changed destination to " + destination );
			//					frame.ChatLog.setText(frame.ChatLog.getText()+ "changed destination to " + destination +'\n');
			//				} else{
			//					if(message.equals("l::")){
			//						GetMemberList();
			//					}else {
			//						if(message.equals("h::")){
			//							System.out.println("To change destination, type in w:: then a space then followed by the recipient's name: note that you have to keep only one white space between the code and the name"
			//									+ "\n To see the list of online users, type in l::"
			//									+ "\n To quit at any given time, type in \"quit\"");
			//							frame.ChatLog.setText(frame.ChatLog.getText()+ "To change destination, type in w:: then a space then followed by the recipient's name: note that you have to keep only one white space between the code and the name"
			//									+ "\n To see the list of online users, type in l::"
			//									+ "\n To quit at any given time, type in \"quit\"" +'\n');
			//						}else {
			//							Chat(name, destination,1 , message);
			//
			//							//Law el user wrote bye or quit it quits, kollo null.
			//							if(message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("QUIT")){
			//								Quit();
			//							}
			//						}
			//					}
			//				}
			//			}
		}
	}

	private void Chat(String name, String destination, int ttl, String message) throws IOException{
		//XXX sends message with custom header to server.

		String msg ="mesg" + destination + "tknzhr" + name + ": " + message;
		outToServer.writeBytes(msg+ '\n');
	}

	public void GetMemberList() throws IOException{
		String msg ="rqst" + name;
		outToServer.writeBytes(msg+ '\n');
	}

	public void Quit() throws IOException{
		outToServer.writeBytes("BYE" + name+ '\n');
		inFromUser = null;
		outToServer = null;
		inFromServer = null;
		System.exit(-1);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		new Client();
	}
}










//XXX rqst stands for server request, which the server doesn't forward but process 3ala tool :D
//XXX while mesg is a standard header for a message, makes the server use my protocol layer (is it really mine?) and forward the message to the destination with the appropriate format.

//		(new Thread(){
//			@Override
//			public void run(){
//				try{
//					//infinite loop 3lshan yeseeb el chat session open 3ala tool.
//					while(true){
//
//						//Law fi incoming transmission, process it and print it.
//						if(inFromServer.ready()){
//							String message = inFromServer.readLine();
//							System.out.println("SERVER: " + message);
//
//							//Law el server wrote bye or quit it quits, kollo null.
//							if(message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("QUIT")){
//								inFromUser = null;
//								outToServer = null;
//								inFromServer = null;
//								System.exit(-1);
//							}
//						}
//
//						//Law el user wants to send, send their message. and print it here as well 3lshan ne3raf el chat history kollo.
//						if(inFromUser.ready()){
//							String message = sendingmessage;
//							System.out.println("CLIENT: " + message);
//							outToServer.writeBytes(message+ '\n');
//
//							//Law el server wrote bye or quit it quits, kollo null.
//							if(message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("QUIT")){
//								inFromUser = null;
//								outToServer = null;
//								inFromServer = null;
//								System.exit(-1);
//							}
//						}
//
//					}
//				} catch (Exception e){e.printStackTrace();}
//			}
//		}).start();





/*	(new Thread(){
			@Override
			public void run(){
				try{
					while(true){

						String message = sendingmessage;
						System.out.println("CLIENTFROMUSER: " + message);
						outToServer.writeBytes(message);
					}
				} catch (Exception e){e.printStackTrace();}
			}
		}).start();
 */