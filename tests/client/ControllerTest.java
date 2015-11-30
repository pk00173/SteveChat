package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ControllerTest {

	@Test
	public void test() {
		ChatClient chatClinet = ChatClient.getInstance();
		Controller controller = new Controller(chatClinet);
		assertNotNull(controller);
		Encryptor encryptor = new Encryptor();
		controller.setEncryptor(encryptor);
		assertEquals(encryptor, controller.getEncryptor());
	}

}
