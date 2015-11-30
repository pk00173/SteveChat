package client;

import static org.junit.Assert.*;
import gui.ClientGUI;

import org.junit.Test;

import server.ChatServer;

public class ChatClientTestMAINTEST {
	
	public void createServer(){
		ChatServer server = new ChatServer(1456, "localhost");
		server.startServerAndListen();
	}
	
	
	
	@Test
	public void test() {
		
		
		
		Thread serverThread = new Thread() {
		    public void run() {
		    	createServer();
		    }  
		};

		serverThread.start();
		
		Thread clientThread = new Thread() {
		    public void run() {
		    	ChatClient chatClient = ChatClient.getInstance();
				assertNotNull(chatClient);
				
				chatClient.startGUI();
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				chatClient.connectToServer("localhost", 1456, false);
				assertEquals("Login was successful", chatClient.sendLoginDataToServerAndWaitForResponse("!login user=Pavel pass=pavel", false));
				
				chatClient.getController().setEncryptor(new Encryptor());
				
				ClientGUI.getInstance().changeToChat();
				chatClient.setChatAlive(true);
				
				chatClient.connectToServer("localhost", 1456, false);
				assertEquals("Already registered", chatClient.sendLoginDataToServerAndWaitForResponse("!register user=Pavel pass=pavel", false));
		    }  
		};

		clientThread.start();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}

}
