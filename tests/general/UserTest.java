package general;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

	@Test
	public void test() {
		User user = new User("TestUsername");
		assertEquals("TestUsername", user.getUsername());
	}

}
