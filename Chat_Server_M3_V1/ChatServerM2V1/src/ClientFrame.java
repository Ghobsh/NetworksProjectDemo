import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.OptionPaneUI;
import javax.swing.text.StyledDocument;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClientFrame extends JFrame {

	private JPanel contentPane;
	JTextField input;
	
	BufferedWriter outToServer;
	
	Client client;
	JTextArea ChatLog;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientFrame frame = new ClientFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	int port = 6000; //default as well
	
	public ClientFrame() {
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 414, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		String x = JOptionPane.showInputDialog("please enter the port of the server you want to connect to \n6000 or 6001", 6000);
		
		if(Integer.parseInt(x)==6001) port = 6001;
		
		input = new JTextField();
		input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
////				try {client.outToServer.writeBytes(textField.getText() + '\n');} catch (IOException e) {e.printStackTrace();}
//				client.sendingmessage = input.getText();
//				client.inFromGUI = true;
//			
				client.sendingmessage = input.getText();
				System.out.println(input.getText());
				client.inFromGUI = true;
				try {
					client.help();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		input.setBounds(10, 410, 300, 27);
		contentPane.add(input);
		input.setColumns(10);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//try {client.outToServer.writeBytes(textField.getText() + '\n');} catch (IOException e) {e.printStackTrace();}
				client.sendingmessage = input.getText();
				System.out.println(input.getText());
				client.inFromGUI = true;
				try {
					client.help();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(319, 410, 69, 25);
		contentPane.add(btnNewButton);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 378, 388);
		contentPane.add(scrollPane);
		
		ChatLog = new JTextArea();
		scrollPane.setViewportView(ChatLog);
		repaint();revalidate();
	}
}
