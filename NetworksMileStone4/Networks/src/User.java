import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;


public class User extends Thread {
	
	Socket socket;
	String name;
	BufferedReader sinput;
	DataOutputStream outputClient;
	BufferedReader ninput;
	
	public User(Socket connectionSocket) throws IOException {
	
		socket = connectionSocket;
		sinput = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		ninput = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		outputClient = new DataOutputStream(this.socket.getOutputStream());
		
	}
	
	
	public boolean addclient(String name){
		ArrayList<User> temp = Server.users;
		ArrayList<User2> temp2 = Server2.users;
		for (int i = 0; i < temp.size(); i++) {
			if(name.equals(temp.get(i).name)){
				return false;  
			}
			
		}
		Server.users.add(this);
		return true;
	}
	

	
	public String showclients() { 
		ArrayList<User> temp = Server.users;
		
		
		String s="";
		for (int i = 0; i < temp.size()-1; i++) {
			
				s+=temp.get(i).name+ '\n';
		}
		return s;
		
		
		}
	
	
	public void run(){
        
		String s = "Please enter your Username:";	
		while(true){
			try {
				outputClient.writeBytes(s + '\n');
				outputClient.flush();
				String n = sinput.readLine();
				if(addclient(n)) {
					this.name = n;
					outputClient.writeBytes("Successfully connected as "+n+'\n');
					outputClient.flush();

					break;
				}
				outputClient.writeBytes("This username already exists." + '\n');
				outputClient.flush();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		while(true){
			try {
				
				String msgOfclient = sinput.readLine();
			 if( msgOfclient!= null)	{
			if(msgOfclient.contains("/")){
					//check that name exists in Users
					String a="please enter your text";
					outputClient.writeBytes(a + '\n');
					outputClient.flush();
					String t = ninput.readLine();
					String[] msgofclientSplit = msgOfclient.split("/"); //get rid of spaces
					String source = msgofclientSplit[1];
					String destination = msgofclientSplit[2];
					String TTL = msgofclientSplit[3];
					
					String[] t1 = t.split("/");
					String tfinal = t1[4];
					
					if(tfinal.equals("getmemberslist")){
						
						outputClient.writeBytes(showclients());
							outputClient.flush();
							break;
						}
					message(source, destination, TTL, tfinal);
					
					if(tfinal.equals("QUIT")||tfinal.equals("BYE")){
						Server.users.remove(this);
						this.socket.close();
						break;
					}
				}
			} 
			}
			 catch (IOException e) {
				e.printStackTrace();
			}
		}
}

	
	public void message(String source,String destination,String TTL,String msg) throws IOException{
		if(destinationCheck(destination)){
			DataOutputStream os = new DataOutputStream(destinationSocket(destination).getOutputStream());
			os.writeBytes("From"+ source +": "+ msg + '\n' );
			
			os.flush();
		}
		else{
			outputClient.writeBytes("This user doesn't exist"+'\n');
			outputClient.flush();
		}
	}
	
	

	public boolean destinationCheck(String destination){
		
		ArrayList<User>temp=Server.users;
		boolean flag=false;
		for (int i = 0; i < temp.size(); i++) {
			if(temp.get(i).name.equals(destination)){
				flag=true;	
			}	
		}
		
		return flag;
	}
	
	
	public  Socket destinationSocket(String destination){//to create socket for the client
		ArrayList<User>temp=Server.users;
		Socket s = new Socket();
		for (int i = 0; i < temp.size(); i++) {
			if(temp.get(i).name.equals(destination)){
				s = temp.get(i).socket;
			}

		}
		return s;
	}

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	

}
