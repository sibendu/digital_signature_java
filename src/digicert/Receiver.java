package digicert;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Arrays;

import javax.crypto.Cipher;

public class Receiver {

	public static void main(String[] args) throws Exception{
		
		String keystore= "C:\\Temp\\digicert\\keystore_receiver.p12";
		String password = "mypassword";
		String digitalSignatureFile= "C:\\Temp\\digicert\\digital_signature_1";
		
		//Message to received from sender (must be same as in sender program)
		String message = "This is message being sent by the sender, with digital encryption and signing";
		byte[] messageBytes = message.getBytes();
				
		//Receiver must have loaded the public key into their Keystore
		//Load public key from keystore for verification
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(new FileInputStream(keystore), password.toCharArray());
		Certificate certificate = keyStore.getCertificate("receiverKeyPair");
		PublicKey publicKey = certificate.getPublicKey();
		
		System.out.println("publicKey: "+publicKey);
		
		//Read digital signature (written to file output by sender program)
		byte[] encryptedMessageHash = Files.readAllBytes(Paths.get(digitalSignatureFile));
		
		//For decryption, need a Cipher instance. Then we call the doFinal method
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] decryptedMessageHash = cipher.doFinal(encryptedMessageHash);
		
		System.out.println("decryptedMessageHash: "+decryptedMessageHash);
		
		//Generate a new message hash from the received message
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] newMessageHash = md.digest(messageBytes);
		
		System.out.println("newMessageHash: "+newMessageHash);
		
		//Check if  generated message hash matches the decrypted one
		boolean isCorrect = Arrays.equals(decryptedMessageHash, newMessageHash);
		
		System.out.println("isCorrect: "+isCorrect);
	}

}
