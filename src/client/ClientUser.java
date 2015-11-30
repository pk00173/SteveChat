package client;

import general.User;
import gui.ClientGUI;

import java.security.PublicKey;



public class ClientUser extends User{
	private boolean unreadMsg;
	private String conversation;
	private PublicKey publicKey;
	
	public ClientUser(String username){
		super(username);
		this.unreadMsg = false;
		this.conversation = "";
	}

	public boolean hasUnreadMsg() {
		return unreadMsg;
	}

	public void setUnreadMsg(boolean unreadMsg) {
		this.unreadMsg = unreadMsg;
		ClientGUI.getInstance().updateUnreadMsgColors();
	}


	public String getConversation() {
		return conversation;
	}

	public void setConversation(String conversation) {
		this.conversation = conversation;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey encryptionKey) {
		this.publicKey = encryptionKey;
	}
	
	
}
