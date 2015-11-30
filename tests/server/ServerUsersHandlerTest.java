package server;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class ServerUsersHandlerTest {

	@Test
	public void test() {
		ServerUsersHandler suh = new ServerUsersHandler(null);
		ServerUser serverUser1 = new ServerUser("Test1", null);
		ServerUser serverUser2 = new ServerUser("Test2", null);
		ServerUser serverUser3 = new ServerUser("Test3", null);
		
		suh.addOnlineClient(serverUser1, false);
		suh.addOnlineClient(serverUser2, false);
		suh.addOnlineClient(serverUser3, false);
		
		Iterator<?> it = suh.createInterator();
		
	        Map.Entry userRecord = (Map.Entry)it.next();
	        ServerUser user = (ServerUser) userRecord.getValue();
        	assertEquals(user, serverUser1);
        	
        	userRecord = (Map.Entry)it.next();
	        user = (ServerUser) userRecord.getValue();
        	assertEquals(user, serverUser3);
        	
        	userRecord = (Map.Entry)it.next();
	        user = (ServerUser) userRecord.getValue();
        	assertEquals(user, serverUser2);
	   
	}

}
