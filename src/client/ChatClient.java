package client;

import gui.ClientGUI;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;

public class ChatClient {
	private static ChatClient instance = null;
	private String loginMsg = null;
	private String[] serverData = null;
	private Socket socket = null;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	private boolean loggedIn = false;
	private ConcurrentHashMap<String, ClientUser> onlineClients;
	private boolean chatAlive = false;
	private String username;
	private boolean connectedToServer = false;
	private Controller controller;

	/**
	 * 
	 * @param args[String port, String IP]
	 *  
	 */
	public static void main(String[] args) {

		ChatClient chatClient = ChatClient.getInstance();
		chatClient.startChatClient(chatClient);

	}
	
	private ChatClient() {
		this.onlineClients = new ConcurrentHashMap<String, ClientUser>();
		this.controller = new Controller(this);

	}
	/**
	 * Starts GUI, handles login process and
	 * opens new input stream listener for chatClient
	 * @param chatClient
	 * 
	 */
	public void startChatClient(ChatClient chatClient) {
		chatClient.startGUI();
		
		requireLogin();
		
		//Person logged in
	
		System.out.println("Logged in as "+ username);
		chatAlive = true;
		
		//GenerateKeys
		controller.setEncryptor(new Encryptor());
		
		ClientGUI.getInstance().changeToChat();
		readStreamIn();
	}
	
	
	/**
	 * Singleton getter
	 * @return this ChatClient
	 */
	public static ChatClient getInstance() {
		if (instance == null) {
			instance = new ChatClient();
		}
		return instance;
	}
	
