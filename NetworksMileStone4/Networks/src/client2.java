import java.io.*;
import java.net.*;
    public class client2 {
         static boolean flag = true;
         static Socket clientSocket;
         static DataOutputStream sendToServer;
         static BufferedReader inputFromServer ;
         static BufferedReader inputText ;
         static String destination=" ";
         static String clientName=" ";
          static String clientText = "";


         private static  void setDestination(String d) {
                 destination = d;
         }
         protected static  String getDestination() {
                 return destination;
         }
         public static void chat(String Source, String destination, double TTL , String Msg) throws IOException{
                 if(Source.equals(destination)){
                         sendToServer.writeBytes(Msg+'\n');
                         sendToServer.flush();
                 }
                 else{
                         sendToServer.writeBytes("/"+Source+"/"+destination+"/"+TTL+"/"+Msg+'\n');
                         sendToServer.flush();
                 }
         }



         public static void main(String argv[]) throws Exception
         {
                 String clientSentence = "";
                 String serverSentence = "";
  BufferedReader userinput = new BufferedReader(new InputStreamReader(System.in));
                 clientSocket = new Socket("localhost",7000);
                 sendToServer = new DataOutputStream(clientSocket.getOutputStream());
                 inputFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                       

                        
 Thread receiving = new Thread() {
     public void run(){
	 
   String serverSentence = "";
       while(!serverSentence.equals("BYE") && !serverSentence.equals("QUIT") && flag){
                try {
                      if(inputFromServer.ready()){
                                                        
                     try {
                           serverSentence = inputFromServer.readLine();
                          if(serverSentence.contains("Successfully connected as ")){
                          
                          int x   ="Successflly connected as ".length();
                          String s = serverSentence.substring(x,serverSentence.length());           
                            clientName=s;
                                                             }
                                                                        
                           System.out.println(serverSentence);
                                                                }

                       catch (Exception e) {
                          e.printStackTrace();
                          }
                             }
                                 } 
                           catch (IOException e){
                                    e.printStackTrace();
                                               }
                                                    }
                                        flag = false;
                                        return;
                                                      }
                                                        };
                        
Thread sending = new Thread(){
   public void run()  {

	   String clientSentence = "";
                                      
 while(!clientSentence.equals("BYE") && !clientSentence.equals("QUIT") && flag){
   try{
          if(userinput.ready()){
             clientSentence = userinput.readLine();
              if(clientSentence.toLowerCase().contains("chatwith")){
                 String[] split = clientSentence.split(":");
                   String clientDest = split[1];
                                                                        
                   setDestination(clientDest);
                                                                        
                    System.out.println("Messages will be delivered to " + clientDest);
                                                                      
                          clientText = userinput.readLine();
                          }
                    
              chat(clientName,getDestination(),2.0, clientSentence);
                                                                       }
                                                                          } 
      
                            catch(Exception e){
                          e.printStackTrace();
                                               }                       
                                                }
          try {
                sendToServer.writeBytes(clientSentence );
                  sendToServer.flush();
                                        } 
         
          catch (IOException e) {
              e.printStackTrace();                     
                                 }
          
          flag = false;
          return;                     
                                }
                       
                             }; 
 if(clientSentence.equals("BYE") || clientSentence.equals("QUIT") ) {
                        	  clientSocket.close();
                        	  }
 
          sending.start();
          receiving.start();
          sending.join();
           receiving.join();
           clientSocket.close(); 
           System.exit(0);
                             
                             
                     
                               }                              
                         
                
        
          

}
