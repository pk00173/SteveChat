package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncryptorTest {

	@Test
	public void test() {
		Encryptor encryptor = new Encryptor();
		assertNotNull(encryptor);
		String encryptedMsg = encryptor.encrypt("This is a test msg", encryptor.getPublicKey());
		assertEquals("This is a test msg", encryptor.decrypt(encryptedMsg));
	}

}
