package digicert;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;

import javax.crypto.Cipher;

public class Sender {

	public static void main(String[] args)  throws Exception {
		
		String keystore= "C:\\Temp\\digicert\\keystore_sender.p12";
		String password = "mypassword";
		String digitalSignatureFile= "C:\\Temp\\digicert\\digital_signature_1";
		
		//Message to be sent
		String message = "This is message being sent by the sender, with digital encryption and signing";
		byte[] messageBytes = message.getBytes();

		//Get instance of the PrivateKey for signing the message
		//For self-signed certificate, export it from the Keystore file, as below
		//But when using CA-signed certificate, create a certificate signing request (CSR)
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(new FileInputStream(keystore), password.toCharArray());
		PrivateKey privateKey = (PrivateKey) keyStore.getKey("senderKeyPair", password.toCharArray());
		
		System.out.println("privateKey: "+privateKey);
		
		//Generate a hash of the message
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] messageHash = md.digest(messageBytes);
		
		System.out.println("messageHash: "+messageHash);
		
		//To encrypt a message, we need an algorithm and a private key. 
		//Use the RSA algorithm (DSA algorithm is another option)
		//Create a Cipher instance and initialize it for encryption. 
		//Then call the doFinal() method to encrypt the previously hashed message			
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		byte[] digitalSignature = cipher.doFinal(messageHash);
		
		System.out.println("digitalSignature: "+digitalSignature);
		
		Files.write(Paths.get(digitalSignatureFile), digitalSignature);
		System.out.println("digitalSignature written to file: "+digitalSignatureFile);
		
	}

}
