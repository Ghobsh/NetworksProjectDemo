import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialArray;

public class ServerInstance {

	ServerSocket server;
	ArrayList<String> Names = new ArrayList<String>();
	ArrayList<DataOutputStream> outputs = new ArrayList<DataOutputStream>();
	Socket client;

	ArrayList<BufferedReader> SerIns = new ArrayList<>();
	ArrayList<DataOutputStream> ServerOutputs = new ArrayList<>();
	int address = 6003;

	public ServerInstance() throws IOException{
		server = new ServerSocket(address);

		try {
			JoinNetwork();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("server online");

		while(true){
			client = server.accept();
			new Thread(){
				public void run(){
					try {
						System.out.println("one trying to get in");
						JoinResponse(client);
						System.out.println("He got it");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}

	}

	boolean check(String name) throws IOException{
		if(Names.contains(name)) return false;

		for (int i = 0; i < ServerOutputs.size(); i++) {

			ServerOutputs.get(i).writeBytes("LoginChk<>"+name+"\n");
			String answer = SerIns.get(i).readLine();

			if(answer.equals("RSPN")) return false;
		}

		return true;
	}


	protected void JoinResponse(Socket client) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		DataOutputStream out = new DataOutputStream(client.getOutputStream());


		String name = "";

		while(true){
			//			out.writeBytes("Please Enter a username: " + "\n");
			name = in.readLine();
			System.out.println("Read line");
			if(name.equals("<<<>>>")){
				System.out.println("peer");
				BufferedReader SerIn = new BufferedReader(new InputStreamReader(client.getInputStream()));;
				DataOutputStream ServerOutput = new DataOutputStream(client.getOutputStream());;
				//				ServerOutput.writeBytes("Y"+"\n");//XXX recently commented

				SerIns.add(SerIn);
				ServerOutputs.add(ServerOutput);

				NetworkTheNetworks(SerIn, ServerOutput);
				break;
			} else {
				if(Names.contains(name)){
					System.out.println("local duplicate");
					out.writeBytes("Please Enter another one, because this one is taken."+ "\n");
				} else {
					if(check(name)){
						System.out.println("unique, should pass");
						out.writeBytes("Welcome"+ "\n");
						out.writeBytes("Welcome to the chat App"+"\n");
						Names.add(name);
						outputs.add(out);
						Serve(name, in, out);
						break;
					} 
				}
			}
		}
	}


	private void Serve(String name, BufferedReader in, DataOutputStream out) throws IOException{
		while(true){
			String message = in.readLine();
			String[] parts = message.split("<>");
			if(message.equals("getmemberslist")){
				for (int i = 0; i < Names.size(); i++) {
					out.writeBytes(Names.get(i)+ "\n");
				}
				if(!ServerOutputs.isEmpty()){
					for (int j = 0; j < ServerOutputs.size(); j++) {
						ServerOutputs.get(j).writeBytes("Request<>"+name+ "\n");
					}
				}
			} else {
				if(Names.contains(parts[0])){
					int x = Names.indexOf(parts[0]);
					out.writeBytes(parts[1] + "\n");
					outputs.get(x).writeBytes(parts[1] + "\n");
				} else {
					if(!ServerOutputs.isEmpty()){
						for (int j = 0; j < ServerOutputs.size(); j++) {
							ServerOutputs.get(j).writeBytes("Forward<>"+message+ "\n");
						}
					}
//					if(ServerOutput!=null)ServerOutput.writeBytes("Forward<>"+message+ "\n");
					//					out.writeBytes("Destination is not online, please select a valid one"+ "\n"); TODO to be removed after we check we covered it
				}

				if(parts[1].equals("Bye") || parts[1].equals("Quit")){
					Names.remove(parts[0]);
					outputs.remove(out);
				}
			}


		}
	}

	
	public void JoinNetwork() throws UnknownHostException, IOException{

		
		if(address!=6000)
		new Thread(){
			public void run(){
				Socket DataLink;
				try {
					DataLink = new Socket("localhost",6000);
					BufferedReader SerIn = new BufferedReader(new InputStreamReader(DataLink.getInputStream()));
					DataOutputStream ServerOutput = new DataOutputStream(DataLink.getOutputStream());

					ServerOutput.writeBytes("<<<>>>" +"\n");

					SerIns.add(SerIn);
					ServerOutputs.add(ServerOutput);

					//					while(true){
					//						if(SerIn.ready()){
					//					String s = SerIn.readLine();
					//					System.out.println(s);
					//					if(s.equals("Y"))break;
					//						}
					//					}
					NetworkTheNetworks(SerIn, ServerOutput);
				} catch (IOException e) {
					System.out.println("debug: server 6000 is offline");
				}
			}
		}.start();
		
		if(address!=6001)
		new Thread(){
			public void run(){
				Socket DataLink;
				try {
					DataLink = new Socket("localhost",6001);
					BufferedReader SerIn = new BufferedReader(new InputStreamReader(DataLink.getInputStream()));
					DataOutputStream ServerOutput = new DataOutputStream(DataLink.getOutputStream());

					ServerOutput.writeBytes("<<<>>>" +"\n");

					SerIns.add(SerIn);
					ServerOutputs.add(ServerOutput);

					//					while(true){
					//						if(SerIn.ready()){
					//					String s = SerIn.readLine();
					//					System.out.println(s);
					//					if(s.equals("Y"))break;
					//						}
					//					}
					NetworkTheNetworks(SerIn, ServerOutput);
				} catch (IOException e) {
					System.out.println("debug: server 6001 is offline");
				}
			}
		}.start();

		if(address!=6002)
		new Thread(){
			public void run(){
				Socket DataLink;
				try {
					DataLink = new Socket("localhost",6002);
					BufferedReader SerIn = new BufferedReader(new InputStreamReader(DataLink.getInputStream()));
					DataOutputStream ServerOutput = new DataOutputStream(DataLink.getOutputStream());

					ServerOutput.writeBytes("<<<>>>" +"\n");

					SerIns.add(SerIn);
					ServerOutputs.add(ServerOutput);

					//					while(true){
					//						if(SerIn.ready()){
					//					String s = SerIn.readLine();
					//					System.out.println(s);
					//					if(s.equals("Y"))break;
					//						}
					//					}
					NetworkTheNetworks(SerIn, ServerOutput);
				} catch (IOException e) {
					System.out.println("debug: server 6002 is offline");
				}
			}
		}.start();

		if(address!=6003)
		new Thread(){
			public void run(){
				Socket DataLink;
				try {
					DataLink = new Socket("localhost",6003);
					BufferedReader SerIn = new BufferedReader(new InputStreamReader(DataLink.getInputStream()));
					DataOutputStream ServerOutput = new DataOutputStream(DataLink.getOutputStream());

					ServerOutput.writeBytes("<<<>>>" +"\n");

					SerIns.add(SerIn);
					ServerOutputs.add(ServerOutput);

					//					while(true){
					//						if(SerIn.ready()){
					//					String s = SerIn.readLine();
					//					System.out.println(s);
					//					if(s.equals("Y"))break;
					//						}
					//					}
					NetworkTheNetworks(SerIn, ServerOutput);
				} catch (IOException e) {
					System.out.println("debug: server 6003 is offline");
				}
			}
		}.start();


	}



	void replytorequest(String message, DataOutputStream ServerOutput) throws IOException{
		for (int i = 0; i < Names.size(); i++) {
			ServerOutput.writeBytes(message + "<>" + Names.get(i)+ "\n");
		}
	}

	public void NetworkTheNetworks(BufferedReader SerIn, DataOutputStream ServerOutput) throws IOException{
		while(true){
			String Transmission = SerIn.readLine();
			System.out.println(Transmission); 
			String message [] = Transmission.split("<>");

			//Case forwarding messages
			if(Transmission.startsWith("Forward")){
				if(Names.contains(message[1])){
					int x = Names.indexOf(message[1]);
					outputs.get(x).writeBytes(message[2] + "\n");
				} else {
					String [] messageComponents = message[2].split(":");
					ServerOutput.writeBytes("Error<>" + messageComponents[0]+ "\n");
				}
			}


			//Case error returns
			if(message[0].equals("Error")){
				int x = Names.indexOf(message[1]);
				outputs.get(x).writeBytes("ERROR-404: Destination offline" +"\n");
			}


			//Case member list request
			if(message[0].equals("Request")){
				for (int i = 0; i < Names.size(); i++) {
					ServerOutput.writeBytes("Response<>" + message[1]+"<>"+ Names.get(i)+ "\n");
				}
			}


			//Case member list response returned
			if(message[0].equals("Response")){
				int x = Names.indexOf(message[1]);
				outputs.get(x).writeBytes(message[2] + "\n");
			}

			//Username uniqueness check
			if(message[0].equals("LoginChk")){
				System.out.println(Transmission);
				if(Names.contains(message[1])){
					ServerOutput.writeBytes("RSPN"+ "\n");
					System.out.println("sent no");
				} else {
					ServerOutput.writeBytes("Y" + "\n");
					System.out.println("sent yes");
					ServerOutput.writeBytes("Y" + "\n");
				}
			}
		}
	}







	public static void main(String[] args) throws IOException {
		ServerInstance ser = new ServerInstance();
	}
}
