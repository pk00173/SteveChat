package server;

import java.util.HashMap;
import java.util.Iterator;

public class ServerUsersHandler implements ServerUsersIterator{
	
	private HashMap<String, ServerUser> onlineClients;
	private ChatServer chatserver;
	
	public ServerUsersHandler(ChatServer chatServer){
		this.onlineClients = new HashMap<String, ServerUser>();
		this.chatserver = chatServer;

	}
	
	@Override
	public Iterator createInterator() {
		
		return onlineClients.entrySet().iterator();
	}
	
	/**
	 * Add client to list and update everyones list
	 * @param client
	 */
	public void addOnlineClient(ServerUser client) {
		
			this.onlineClients.put(client.getUsername(), client);
			chatserver.sendEveryoneOnlinePeopleList();
		
	}
	
	/**
	 * Add client to list and and if sendEveryoneNotification is true, update everyones list
	 * @param client
	 * @param sendEveryoneNotification
	 */
	public void addOnlineClient(ServerUser client, boolean sendEveryoneNotification) {
		
			this.onlineClients.put(client.getUsername(), client);
			if(sendEveryoneNotification)
				chatserver.sendEveryoneOnlinePeopleList();
		
	}
	
	/**
	 * Remove client from list and update everyones list
	 * @param client
	 */
	public void setClientOffline(ServerUser client) {
		
			this.onlineClients.remove(client.getUsername());
			
			chatserver.sendEveryoneOnlinePeopleList();	
		
	}
	
		/**
		 * Remove client from list and if sendEveryoneNotification is true, update everyones list
		 * @param client
		 * @param sendEveryoneNotification
		 * 
		 */
	public void setClientOffline(ServerUser client, boolean sendEveryoneNotification) {
		
			this.onlineClients.remove(client.getUsername());
			if(sendEveryoneNotification)
				chatserver.sendEveryoneOnlinePeopleList();	
		
	}
	
	public int getNumberOfClients() {
		return this.onlineClients.size();
	}

	public ServerUser getClientByName(String clientName) {
		return this.onlineClients.get(clientName);
	}


}
