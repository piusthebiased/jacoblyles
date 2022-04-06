package crypto.pgp.painless;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.util.io.Streams;
import org.pgpainless.PGPainless;
import org.pgpainless.algorithm.DocumentSignatureType;
import org.pgpainless.decryption_verification.ConsumerOptions;
import org.pgpainless.decryption_verification.DecryptionStream;
import org.pgpainless.encryption_signing.*;
import org.pgpainless.key.protection.SecretKeyRingProtector;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Account {
    private boolean hasLocalKeys;
    private PGPSecretKeyRing user;

    private static final SecretKeyRingProtector keyProtector = SecretKeyRingProtector.unprotectedKeys();

    public Account(){
        this.hasLocalKeys = false;
    }

    public Account(PGPSecretKeyRing user) {
        this.user = user;
    }

    //public void
    public String encryptArmored(String msg, PGPPublicKeyRing pub) throws IOException, PGPException {
        ByteArrayOutputStream ciphertext = new ByteArrayOutputStream();
        EncryptionStream encryptionStream = PGPainless.encryptAndOrSign()
                .onOutputStream(ciphertext)
                .withOptions(
                        ProducerOptions.signAndEncrypt(
                                new EncryptionOptions()
                                        //recipient
                                        .addRecipient(pub),
                                new SigningOptions()
                                        // Sign in-line (using one-pass-signature packet)
                                        .addInlineSignature(SecretKeyRingProtector.unprotectedKeys(), user, DocumentSignatureType.CANONICAL_TEXT_DOCUMENT)
                        ).setAsciiArmor(true) // Ascii armor
                );

        Streams.pipeAll(new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8)), encryptionStream);
        encryptionStream.close();

        return ciphertext.toString();
    }

    public String decrypt(String msg) throws PGPException, IOException {
        ConsumerOptions consumerOptions = new ConsumerOptions()
                .addDecryptionKey(user, keyProtector); // add the decryption key ring

        ByteArrayOutputStream plaintextOut = new ByteArrayOutputStream();
        ByteArrayInputStream ciphertextIn = new ByteArrayInputStream(msg.getBytes(StandardCharsets.UTF_8));

        // The decryption stream is an input stream from which we read the decrypted data
        DecryptionStream decryptionStream = PGPainless.decryptAndOrVerify()
                .onInputStream(ciphertextIn)
                .withOptions(consumerOptions);

        Streams.pipeAll(decryptionStream, plaintextOut);
        decryptionStream.close(); // remember to close the stream!

        System.out.println(plaintextOut);

        return plaintextOut.toString();
    }

    public PGPSecretKeyRing getUser() {
        return user;
    }

    public void setUser(PGPSecretKeyRing user) {
        this.user = user;
    }

    public static void main(String[] args) {
        new Account();
    }
}
