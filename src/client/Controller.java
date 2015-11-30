package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import gui.ClientGUI;

public class Controller {
	private String previousOnlinePeople = "";
	private ChatClient chatClient;
	private Encryptor encryptor = null;
	private ConcurrentHashMap<String, ClientUser> onlineClients;
	
	public Controller(ChatClient chatClient){
		this.chatClient = chatClient;
		onlineClients = chatClient.getOnlineClients();
		
	}
	
	/**
	 * Handle incoming message packet depending on it's prefix type 
	 * @param packet
	 */
	public void handleIncommingMsg(String msg){
		 if(msg.startsWith("!onlineList")){
			msg = msg.substring(12);
			if(!msg.contentEquals(previousOnlinePeople)){
				updateOnlineList(msg.split(" "));
				
				previousOnlinePeople = msg;

			}
		}else if(msg.startsWith("!msg")){
			String[] packetInfo = msg.split(" ");
			String fromUsername = packetInfo[1];
			
			msg = msg.substring(packetInfo[0].length() + packetInfo[1].length() + 2);
			msg = encryptor.decrypt(msg);
			chatClient.addMsgToConversation(fromUsername, msg, false);
			
			if(ClientGUI.getInstance().getPeopleOnlineList().isSelectionEmpty() || !ClientGUI.getInstance().getSelectedUsername().contentEquals(fromUsername)){
				onlineClients.get(fromUsername).setUnreadMsg(true);
				ClientGUI.getInstance().updateUnreadMsgColors();
			}
		}else if(msg.startsWith("!key")){
			String[] packetInfo = msg.split(" ");
			System.out.println(msg);

			encryptor.setKeyForUser(packetInfo[1], packetInfo[2], packetInfo[3]);
		}else if(msg.startsWith("!requestKey")){
			String[] packetInfo = msg.split(" ");
			encryptor.sendPublicKey(packetInfo[1]);
		}
	}
	
	/**
	 * Update list of online users
	 * @param newPeopleOnline
	 */
	public synchronized void updateOnlineList(String[] newPeopleOnline){
		//Our online list has changed, update it
		//Add new online people
		for (int i = 0; i < newPeopleOnline.length; i++) {
			if(!onlineClients.containsKey(newPeopleOnline[i])){
				//Not in the list, add it
				onlineClients.put(newPeopleOnline[i], new ClientUser(newPeopleOnline[i]));
			}
		}
		List<ClientUser> clientObjectsOnline = new ArrayList<ClientUser>();
		
		//Remove offline people
		for (String key: onlineClients.keySet()) {
			if(!Arrays.asList(newPeopleOnline).contains(key)){
				//Client went offline, remove him
				onlineClients.remove(key);
			}else{
				clientObjectsOnline.add(onlineClients.get(key));
			}
		}
		
		ClientGUI.getInstance().updateOnlineList(clientObjectsOnline);
		
	}
	
	public void setUnreadMsgForUser(String username, boolean unreadMsg){
		onlineClients.get(username).setUnreadMsg(unreadMsg);
	}

	public Encryptor getEncryptor() {
		return encryptor;
	}

	public void setEncryptor(Encryptor encryptor) {
		this.encryptor = encryptor;
	}
	
	
	
}
