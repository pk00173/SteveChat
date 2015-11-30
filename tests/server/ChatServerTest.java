package server;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChatServerTest {

	@Test
	public void test() {
		ChatServer chatServer = new ChatServer(1111, "localhost");
		assertNotNull(chatServer);
		chatServer.startServerAndListen();
		assertTrue(chatServer.isSuccessfulStart());
	}

}
