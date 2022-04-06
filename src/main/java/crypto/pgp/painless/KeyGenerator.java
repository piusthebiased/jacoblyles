package crypto.pgp.painless;

import global.FileIO;
import global.Root;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.pgpainless.PGPainless;
import org.pgpainless.key.generation.type.rsa.RsaLength;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class KeyGenerator {
    public static PGPSecretKeyRing genSimpleRSA(String userID, boolean exportToFile, boolean overwrite) throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        PGPSecretKeyRing secretKey = PGPainless.generateKeyRing()
                .simpleRsaKeyRing(userID, RsaLength._4096);

        if(exportToFile){
            // Extract certificate (public key) from secret key
            PGPPublicKeyRing certificate = PGPainless.extractCertificate(secretKey);

            String p1 = Root.keys + "public.asc";
            String p2 = Root.keys + "private.asc";
            if(!overwrite) {
                p1 = FileIO.makeFile(p1);
                p2 = FileIO.makeFile(p2);
            }
            FileIO.writeFile(p1, PGPainless.asciiArmor(certificate));
            FileIO.writeFile(p2, PGPainless.asciiArmor(secretKey));
        }

        return secretKey;
    }

    private static int rand(){
        return (short)(Math.random() * 65535);
    }

    public static void main(String[] args) throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        genSimpleRSA("not test", true, false);
    }
}
