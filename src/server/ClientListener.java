package server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ClientListener implements Runnable {

	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;
	private ServerSocket serverSocket;
	private ChatServer chatServer;
	private boolean chatAlive;
	private boolean streamOpened = false;
	private String username = "";
	private FileHandler fileHandler;

	public ClientListener(ServerSocket serverSocket, ChatServer chatServer) {
		this.serverSocket = serverSocket;
		this.chatServer = chatServer;
		this.chatAlive = true;
		this.fileHandler = new FileHandler();
	}

	public void run() {
		waitForClient(serverSocket);
	}

	private void startStreams(Socket socket){
		try {
			streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			streamOut = new DataOutputStream(socket.getOutputStream());
			streamOpened = true;
		} catch (IOException e) {
			streamOpened = false;
		}
	}
	
	
	/**
	 * Handle login/registration information 
	 * @param loginInfo
	 * @return true/false, depending if login was successful or not
	 */
	private boolean handleLoginProcess(String loginInfo){
		boolean loggedIn = false;
		
		String[] loginInfoSplitted = loginInfo.split(" ");
		
		if(loginInfoSplitted[2]!= null){
			username = loginInfoSplitted[1].substring(5);
			
			if (loginInfo.startsWith("!register")) {
				if (!fileHandler.checkIfFileContains("users.txt", username)) {
					fileHandler.appendToFile("users.txt", loginInfo.substring(10));
					loggedIn = true;
				}else{
					sendMsg("!system User already registered");
					
				}
			} else if (loginInfo.startsWith("!login")) {
				
				if (chatServer.getServerUsersHandler().getClientByName(username) != null) {
					sendMsg("!system User already logged in");
					
				} else if (fileHandler.checkIfFileContains("users.txt", loginInfo.substring(7))) {
					loggedIn = true;
				} else {
					sendMsg("!system Wrong username or password");
					
				}
			}
			
		}
		return loggedIn;
	}
	
	/**
	 * Wait for client and handle login/registration process
	 * @param serverSocket
	 */
	private void waitForClient(ServerSocket serverSocket) {
		try {
			// Wait for client
			System.out.println("Waiting for clients to connect");
			Socket socket = serverSocket.accept();
			// Client connected, create new thread for new waiting for new  client
			Thread clientListner = new Thread(new ClientListener(serverSocket, chatServer));
			clientListner.start();

			// Read input and prepare output stream
			
			System.out.println("Waiting for user to login");
			startStreams(socket);
			
			if (streamOpened) {
				// Handle login
				String loginInfo = "";
				boolean stillActive = true;
				try {
					loginInfo = streamIn.readUTF();
				} catch (SocketException e) {
					// User disconnected
					stillActive = false;
				}

				if (stillActive) {
					
					boolean loggedIn = handleLoginProcess(loginInfo);
					
					
					if (loggedIn) {
						
						sendMsg("!system Loggedin");
						System.out.println("User "+username+" has logged in.");
						ServerUser client = new ServerUser(username, streamOut);
						chatServer.getServerUsersHandler().addOnlineClient(client);

						// Forward messages from one client to another
						handleMessages(client);
					}
				}
			}
		} catch (IOException e) {
			
		}

	}
	/**
	 * handles message in certain way
	 * depending on packet prefix, probably forwards message
	 * @param client
	 */
	private void handleMessages(ServerUser client){
		while (chatAlive) {
			String msg = "";
			try {

				try {
					msg = streamIn.readUTF();
				} catch (SocketException e) {
					closeConnection(client);
				}
				
				if (msg != "" && chatAlive) {

					if (msg.startsWith("!msg") || msg.startsWith("!key") || msg.startsWith("!requestKey")) {
						String[] msgInfo = msg.split(" ");
						String msgText = msg.substring(msgInfo[0].length() + msgInfo[1].length() + 2);
						
						if (chatServer.getServerUsersHandler().getClientByName(msgInfo[1]) != null) {
							chatServer.getServerUsersHandler().getClientByName(msgInfo[1]).sendMsg(msg.substring(0,msgInfo[0].length())+" " + msgText);
							System.out.println(msg.substring(0,msgInfo[0].length())+" " + msgText);
						} 
					} 
				}
			} catch (IOException e) {
				closeConnection(client);
			}
		}
	}
	
	/**
	 * Sends message to user that is connected to this chatListener
	 * @param msg
	 */
	private void sendMsg(String msg){
		try {
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch (IOException e) {
			//Unable to send msg
			System.out.println("Unable to connect to client");
			chatAlive = false;
			
		}
		
	}



	private void closeConnection(ServerUser client) {
		System.out.println("User "+client.getUsername()+" is now offline");
		chatServer.getServerUsersHandler().setClientOffline(client);
		chatAlive = false;
	}
}
