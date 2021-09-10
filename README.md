1. Generate Keypair:

keytool -genkeypair -alias senderKeyPair -keyalg RSA -keysize 2048 -dname "CN=Sibendu" -validity 365 -storetype PKCS12 -keystore keystore_sender.p12 -storepass mypassword


2. Publish public key

For self-signed certificate, export it from the Keystore file: 
keytool -exportcert -alias senderKeyPair -storetype PKCS12 -keystore keystore_sender.p12 -file sender_certificate.cer -rfc -storepass mypassword

But when using CA-signed certificate, create a certificate signing request (CSR): 

keytool -certreq -alias senderKeyPair -storetype PKCS12 -keystore keystore_sender.p12 -file -rfc -storepass mypassword > sender_certificate.csr


3. Sender.java -> Follow documentation


4. On their side, receiver can load it into their Keystore:

keytool -importcert -alias receiverKeyPair -storetype PKCS12 -keystore keystore_receiver.p12 -file sender_certificate.cer -rfc -storepass mypassword


5. Receiver.java -> Follow documentation


Source reference: https://www.baeldung.com/java-digital-signature
