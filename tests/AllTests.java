import general.UserTest;

import org.junit.Test;

import server.ChatServerTest;
import server.ClientListener;
import server.ClientListenerTest;
import server.ServerUserTest;
import server.ServerUsersHandlerTest;
import client.ChatClientTestMAINTEST;
import client.ClientUserTest;
import client.ControllerTest;
import client.EncryptorTest;


public class AllTests {

	@Test
	public void allTests() {
		ChatClientTestMAINTEST chatClientTest = new ChatClientTestMAINTEST();
		chatClientTest.test();

		ClientUserTest clientUserTest = new ClientUserTest();
		clientUserTest.test();
		
		ControllerTest controllTest = new ControllerTest();
		controllTest.test();
		
		EncryptorTest encryptorTest = new EncryptorTest();
		encryptorTest.test();
		
		UserTest userTest = new UserTest();
		userTest.test();
		
		ChatServerTest chatServerTest = new ChatServerTest();
		chatServerTest.test();
		
		ClientListenerTest clientListenerTest = new ClientListenerTest();
		clientListenerTest.test();
		
		ServerUsersHandlerTest serverUsersHandlerTest = new ServerUsersHandlerTest();
		serverUsersHandlerTest.test();
		
		ServerUserTest serverUsertest =  new ServerUserTest();
		serverUsertest.test();
	}

}
