package server;

import general.User;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerUser extends User{
	private DataOutputStream streamOut = null;

	
	public ServerUser(String username, DataOutputStream streamOut){
		super(username);
		this.streamOut = streamOut;
	}

	/**
	 * Send message to this user
	 * @param msg
	 */
	public void sendMsg(String msg){
		try {
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch (IOException e) {
			String[] msgInfo = msg.split(" ");
			sendMsg("Msg could no be dilivered. User " + msgInfo[1] + " is offline");
		}
		
	}

}
