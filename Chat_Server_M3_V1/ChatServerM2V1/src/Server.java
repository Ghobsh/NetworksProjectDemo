import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server {
	ServerSocket server;

	Socket requestee;

	ArrayList<String> Names = new ArrayList<>();
	ArrayList<Socket> Sockets = new ArrayList<>();

	ArrayList<DataOutputStream> outToClients = new ArrayList<>();
	ArrayList<BufferedReader> inFromClients = new ArrayList<>();

	//	ArrayList<Socket> otherServers = new ArrayList<>();
	//	ArrayList<String> otherServerNames = new ArrayList<>();
	//	ArrayList<DataOutputStream> outToOtherServers = new ArrayList<>();

	DataOutputStream outToPeer=null;
	BufferedReader inFromPeer=null;

	public Server() throws IOException{

		System.out.println("Establishing Server....");
		System.out.println("Server Online, Awaiting Orders....");

		//Establish Server socket in this port to reserve it for connections.
		InetAddress addr = InetAddress.getByName("0.0.0.0");
		server = new ServerSocket(6000, 70000, addr);

		try{connectToPeer();} catch(Exception e){System.out.println("Primary");}

		//TODO thread loops, not all processing to accept connections
		while(true){
			requestee = server.accept();
			new Thread(){
				public void run(){
					try {JoinResponse(requestee);}catch(IOException e){e.printStackTrace();}
				}
			}.start();
		}
	}

	void connectToPeer() throws UnknownHostException, IOException{
		Socket client = new Socket("0.0.0.0", 6001);
		outToPeer = new DataOutputStream(client.getOutputStream());
		inFromPeer = new BufferedReader(new InputStreamReader(client.getInputStream()));

		outToPeer.writeBytes("prsvr" + '\n'); //TODO for milestone 4 here check if entered first, just like a normal client

//		Names.add("prsvr");
//		outToClients.add(outToPeer);
//		inFromClients.add(inFromPeer);
	}


	public void JoinResponse(Socket client) throws IOException{
		DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));

		String name = "";
		System.out.println("new client pending, waiting for requests");

		while(true){
			if(inFromClient.ready()){
				name = inFromClient.readLine();
				System.out.println(name + " is now attempting connection"); //XXX debug can keep

				if(name.equals("prsvr")){
					outToPeer=new DataOutputStream(client.getOutputStream());
					inFromPeer=new BufferedReader(new InputStreamReader(client.getInputStream()));

					//					Names.add("prsvr");
					//					outToClients.add(outToPeer);
					//					inFromClients.add(inFromPeer);

					System.out.println("Paired with other server");
					break;
				} else {
					//					if(otherServerNames.contains(name)){outToClient.writeBytes("n"+ '\n');System.out.println("Rejected, waiting for another input"+ '\n');} //XXX debug may keep
					//					else {
					//						System.out.println("Accepted, server connection established"+ '\n'); //XXX debug may keep
					//						outToClient.writeBytes("y"+ '\n');
					//						otherServerNames.add(name);
					//						outToOtherServers.add(outToClient);
					//						break;
					//					}


					if(Names.contains(name)){outToClient.writeBytes("n"+ '\n');System.out.println("Rejected, waiting for another input"+ '\n');} //XXX debug may keep
					else {
						System.out.println("Accepted, "+name+" is in"+ '\n'); //XXX debug may keep
						outToClient.writeBytes("y"+ '\n');
						Names.add(name);
						inFromClients.add(inFromClient);
						outToClients.add(outToClient);
						Sockets.add(client);
						break;
					}
				}
			}
		}


		while(true){
			//Law fi incoming transmission, process it and print it.
			if(inFromClient.ready()){
				String message = inFromClient.readLine();
				System.out.println(message);//XXX debug, can keep

				//XXX handle incoming transmission
				if(message.startsWith("mesg")){
					String[] trans = message.substring(4).split("tknzhr");
					if(Names.contains(trans[0])){
						int destno = Names.indexOf(trans[0]);
						outToClients.get(destno).writeBytes(trans[1]+ '\n');
					} else {
						System.out.println("no local reciever");
						outToClient.writeBytes("SERVER: Destination not found; specify an online target"+ '\n');
						if(outToPeer!=null)outToPeer.writeBytes("fwdmsg" +  message.substring(4) + '\n');
					}

				} else { //XXX handle name requests

					if(message.startsWith("BYE")){
						int x = Names.indexOf(message.substring(3));
						Names.remove(x);
						Sockets.remove(x);
						inFromClients.remove(x);
						outToClients.remove(x);
					}else {
						if(message.startsWith("rqst")){
							String rqstee = message.substring(4);
							MemberListResponse(rqstee);
						} 
					}
				}

				//Law el user wrote bye or quit it quits, kollo null.
//				if(message.substring(0, 3).equalsIgnoreCase("BYE") || message.substring(0, 3).equalsIgnoreCase("QUIT")){
//					//TODO exit and delete ties to the quitter
//					int cncld = Names.indexOf(message.substring(3));
//					Names.remove(cncld);
//					Sockets.remove(cncld);
//					outToClients.remove(cncld);
//					inFromClients.remove(cncld);
//				}
			}
			if(inFromPeer!=null && inFromPeer.ready()){
				String message = inFromPeer.readLine();
				System.out.println(message);//XXX debug, can keep

				//XXX handle incoming transmission
				if(message.startsWith("mesg")){
					String[] trans = message.substring(4).split("tknzhr");
					if(Names.contains(trans[0])){
						int destno = Names.indexOf(trans[0]);
						outToClients.get(destno).writeBytes(trans[1]+ '\n');
					} else {
						if(outToPeer!=null)outToPeer.writeBytes("fwdmsg" +  message.substring(4) + '\n');
					}

				} else { //XXX handle name requests

					if(message.startsWith("BYE")){
						int x = Names.indexOf(message.substring(3));
						Names.remove(x);
						Sockets.remove(x);
						inFromClients.remove(x);
						outToClients.remove(x);
					}else {
						if(message.startsWith("rqst")){
							String rqstee = message.substring(4);
							MemberListResponse(rqstee);
						} else{
							if(message.startsWith("fwdmsg")){
								System.out.println("gonna forward message");
								String naddaf = message.substring(6);
								String[] trans = naddaf.split("tknzhr");
								if(Names.contains(trans[0])){
									int destno = Names.indexOf(trans[0]);
									outToClients.get(destno).writeBytes(trans[1]+ '\n');
								} else {
									String[] transs = trans[1].split(":");
									if(outToPeer!=null)outToPeer.writeBytes("fwder"+ transs[0] + '\n');
								}
							} else {
								if(message.startsWith("fwder")){
									String ghaby = message.substring(5);
									outToClients.get(Names.indexOf(ghaby)).writeBytes("SERVER: Destination not found; specify an online target"+ '\n');
								} else {
									if(message.startsWith("fwdrqst")){
										System.out.println("sending");
										propList(message.substring(7));
									} else{
										if(message.startsWith("rqt")){
											String [] tkns = message.split("tknzhr");
											outToClients.get(Names.indexOf(tkns[0].substring(3))).writeBytes(tkns[1] +'\n');
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	void propList(String x) throws IOException{
		System.out.println("here");
		for (int i = 0; i < Names.size(); i++) {
			String r = "rqt"+x+ "tknzhrSERVER: " +Names.get(i)+'\n';
			outToPeer.writeBytes(r);
			System.out.println(r);
		}
		System.out.println("done propagating mem list");
	}

	private void MemberListResponse(String rqstee) throws IOException{
		System.out.println("Getting Member list for " + rqstee);
		DataOutputStream x = outToClients.get(Names.indexOf(rqstee));
		for (int i = 0; i < Names.size(); i++) {
			x.writeBytes("SERVER: " + Names.get(i) + "\n");
			System.out.println("sent a name");
		}
		if(outToPeer!=null)outToPeer.writeBytes("fwdrqst"+rqstee+'\n');
	}

	//	private void PropagateMemberList(DataOutputStream x) throws IOException{
	//		outToPeer.writeBytes("prsvrprsvrmlrq" + '\n');
	//		String res = "";
	//		while(!res.equals("finished")){
	//			res = inFromPeer.readLine();
	//			if(!res.equals("finished")){
	//				String [] ans = res.split("rqsteeend");
	//				x.writeBytes(ans[0]+'\n');
	//			} else{
	//				break;
	//			}
	//		}
	//	}

	public static void main(String[] args) throws IOException {
		new Server();
	}

}








//TODO handle forwarding messages to the other servers, and handle receiving them properly, hint: use fwdmsg to forward, propagate forwards



















//public void serve(Socket client1) throws IOException{
//
//		//Client1 is assigned to the first client to try and connect
//
//
//		//This makes input and output connections to the client's socket
//		inFromClient = new BufferedReader(new InputStreamReader(client1.getInputStream()));
//		outToClient = new DataOutputStream(client1.getOutputStream());
//
//		//This takes input from user, 3ala asas enny 3amel el server can send w keda.
//		inFromUser = new BufferedReader(new InputStreamReader(System.in));
//
//		(new Thread(){
//			@Override
//			public void run(){
//				try{
//
//					//infinite loop 3lshan yeseeb el chat session open 3ala tool.
//					while(true){
//
//						//Law fi incoming transmission, process it and print it.
//						if(inFromClient.ready()){
//							String message = inFromClient.readLine();
//							System.out.println("CLIENT: " + message);
//
//							//Law el user wrote bye or quit it quits, kollo null.
//							if(message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("QUIT")){
//								client1 = null;
//								inFromUser = null;
//								outToClient = null;
//								inFromClient = null;
//								System.exit(-1);
//							}
//						}
//
//						//Law el user wants to send, send their message. and print it here as well 3lshan ne3raf el chat history kollo.
//						if(inFromUser.ready()){
//							String message = inFromUser.readLine();
//							System.out.println("SERVER: " + message);
//							outToClient.writeBytes(message + '\n');
//
//							//Law el user wrote bye or quit it quits, kollo null.
//							if(message.equalsIgnoreCase("BYE") || message.equalsIgnoreCase("QUIT")){
//								client1 = null;
//								inFromUser = null;
//								outToClient = null;
//								inFromClient = null;
//								System.exit(-1);
//							}
//						}
//					}
//				} catch (Exception e){e.printStackTrace();}
//			}
//		}).start();
//
//
//	}
//
//	public static void main(String[] args) throws IOException {
//		new Server();
//	}






/*(new Thread(){
			@Override
			public void run(){
				try{
					while(true){

						String message = inFromUser.readLine();
						System.out.println("SERVERFROMUSER: " + message);
						outToClient.writeBytes(message);
					}
				} catch (Exception e){e.printStackTrace();}
			}
		}).start();
 */