

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server2 {
	
	static ArrayList<User2> users = new ArrayList<>();
	
public static void main(String argv[]) throws Throwable {
		
		
		ServerSocket welcomeSocket = new ServerSocket(7000);

		
		System.out.println(welcomeSocket.getLocalSocketAddress());
		System.out.println(InetAddress.getLocalHost().toString());
		while(true) { 

			Socket connectionSocket = welcomeSocket.accept();
			System.out.println(connectionSocket.getInetAddress().getHostAddress() + " has connected.");

			User2 user = new User2(connectionSocket);
			user.start();
			
		}
      
	} 

 


	

}

	