import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientInstance {
	Socket client;
	String name;
	BufferedReader ServerInput;
	BufferedReader UserInput;
	DataOutputStream Output;

	ClientInterface GUI;
	boolean ready = false;

	int port;

	public ClientInstance() throws UnknownHostException, IOException{

		GUI = new ClientInterface();
		GUI.client = this;

		port = GUI.port;

		client = new Socket("localhost", port);
		ServerInput = new BufferedReader(new InputStreamReader(client.getInputStream()));
		UserInput = new BufferedReader(new InputStreamReader(System.in));
		Output = new DataOutputStream(client.getOutputStream());

		Join();
	}

	public void Join() throws IOException{
		System.out.println("Welcome to the chat server \nPlease enter a Valid username to begin");
		GUI.textArea.setText(GUI.textArea.getText() +"Welcome to the chat server \nPlease enter a Valid username to begin"+ "\n");

		while(true){
			System.out.println("delaying this thread long enough for the change in the boolean to happen");
			if(ready){
				name = GUI.textField.getText();
				Output.writeBytes(name + "\n");
				String response = ServerInput.readLine();
				ready=false;
				if(response.equals("Welcome")){
					break;
				}else{
					System.out.println("This username is already taken, please try another one");
					GUI.textArea.setText(GUI.textArea.getText() +"This username is already taken, please try another one"+ "\n");
				}
			}
			ready = false;
		}
		System.out.println("syso");
		BeSami();

	}

	public void BeSami() throws IOException{
		while(true){
			System.out.println("delay bardo");
			if(ready){
				String letter = GUI.textField.getText();
				ready=false;
				if(!letter.equals("")){
					if(letter.equals("getmemberslist")){
						GetMembersList(letter);
					} else {
						String letterComponents [] = letter.split("<>");
						try{
							Chat(letterComponents[0], name, letterComponents[1], 2);
						}catch(IndexOutOfBoundsException e){
							System.out.println("Please consult help button for the right syntax");
							GUI.textArea.setText(GUI.textArea.getText() +"Please consult help button for the right syntax"+ "\n");
}
					}
				}

			}
			if(ServerInput.ready()){
				String ServerLetter = ServerInput.readLine();
				System.out.println(ServerLetter);
				GUI.textArea.setText(GUI.textArea.getText() + ServerLetter + "\n");
			}


		}
	}

	public void GetMembersList(String letter) throws IOException{
		Output.writeBytes(letter + "\n");
	}

	public void Chat(String destination, String Sender, String letter, int TTL) throws IOException{
		String CombinedParts = destination+"<>"+name+": " + letter;
		Output.writeBytes(CombinedParts+ "\n");

	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		ClientInstance Felix = new ClientInstance();
	}

}
