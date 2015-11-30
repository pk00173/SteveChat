package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientUserTest {

	@Test
	public void test() {
		ClientUser clientUser = new ClientUser("TestName");
		assertEquals("TestName", clientUser.getUsername());
		assertEquals("", clientUser.getConversation());
		clientUser.setConversation("asd");
		assertEquals("asd", clientUser.getConversation());
	}

}
