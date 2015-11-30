package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

public class ChatServer {
	private ServerSocket server = null;
	private int port;
	private String serverAddress;
	private ServerUsersHandler serverUsersHandler;
	private boolean successfulStart = false;

	public static void main(String args[]) {

		if (args.length == 2 && isInteger(args[0])) {
			ChatServer server = new ChatServer(Integer.parseInt(args[0]), args[1]);
			server.startServerAndListen();

		} else {
			System.out.println("Port must be integer and adress must be specified. Example 5586 localhost or 4751 192.168.1.1");
		}

	}

	/**
	 * Setup variables for chat server
	 * @param port
	 * @param serverName
	 */
	public ChatServer(int port, String serverName) {
		this.port = port;
		this.serverAddress = serverName;
		this.serverUsersHandler = new ServerUsersHandler(this);
		
	}

	/**
	 * Starts server and listens for incoming users (starts chatListener)
	 * 
	 */
	public void startServerAndListen() {
		
		InetAddress address = null;
		boolean serverCreated = false;

		// Get server address
		try {
			address = InetAddress.getByName(serverAddress);
		} catch (UnknownHostException e) {
			System.out.println("Could not find server");
			
		}

		// Start server
		try {
			server = new ServerSocket(port, 50, address);
			serverCreated = true;
			System.out.println("Server created on " + address + ":" + port);

		} catch (IOException e) {
			System.out.println("Could not create server");

		}

		if (serverCreated) {
			// Wait for first client
			Thread clientListner = new Thread(new ClientListener(server, this));
			clientListner.start();
			successfulStart = true;
		}

	}

	

	/**
	 * make packet from online users list and send it to every user in that list
	 */
	public void sendEveryoneOnlinePeopleList() {
		String listOfAllPeople = "";
		ServerUser user = null;

		Iterator<?> it = serverUsersHandler.createInterator();
		while (it.hasNext()) {
	        Map.Entry userRecord = (Map.Entry)it.next();
	        user = (ServerUser) userRecord.getValue();

	        listOfAllPeople = listOfAllPeople + " " + user.getUsername();
	      
	    }
		it = serverUsersHandler.createInterator();
		while (it.hasNext()) {
	        Map.Entry userRecord = (Map.Entry)it.next();
	        user = (ServerUser) userRecord.getValue();
	        user.sendMsg("!onlineList " + listOfAllPeople);
	         
	    }
		
		
	}
	
	
	public ServerUsersHandler getServerUsersHandler() {
		return serverUsersHandler;
	}
	
	public boolean isSuccessfulStart() {
		return successfulStart;
	}

	private static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
