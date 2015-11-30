package server;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.net.Socket;

import org.junit.Test;

public class ServerUserTest {

	@Test
	public void test() {
		ServerUser serverUser = new ServerUser("TestName", new DataOutputStream(null));
		assertEquals("TestName", serverUser.getUsername());
	}

}
