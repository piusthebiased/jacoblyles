package tests.pgp.painless;

import global.FileIO;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.pgpainless.PGPainless;

import java.io.IOException;

public class ReadPGP {
    public static void main(String[] args) throws IOException {
        String test = FileIO.readFile("res/painlessKeys/private.asc");
        System.out.println(test);

        PGPSecretKeyRing sec = PGPainless.readKeyRing()
                .secretKeyRing(test);

        System.out.println(PGPainless.asciiArmor(PGPainless.extractCertificate(sec)));
    }
}
