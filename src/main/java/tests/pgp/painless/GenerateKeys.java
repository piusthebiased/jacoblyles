package tests.pgp.painless;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.pgpainless.PGPainless;
import org.pgpainless.algorithm.CompressionAlgorithm;
import org.pgpainless.algorithm.KeyFlag;
import org.pgpainless.key.generation.KeySpec;
import org.pgpainless.key.generation.type.ecc.EllipticCurve;
import org.pgpainless.key.generation.type.ecc.ecdh.ECDH;
import org.pgpainless.key.generation.type.ecc.ecdsa.ECDSA;
import org.pgpainless.key.generation.type.rsa.RSA;
import org.pgpainless.key.generation.type.rsa.RsaLength;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

public class GenerateKeys {
    public static void main(String[] args) throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        int flag = 0;

        if(flag == 0){
            // RSA key without additional subkeys
            PGPSecretKeyRing secretKeys = PGPainless.generateKeyRing()
                    .simpleRsaKeyRing("yes <whydoyouwantmyemail@gmail.com>", RsaLength._4096);
        } else if (flag == 1){
            // EdDSA primary key with EdDSA signing- and XDH encryption subkeys
            PGPSecretKeyRing secretKeys = PGPainless.generateKeyRing()
                    .modernKeyRing("no <thisismyemail@gmail.com>", "thisisapassword");
        } else {
            // Customized key
            PGPSecretKeyRing keyRing = PGPainless.buildKeyRing()
                    .setPrimaryKey(KeySpec.getBuilder(
                            RSA.withLength(RsaLength._8192),
                            KeyFlag.SIGN_DATA, KeyFlag.CERTIFY_OTHER))
                    .addSubkey(
                            KeySpec.getBuilder(ECDSA.fromCurve(EllipticCurve._P256), KeyFlag.SIGN_DATA)
                    ).addSubkey(
                            KeySpec.getBuilder(
                                    ECDH.fromCurve(EllipticCurve._P256),
                                    KeyFlag.ENCRYPT_COMMS, KeyFlag.ENCRYPT_STORAGE)
                    ).addUserId("userids")
                    .addUserId("imagine")
                    .build();
        }
    }
}
