package client;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;


public class Encryptor {

    
    private static PrivateKey privateKey;
    private static PublicKey publicKey;
    private final static String transformation = "RSA/ECB/PKCS1Padding";
    private final static String encoding = "UTF-8";
    
	/**
	 * Generate new public and private key
	 * with every new instance of Encryptor
	 */
	public Encryptor() {
		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(ClientVariables.KEYLENGHT);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
		} catch (NoSuchAlgorithmException e) {
			ChatClient.getInstance().popupWarningMsg("Unable to gererate encryption keys. Please try again.");
		}
		
	}
	/**
	 * Encrypts a message with certain users public key
	 * @param rawText
	 * @param key
	 * @return cipher message
	 */
	public String encrypt(String rawText, PublicKey key){
		Cipher cipher;
		String cipherText = null;
		try {
			cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = DatatypeConverter.printBase64Binary(cipher.doFinal(rawText.getBytes(encoding)));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			ChatClient.getInstance().popupWarningMsg("Unable to encrypt your message.");
		}
	
		return cipherText;
	}
	/**
	 * Decrypts message with private key
	 * @param cipherText
	 * @return
	 */
	public String decrypt(String cipherText){
		Cipher cipher;
		String result = null;
		try {
			cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			result = new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(cipherText)), encoding);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
			ChatClient.getInstance().popupWarningMsg("Unable to decrypt incomming message");
		}
		
		return result;
	}
	/**
	 * Generate public key with given modulus and exponent
	 * @param modulus
	 * @param exponent
	 * @return public key
	 */
	public PublicKey createPublicKey(String modulus, String exponent){
		KeyFactory fact;
		PublicKey key = null;
		try {
			fact = KeyFactory.getInstance("RSA");
			key = fact.generatePublic(new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(exponent)));		
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			ChatClient.getInstance().popupWarningMsg("Other client send invalid key.");

		}
		
		return key;
	}
	
	/**
	 * Send current public key to some other user in form of packet
	 * @param username of user this public key should be send to
	 */
	public void sendPublicKey(String toUser){
        KeyFactory fact;
		try {
			fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec publicKeyInfo = fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
			ChatClient.getInstance().sendSystemData("!key "+toUser+" " +ChatClient.getInstance().getUsername()+" "+publicKeyInfo.getModulus()+" "+publicKeyInfo.getPublicExponent());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			ChatClient.getInstance().popupWarningMsg("Unable to send public key.");

		}
		
	}

	/**
	 * Generates public key for certain user
	 * @param username
	 * @param modulus
	 * @param exponent
	 */
	public void setKeyForUser(String username, String modulus, String exponent) {
		PublicKey usersPublicKey = createPublicKey(modulus, exponent);
		ChatClient.getInstance().getUser(username).setPublicKey(usersPublicKey);
	}
	
	/**
	 * Send public key request to certain user 
	 * @param receiver
	 */
	public void requestPublicKey(String receiver) {
		ChatClient.getInstance().sendSystemData("!requestKey " + receiver + " "+ChatClient.getInstance().getUsername()+" ");
	}
	public static PublicKey getPublicKey() {
		return publicKey;
	}
	
}