	/**
	 * send message to server without encryption 
	 * @param packet as String
	 */
	public void sendSystemData(String msg){
		try {
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch (IOException e) {
			logOut();
		}
		
	} 
	
	/**
	 * Creates packet from information in GUI and input
	 * encrypts message and sends it to server 
	 * @param msg
	 * 
	 */
	public synchronized void sendMsg(String msg, String receiverUsername){
		
		if(streamOut != null){
			try {
				if(receiverUsername!= ""){
					while(onlineClients.get(receiverUsername).getPublicKey() == null){
						controller.getEncryptor().requestPublicKey(receiverUsername); 
						Thread.sleep(500);
					}
					//Adding it to the gui first looks good/fast
					addMsgToConversation(ClientGUI.getInstance().getReceiver(), msg, true);
					ClientGUI.getInstance().clearMsgField();
					
					String packet = "!msg "+receiverUsername +" "+username+" "+ controller.getEncryptor().encrypt(msg, getUser(receiverUsername).getPublicKey());
					streamOut.writeUTF(packet);
					streamOut.flush();
					
				}
			} catch (IOException e) {
				logOut();
			} catch (InterruptedException e) {
				logOut();
			}
		}
	}
	
	/**
	 * Add incoming message to certain user and if 
	 * that client is selected, update gui
	 * @param secondClient
	 * @param msg
	 * @param outCommingMsg
	 */
	public void addMsgToConversation(String secondClient, String msg, boolean outCommingMsg){
		String fromPrefix = "";
		if(outCommingMsg){
			fromPrefix = "You : ";
		}else{
			fromPrefix = secondClient + " : ";
		}
		onlineClients.get(secondClient).setConversation(onlineClients.get(secondClient).getConversation() +fromPrefix + msg+" \n");
		
		//If person is already selected (ongoing communication), update chat text area
		if(ClientGUI.getInstance().getPeopleOnlineList().getSelectedValue() != null && ClientGUI.getInstance().getSelectedUsername().contentEquals(secondClient)){
    		ClientGUI.getInstance().getChat().setText(onlineClients.get(ClientGUI.getInstance().getSelectedUsername()).getConversation());
    	}
	}
	
	
	/**
	 * Handle login process
	 * This method should be private but I cannot test it without it being public
	 */
	public void requireLogin(){
		while (!loggedIn) {
			// Wait for server data
			while (serverData == null) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					//It woke up early
				}
			}
			
			// Connect to server
			connectToServer(serverData[1], Integer.parseInt(serverData[0]),true);
			sendLoginDataToServerAndWaitForResponse(loginMsg, true);
		}
	}
	
	/**
	 * This method sends login data to the server if connection was successful 
	 */
	public String sendLoginDataToServerAndWaitForResponse(String loginMsgToSend, boolean popUpMessage){
		String result = "Could not connect to the server";
		if (connectedToServer) {
			boolean streamsOpen = false;
			try {
				streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				streamOut = new DataOutputStream(socket.getOutputStream());
				streamsOpen = true;
			} catch (IOException e) {
				logOut();
			}
			if (streamsOpen) {
				// Send login info
				sendSystemData(loginMsgToSend);

				
				// Wait for server to answer
				String loginAnswer = "";
				try {
					loginAnswer = streamIn.readUTF();
				} catch (IOException e) {
					logOut();
				}
				if (loginAnswer.contentEquals("!system Loggedin")) {
					loggedIn = true;
					result = "Login was successful";
				}else if(loginAnswer.contentEquals("!system Wrong username or password")){
					if(popUpMessage){
						popupWarningMsg("You have entered wrong username or password");
					}
					result = "Wrong password";
				}else if(loginAnswer.contentEquals("!system User already registered")){
					if(popUpMessage){
						popupWarningMsg("User with this username is already registered");
				}
					result = "Already registered";
				}else if(loginAnswer.contentEquals("!system User already logged in")){
					if(popUpMessage){
						popupWarningMsg("User with this username is already logged in");
				}
					result = "Already logged in";
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param ip
	 * @param port
	 * @param useGui
	 */
	public void connectToServer(String ip, Integer port, boolean useGui){
		try {
			if(useGui)
				ClientGUI.getInstance().disableButtons();
			
			socket = new Socket(ip, port);
			//socket = new Socket(serverData[1], Integer.parseInt(serverData[0]));
			connectedToServer = true;
			serverData = null;
		} catch (UnknownHostException e1) {
			
			logOut();
			
		} catch (IOException e) {
			//COULD NOT CONNECT TO SERVER
			popupWarningMsg("Could not connect to server, please try later or differenet server.");
	
			serverData = null;
		}
		if(useGui)
			ClientGUI.getInstance().enableButtons();
	}
	
    /**
     * open new streaming and listen for messages from the server
     */
	public void readStreamIn(){
		while (chatAlive) {
			String msg = null;
			boolean stillActive = false;
			
			try {
				msg = streamIn.readUTF();
				stillActive = true;
			} catch (SocketException e) {
				chatAlive = false;
				popupWarningMsg("Servers are down, please try later.");
				logOut();
			} catch (IOException e) {
				chatAlive = false;
				popupWarningMsg("Servers are down, please try later.");
				logOut();
			}
			if(stillActive){
				controller.handleIncommingMsg(msg);
				System.out.println(msg);
			}
			
		}
	}
	/**
	 * Unlink all current information, 
	 * change GUI to login card and 
	 * handle login again
	 */
	public void logOut(){
		streamIn = null;
		streamOut = null;
		chatAlive = false;
		connectedToServer = false;
		onlineClients.clear();
		loginMsg = null;
		loggedIn = false;
		serverData = null;
		controller.handleIncommingMsg("!onlineList ");
		ClientGUI.getInstance().changeToLogin();
		startChatClient(this);
	}
	
	public void logOutAndExit(){
		streamIn = null;
		streamOut = null;
		chatAlive = false;
		connectedToServer = false;
		onlineClients.clear();
		loginMsg = null;
		loggedIn = false;
		serverData = null;
		controller.handleIncommingMsg("!onlineList ");
		ClientGUI.getInstance().changeToLogin();
		
	}


	public void startGUI(){
		 ClientGUI.getInstance();
	}

	public void popupWarningMsg(String warning) {
		JOptionPane.showMessageDialog(null, warning, "SteveChat", JOptionPane.INFORMATION_MESSAGE);
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ClientUser getUser(String username){
		return onlineClients.get(username);
	}

	public void setLoginMsg(String loginMsg) {
		this.loginMsg = loginMsg;
	}

	public void setServerData(String[] serverData) {
		this.serverData = serverData;
	}

	public ConcurrentHashMap<String, ClientUser> getOnlineClients() {
		return onlineClients;
	}

	public Controller getController() {
		return controller;
	}

	public boolean isChatAlive() {
		return chatAlive;
	}

	public void setChatAlive(boolean chatAlive) {
		this.chatAlive = chatAlive;
	}
	
	
}
