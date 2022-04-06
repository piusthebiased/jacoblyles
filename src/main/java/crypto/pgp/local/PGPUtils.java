package crypto.pgp.local;

import java.io.*;
import java.security.NoSuchProviderException;
import java.util.Iterator;

import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;

//standard pgp utility
/*
basically standard utility stuff, half of this was taken from the maven source because i need to learn
 */

public class PGPUtils {
    //ong compression
    public static byte[] compressFile(String fileName, int algorithm) throws IOException {
        //init
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PGPCompressedDataGenerator data = new PGPCompressedDataGenerator(algorithm);

        //quite literally
        //conv data to bin -> compress -> stuff into array
        org.bouncycastle.openpgp.PGPUtil.writeFileToLiteralData(data.open(out), PGPLiteralData.BINARY, new File(fileName));
        data.close();

        //i fly out of method
        return out.toByteArray();
    }

    /*
    this thing basically searches through a keyring, and sees if a keyid is in it
    if it is in it, return the key, if not, return null
     */
    public static PGPPrivateKey findPrivateKey(PGPSecretKeyRingCollection pgpSec, long keyID, char[] pass) throws PGPException, NoSuchProviderException {
        PGPSecretKey secretKey = pgpSec.getSecretKey(keyID);

        if (secretKey == null) {
            return null;
        }

        return secretKey.extractPrivateKey(new JcePBESecretKeyDecryptorBuilder().setProvider("BC").build(pass));
    }

    //read pub key from file
    public static PGPPublicKey readPublicKey(String fileName) throws IOException, PGPException {
        InputStream keyIn = new BufferedInputStream(new FileInputStream(fileName));
        PGPPublicKey pubKey = readPublicKey(keyIn);
        keyIn.close();
        return pubKey;
    }

    /*
     routine that basically opens keyring collection and loads first available key
     that can be used to encrypt messages
     */
    public static PGPPublicKey readPublicKey(InputStream input) throws IOException, PGPException {
        PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(org.bouncycastle.openpgp.PGPUtil.getDecoderStream(input), new JcaKeyFingerprintCalculator());

        Iterator keyRings = pgpPub.getKeyRings();
        while (keyRings.hasNext()) {
            PGPPublicKeyRing keyRing = (PGPPublicKeyRing) keyRings.next();

            Iterator keyIter = keyRing.getPublicKeys();
            while (keyIter.hasNext()) {
                PGPPublicKey key = (PGPPublicKey) keyIter.next();

                if (key.isEncryptionKey()) {
                    return key;
                }
            }
        }

        throw new IllegalArgumentException("Can't find encryption key in key ring.");
    }

    /*
    pretty self explanatory
    read secret key from file; i dislike java error handling
     */
    public static PGPSecretKey readSecretKey(String fileName) throws IOException, PGPException {
        //this is a buffer method because sometimes im lazy
        InputStream keyIn = new BufferedInputStream(new FileInputStream(fileName));
        PGPSecretKey secKey = readSecretKey(keyIn);

        keyIn.close();
        return secKey;
    }

    /*
     routine that basically opens keyring collection and loads first available key
     that can be used to generate a sig
     */
    public static PGPSecretKey readSecretKey(InputStream input) throws IOException, PGPException {
        //init the keyring
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(org.bouncycastle.openpgp.PGPUtil.getDecoderStream(input), new JcaKeyFingerprintCalculator());

        //iterate thru the ring
        Iterator keyRings = pgpSec.getKeyRings();
        while (keyRings.hasNext()) {
            PGPSecretKeyRing keyRing = (PGPSecretKeyRing) keyRings.next();

            Iterator keyIter = keyRing.getSecretKeys();
            while (keyIter.hasNext()) {
                PGPSecretKey key = (PGPSecretKey) keyIter.next();

                if (key.isSigningKey()) {
                    return key;
                }
            }
        }

        //if all else fails, throw an exception
        throw new IllegalArgumentException("Can't find signing key in key ring.");
    }
}