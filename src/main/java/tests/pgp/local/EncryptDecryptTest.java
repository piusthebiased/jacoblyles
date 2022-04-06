package tests.pgp.local;

import crypto.pgp.local.PGPActions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPException;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class EncryptDecryptTest {
    public static void main(String[] args) throws PGPException, IOException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        String passPhrase = "unit tests suck";
        char[] passArray = passPhrase.toCharArray();
        byte[] original = "Hello world".getBytes();

        System.out.println("Starting PGP test");
        byte[] encrypted = PGPActions.encrypt(original, passArray, "x", PGPEncryptedDataGenerator.CAST5, true);
        System.out.println("encrypted data = \n'"+new String(encrypted)+"'");

        byte[] decrypted= PGPActions.decrypt(encrypted, passArray);
        System.out.println("decrypted data = '"+new String(decrypted)+"'");

        encrypted = PGPActions.encrypt(original, passArray, "x", PGPEncryptedDataGenerator.AES_256, false);
        System.out.println("encrypted data = '"+new String(org.bouncycastle.util.encoders.Hex.encode(encrypted))+"'");

        decrypted= PGPActions.decrypt(encrypted, passArray);
        System.out.println("decrypted data = '"+new String(decrypted)+"'");
    }
}
