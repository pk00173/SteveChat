package server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

public class ClientListenerTest {

	@Test
	public void test() {
		InetAddress address;
		try {
			ChatServer chatServer =  new ChatServer(2222, "locahost");
			address = InetAddress.getByName("localhost");
			ServerSocket server = new ServerSocket(2222, 50, address);
			
			ClientListener clientListener = new ClientListener(server, chatServer);
			assertNotNull(clientListener);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
