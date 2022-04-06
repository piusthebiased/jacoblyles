package crypto.pgp.painless;

import global.FileIO;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.pgpainless.PGPainless;

import java.io.IOException;

public class PGPIO {
    public static PGPSecretKeyRing keyFromFile(String path) throws IOException {
        return PGPainless.readKeyRing().secretKeyRing(FileIO.readFile(path));
    }
}
