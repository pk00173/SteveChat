package gui;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;

import client.ChatClient;
import client.ClientUser;
import client.ClientVariables;

public class ClientGUI extends JFrame  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private CardLayout cl = new CardLayout();
	private JTextField usernameLogin;
	private JPasswordField passwordLogin;
	private JTextField usernameRegister;
	private JList<ClientUser> peopleOnlineList;
	private JPasswordField passwordRegister;
	private JTextArea chat;
	private JLabel chattingWithLabel;
	private JLabel clientUserame;
	private JTextArea msgTextArea;
	private static ClientGUI instance = null;
	private JButton sendMsgButton;
	private boolean chatActivated = false;
	private JScrollPane normalChatPane;
	private JComboBox<?> serversBox;
	private String[] servers = ClientVariables.SERVERS;
	private String defaultPort = ClientVariables.DEFAULTPORT;
	private JTextField portField;
	private JButton btnLogin;
	private JButton btnRegister;
	private boolean loaded = false;

	
	public static ClientGUI getInstance() {
		if (instance == null) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						instance = new ClientGUI();
						instance.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		return instance;
	}
	


	private ClientGUI() {
		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 593, 478);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(cl);
		setContentPane(contentPane);
		
		JPanel loginPane = new JPanel();
		contentPane.add(loginPane, "1");
		loginPane.setLayout(null);
		
		usernameLogin = new JTextField();
		usernameLogin.setBounds(54, 178, 152, 29);
		loginPane.add(usernameLogin);
		usernameLogin.setColumns(10);
		
		passwordLogin = new JPasswordField();
		passwordLogin.setBounds(54, 240, 152, 29);
		loginPane.add(passwordLogin);
		passwordLogin.setColumns(10);
		
		usernameRegister = new JTextField();
		usernameRegister.setBounds(354, 178, 152, 29);
		loginPane.add(usernameRegister);
		usernameRegister.setColumns(10);
		
		passwordRegister = new JPasswordField();
		passwordRegister.setBounds(354, 240, 152, 29);
		loginPane.add(passwordRegister);
		passwordRegister.setColumns(10);
		
		
		btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(!ChatClient.getInstance().isLoggedIn()){

					if(isInteger(portField.getText())){
						
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

							@Override
							protected Void doInBackground() throws Exception {
								//ConnectToServer
								ChatClient.getInstance().setServerData(new String[]{portField.getText(), serversBox.getSelectedItem().toString()});
								ChatClient.getInstance().setLoginMsg("!login user="+usernameLogin.getText().replaceAll(" ", "")+" pass="+getLoginPassword());
								clientUserame.setText(usernameLogin.getText());
								ChatClient.getInstance().setUsername(usernameLogin.getText());
								return null;
							}
							
						};
						
						worker.execute();
					}else{
						ChatClient.getInstance().popupWarningMsg("Port must be an integer.");
					}
				}
			}
		});
		btnLogin.setBounds(88, 295, 89, 23);
		loginPane.add(btnLogin);
		
		btnRegister = new JButton("Register");
		btnRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(!ChatClient.getInstance().isLoggedIn()){
				
					if(isInteger(portField.getText())){
						
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

							@Override
							protected Void doInBackground() throws Exception {
								// ConnectToServer
								ChatClient.getInstance().setServerData(new String[] { portField.getText(), serversBox.getSelectedItem().toString() });
								ChatClient.getInstance().setLoginMsg("!register user=" + usernameRegister.getText().replaceAll(" ", "") + " pass=" + getRegisterPassword());
								clientUserame.setText(usernameRegister.getText());
								ChatClient.getInstance().setUsername(usernameRegister.getText());
								return null;
							}

						};

						worker.execute();
					}else{
						ChatClient.getInstance().popupWarningMsg("Port must be an integer.");
					}
				}
			}
		});
		btnRegister.setBounds(391, 295, 89, 23);
		loginPane.add(btnRegister);
		
		JLabel lblStevechat = new JLabel("SteveChat");
		lblStevechat.setFont(new Font("Papyrus", Font.PLAIN, 35));
		lblStevechat.setBounds(182, 11, 187, 50);
		loginPane.add(lblStevechat);
		
		JLabel lblBeta = new JLabel("Beta 0.8");
		lblBeta.setBounds(354, 57, 46, 14);
		loginPane.add(lblBeta);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(276, 72, 15, 347);
		loginPane.add(separator);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
		lblUsername.setBounds(95, 152, 82, 15);
		loginPane.add(lblUsername);
		
		JLabel label = new JLabel("Username");
		label.setFont(new Font("Arial", Font.PLAIN, 14));
		label.setBounds(391, 152, 89, 15);
		loginPane.add(label);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 13));
		lblPassword.setBounds(98, 218, 71, 14);
		loginPane.add(lblPassword);
		
		JLabel label_1 = new JLabel("Password");
		label_1.setFont(new Font("Arial", Font.PLAIN, 13));
		label_1.setBounds(397, 215, 71, 14);
		loginPane.add(label_1);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Arial", Font.PLAIN, 17));
		lblLogin.setBounds(108, 88, 69, 29);
		loginPane.add(lblLogin);
		
		JLabel lblRegister = new JLabel("Register");
		lblRegister.setFont(new Font("Arial", Font.PLAIN, 17));
		lblRegister.setBounds(391, 88, 83, 29);
		loginPane.add(lblRegister);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(60, 120, 146, 8);
		loginPane.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(344, 120, 146, 8);
		loginPane.add(separator_2);
		
		JLabel lblServer = new JLabel("Server");
		lblServer.setBounds(48, 374, 46, 14);
		loginPane.add(lblServer);
		
		serversBox = new JComboBox<Object>(servers);
		serversBox.setBounds(10, 399, 127, 20);
		loginPane.add(serversBox);
		
		portField = new JTextField();
		portField.setText(defaultPort);
		portField.setBounds(157, 399, 71, 20);
		loginPane.add(portField);
		portField.setColumns(10);
		
		JLabel label_2 = new JLabel(":");
		label_2.setBounds(147, 402, 15, 14);
		loginPane.add(label_2);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(171, 374, 46, 14);
		loginPane.add(lblPort);
		
		
		
		JPanel chatPane = new JPanel();
		chatPane.setVisible(false);
		contentPane.add(chatPane, "2");
		chatPane.setLayout(null);
		
		msgTextArea = new JTextArea();
		msgTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {
							ChatClient.getInstance().sendMsg(msgTextArea.getText().substring(0,msgTextArea.getText().length()-1), getReceiver());
							return null;
						}
					};
					worker.execute();
				}
			}
		});
		msgTextArea.setLineWrap(true);
		msgTextArea.setEnabled(false);
		msgTextArea.setBounds(175, 349, 283, 47);
		chatPane.add(msgTextArea);
		
		sendMsgButton = new JButton("Send");
		sendMsgButton.setEnabled(false);

		sendMsgButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						ChatClient.getInstance().sendMsg(msgTextArea.getText(), getReceiver());
						return null;
					}
				};
				worker.execute();
			}
		});
		sendMsgButton.setBounds(468, 349, 89, 47);
		chatPane.add(sendMsgButton);
		
		normalChatPane = new JScrollPane();
		normalChatPane.setBounds(175, 83, 382, 255);
		chatPane.add(normalChatPane);
		
		chat = new JTextArea();
		chat.setLineWrap(true);
		chat.setEditable(false);
		DefaultCaret caret = (DefaultCaret)chat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		normalChatPane.setViewportView(chat);
		
		peopleOnlineList = new JList<ClientUser>();
		peopleOnlineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		peopleOnlineList.setCellRenderer(new SelectedListCellRenderer());
		peopleOnlineList.setBounds(10, 83, 136, 319);
		chatPane.add(peopleOnlineList);
		
		peopleOnlineList.addListSelectionListener(new ListSelectionListener() {

	            @Override
	            public void valueChanged(ListSelectionEvent arg0) {
	                if (!arg0.getValueIsAdjusting()) {
	                	if(peopleOnlineList.getSelectedValue() != null){
	                		chattingWithLabel.setText(peopleOnlineList.getSelectedValue().getUsername());
	                		chat.setText(peopleOnlineList.getSelectedValue().getConversation());
	                		ChatClient.getInstance().getController().setUnreadMsgForUser(getSelectedUsername(), false);
	                		activateChat();
	                	}
	                }
	            }
	        });
		 
		
		chattingWithLabel = new JLabel("");
		chattingWithLabel.setFont(new Font("Tahoma", Font.PLAIN, 23));
		chattingWithLabel.setBounds(175, 32, 232, 40);
		chatPane.add(chattingWithLabel);
		
		clientUserame = new JLabel("");
		clientUserame.setFont(new Font("Tahoma", Font.BOLD, 11));
		clientUserame.setBounds(415, 1, 152, 18);
		chatPane.add(clientUserame);
		
		JLabel Name = new JLabel("SteveChat");
		Name.setFont(new Font("Papyrus", Font.PLAIN, 16));
		Name.setBounds(10, 0, 116, 36);
		chatPane.add(Name);
		
		JLabel lblUsersOnline = new JLabel("Users Online");
		lblUsersOnline.setBounds(41, 59, 73, 18);
		chatPane.add(lblUsersOnline);
		
		JLabel lblLoogedInAs = new JLabel("Logged In as:");
		lblLoogedInAs.setBounds(333, 0, 98, 18);
		chatPane.add(lblLoogedInAs);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(157, 83, 9, 319);
		chatPane.add(separator_3);
		
		loaded = true;
	}
	public String getLoginUsername(){
		return usernameLogin.getText();
	}
	
	public JLabel getClientUserame() {
		return clientUserame;
	}



	public String getRegisterUsername(){
		return usernameRegister.getText();
	}
	public String getLoginPassword(){
		return new String(passwordLogin.getPassword());
	}
	public String getRegisterPassword(){
		return new String(passwordRegister.getPassword());
	}
	public void changeToLogin(){
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				cl.show(contentPane, "1");
			}

		});
	}
	public void changeToChat(){
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				clearChat();
				cl.show(contentPane, "2");
			}

		});

	}
	public void addMsg(String fromUsername, String msg){
		
		chat.setText(chat.getText()+" "+msg+"\n");
	}

	public String getReceiver() {
		
		return chattingWithLabel.getText();
	}
	public void clearMsgField() {
		msgTextArea.setText("");
		
	}
	public void updateOnlineList(List<ClientUser> onlinePeople) {
		final DefaultListModel<ClientUser> model = new DefaultListModel<ClientUser>();
		for (int i = 0; i < onlinePeople.size(); i++) {
			if(!onlinePeople.get(i).getUsername().contentEquals(ChatClient.getInstance().getUsername())){
				model.addElement(onlinePeople.get(i));
			}
		}
		
	
	 peopleOnlineList.setModel(model);
				
	
		
		
	}
	public void updateUnreadMsgColors(){
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				peopleOnlineList.updateUI();
				
			}

		});

		
	}
	public void setUsername(String username) {
		clientUserame.setText(username);
		
	}



	public JTextArea getChat() {
		return chat;
	}



	public JList<ClientUser> getPeopleOnlineList() {
		return peopleOnlineList;
	}
	public String getSelectedUsername(){
		return peopleOnlineList.getSelectedValue().getUsername();
	}
	private void activateChat(){
		if(!chatActivated){
			sendMsgButton.setEnabled(true);
			msgTextArea.setEnabled(true);
			normalChatPane.setVisible(true);
			chatActivated = true;
		}
	}
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}



	public JButton getBtnLogin() {
		return btnLogin;
	}
	
	public JButton gerBtnRegister() {
		return btnRegister;
	}



	public JComboBox<?> getServersBox() {
		return serversBox;
	}



	public JTextField getPortField() {
		return portField;
	}

	public void disableButtons(){
		btnLogin.setEnabled(false);
		btnRegister.setEnabled(false);
	}

	public void enableButtons(){
		btnLogin.setEnabled(true);
		btnRegister.setEnabled(true);
	}



	public boolean isLoaded() {
		return loaded;
	}

	public void clearChat(){
		getChat().setText("");
		
	}

	
}
