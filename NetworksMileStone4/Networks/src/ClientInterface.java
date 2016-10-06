import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class ClientInterface extends JFrame {

	JPanel contentPane;
	JTextField textField;
	JTextArea textArea = new JTextArea();
	ClientInstance client;
	
	
	int port = 0;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientInterface frame = new ClientInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientInterface() {
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 471, 472);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		String portGetter = JOptionPane.showInputDialog("Please input the port you want to connect to, choose from 6000 to 6003 \nThe default valuse is 6000 if you type in anything else");
		try{port = Integer.parseInt(portGetter);}catch(Exception e){port=6000;} if(port!=6000&&port!=6001&&port!=6002&&port!=6003)port=6000;
		
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client.ready = true;
			}
		});
		textField.setBounds(95, 382, 265, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client.ready = true;
			}
		});
		btnSend.setBounds(370, 381, 75, 26);
		contentPane.add(btnSend);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 435, 360);
		contentPane.add(scrollPane);
		
		
		scrollPane.setViewportView(textArea);
		
		JButton button = new JButton("Help");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showInputDialog("To view the members list type in getmemberslist \nTo chat type the name of the recepient followed by <> then the message that you want to send");
			}
		});
		button.setBounds(10, 381, 75, 26);
		contentPane.add(button);
		
		repaint();
		revalidate();
	}
}
