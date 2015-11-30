package client;

public abstract class ClientVariables {
	
	public static final String[] SERVERS= {"93.185.105.58", "localhost"}; 
	public static final String DEFAULTPORT = "5468";
	
	//Security variables
	public static final int KEYLENGHT = 2048;
	public static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	public static final String ENCODING = "UTF-8";
}
