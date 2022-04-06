package crypto.pgp.local;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.util.Date;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.sig.Features;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPGPKeyPair;

import static org.bouncycastle.bcpg.HashAlgorithmTags.*;
import static org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags.*;
import static org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags.AES_256;
import static org.bouncycastle.bcpg.sig.KeyFlags.*;
import static org.bouncycastle.bcpg.sig.KeyFlags.ENCRYPT_STORAGE;

public class KeyGenerator {
    //real stuff
    public static PGPKeyRingGenerator buildKeyRingGenerator(String keyID, char[] keyPassPhraseAr) throws PGPException {
        //initialize the key ring gen
        RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();
        kpg.init(new RSAKeyGenerationParameters(BigInteger.valueOf(0x10001), new SecureRandom(), 2048, 12));

        //sing/encoder init
        PGPKeyPair rsakp_sign = new BcPGPKeyPair(PGPPublicKey.RSA_SIGN, kpg.generateKeyPair(), new Date());
        PGPKeyPair rsakp_enc = new BcPGPKeyPair(PGPPublicKey.RSA_ENCRYPT, kpg.generateKeyPair(), new Date());

        // MORE INITIALIZTIONS
        PGPSignatureSubpacketGenerator signhashgen = new PGPSignatureSubpacketGenerator();
        signhashgen.setKeyFlags(false, SIGN_DATA | CERTIFY_OTHER);
        signhashgen.setPreferredSymmetricAlgorithms(false, new int[]{AES_256, AES_192, AES_128});
        signhashgen.setPreferredHashAlgorithms(false, new int[]{SHA256, SHA1, SHA384, SHA512, SHA224});
        signhashgen.setFeature(false, Features.FEATURE_MODIFICATION_DETECTION);

        //oh the boilerplate...
        PGPSignatureSubpacketGenerator enchashgen = new PGPSignatureSubpacketGenerator();
        enchashgen.setKeyFlags(false, ENCRYPT_COMMS | ENCRYPT_STORAGE);

        //init calculators
        PGPDigestCalculator sha1Calc = new BcPGPDigestCalculatorProvider().get(SHA1);
        PGPDigestCalculator sha256Calc = new BcPGPDigestCalculatorProvider().get(SHA256);

        //start signers
        PGPContentSignerBuilder pcsb = new BcPGPContentSignerBuilder(rsakp_sign.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1);
        PBESecretKeyEncryptor pske = (new BcPBESecretKeyEncryptorBuilder(AES_256, sha256Calc, 0xc0)).build(keyPassPhraseAr);

        //generate keyrings(finally)
        PGPKeyRingGenerator keyRingGen = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, rsakp_sign, keyID, sha1Calc,
                signhashgen.generate(), null, pcsb, pske);

        //return the actual thing ;-;
        //also add the encrypted subkeys
        keyRingGen.addSubKey(rsakp_enc, enchashgen.generate(), null);
        return keyRingGen;
    }

    //mainly taken from @EazyTutorial beacuse i cannot with this...
    public static void writePEMKeys(String path, String keyID, String keyPassPhrase) throws IOException, PGPException {
        //build keyrings
        PGPKeyRingGenerator krg = buildKeyRingGenerator(keyID, keyPassPhrase.toCharArray());

        //generate the public keys
        PGPPublicKeyRing pkr = krg.generatePublicKeyRing();
        try (BufferedOutputStream pubout = new BufferedOutputStream(new FileOutputStream(path + "public.key"))) {
            try (ArmoredOutputStream ao = new ArmoredOutputStream(pubout)) {
                pkr.encode(ao);
            }
        }

        //generate the secret keys
        PGPSecretKeyRing skr = krg.generateSecretKeyRing();
        try (BufferedOutputStream secout = new BufferedOutputStream(new FileOutputStream(path + "private.key"))) {
            try (ArmoredOutputStream ao = new ArmoredOutputStream(secout)) {
                skr.encode(ao);
            }
        }
    }
}