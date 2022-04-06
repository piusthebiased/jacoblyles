package crypto.pgp.local;

import java.io.*;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.jcajce.*;
import org.bouncycastle.util.io.Streams;

public class PGPActions {
    //verify file
    private static void verifyFile(InputStream in, InputStream keyIn) throws Exception {
        in = PGPUtil.getDecoderStream(in);

        JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(in);
        PGPCompressedData c1 = (PGPCompressedData) pgpFact.nextObject();
        pgpFact = new JcaPGPObjectFactory(c1.getDataStream());

        PGPOnePassSignatureList p1 = (PGPOnePassSignatureList) pgpFact.nextObject();
        PGPOnePassSignature ops = p1.get(0);
        PGPLiteralData p2 = (PGPLiteralData) pgpFact.nextObject();
        InputStream dIn = p2.getInputStream();

        int ch;
        PGPPublicKeyRingCollection pgpRing = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(keyIn), new JcaKeyFingerprintCalculator());

        PGPPublicKey key = pgpRing.getPublicKey(ops.getKeyID());
        FileOutputStream out = new FileOutputStream(p2.getFileName());

        ops.init(new JcaPGPContentVerifierBuilderProvider().setProvider("BC"), key);

        while ((ch = dIn.read()) >= 0) {
            ops.update((byte) ch);
            out.write(ch);
        }

        out.close();

        PGPSignatureList p3 = (PGPSignatureList) pgpFact.nextObject();

        if (ops.verify(p3.get(0))) {
            System.out.println("signature verified.");
        } else {
            System.out.println("signature verification failed.");
        }
    }

    //sign file
    private static void signFile(String fileName, InputStream keyIn, OutputStream out, char[] pass, boolean armor) throws IOException, PGPException {
        if (armor) {
            out = new ArmoredOutputStream(out);
        }

        PGPSecretKey pgpSec = PGPUtils.readSecretKey(keyIn);
        PGPPrivateKey pgpPrivKey = pgpSec.extractPrivateKey(new JcePBESecretKeyDecryptorBuilder().setProvider("BC").build(pass));
        PGPSignatureGenerator sGen = new PGPSignatureGenerator(new JcaPGPContentSignerBuilder(pgpSec.getPublicKey().getAlgorithm(), PGPUtil.SHA1).setProvider("BC"));

        sGen.init(PGPSignature.BINARY_DOCUMENT, pgpPrivKey);

        Iterator it = pgpSec.getPublicKey().getUserIDs();
        if (it.hasNext()) {
            PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();

            spGen.addSignerUserID(false, (String) it.next());
            sGen.setHashedSubpackets(spGen.generate());
        }

        PGPCompressedDataGenerator cGen = new PGPCompressedDataGenerator(PGPCompressedData.ZLIB);

        BCPGOutputStream bOut = new BCPGOutputStream(cGen.open(out));

        sGen.generateOnePassVersion(false).encode(bOut);

        File file = new File(fileName);
        PGPLiteralDataGenerator lGen = new PGPLiteralDataGenerator();
        OutputStream lOut = lGen.open(bOut, PGPLiteralData.BINARY, file);
        FileInputStream fIn = new FileInputStream(file);
        int ch;

        while ((ch = fIn.read()) >= 0) {
            lOut.write(ch);
            sGen.update((byte) ch);
        }

        lGen.close();
        sGen.generate().encode(bOut);
        cGen.close();

        if (armor) {
            out.close();
        }
    }

    //omg compression
    private static byte[] compress(byte[] clearData, String fileName, int algorithm) throws IOException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(algorithm);
        OutputStream cos = comData.open(bOut); // open it with the final destination

        PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();

        // we want to generate compressed data. This might be a user option later,
        // in which case we would pass in bOut.
        OutputStream pOut = lData.open(cos, // the compressed output stream
                PGPLiteralData.BINARY, fileName,  // "filename" to store
                clearData.length, // length of clear data
                new Date()  // current time
        );

        pOut.write(clearData);
        pOut.close();

        comData.close();

        return bOut.toByteArray();
    }

    //encrypt data
    public static byte[] encrypt(byte[] clearData, char[] passPhrase, String fileName, int algorithm, boolean armor) throws IOException, PGPException, NoSuchProviderException {
        if (fileName == null) {
            fileName = PGPLiteralData.CONSOLE;
        }

        byte[] compressedData = compress(clearData, fileName, CompressionAlgorithmTags.ZIP);

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        OutputStream out = bOut;
        if (armor) {
            out = new ArmoredOutputStream(out);
        }

        PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(new JcePGPDataEncryptorBuilder(algorithm).setSecureRandom(new SecureRandom()).setProvider("BC"));
        encGen.addMethod(new JcePBEKeyEncryptionMethodGenerator(passPhrase).setProvider("BC"));
        OutputStream encOut = encGen.open(out, compressedData.length);

        encOut.write(compressedData);
        encOut.close();

        if (armor) {
            out.close();
        }

        return bOut.toByteArray();
    }

    //decrypt data
    public static byte[] decrypt(byte[] encrypted, char[] passPhrase) throws IOException, PGPException, NoSuchProviderException {
        InputStream in = new ByteArrayInputStream(encrypted);
        in = PGPUtil.getDecoderStream(in);

        JcaPGPObjectFactory pgpF = new JcaPGPObjectFactory(in);
        PGPEncryptedDataList enc;
        Object o = pgpF.nextObject();

        //
        // the first object might be a PGP marker packet.
        //
        if (o instanceof PGPEncryptedDataList) {
            enc = (PGPEncryptedDataList) o;
        } else {
            enc = (PGPEncryptedDataList) pgpF.nextObject();
        }

        PGPPBEEncryptedData pbe = (PGPPBEEncryptedData) enc.get(0);
        InputStream clear = pbe.getDataStream(new JcePBEDataDecryptorFactoryBuilder(new JcaPGPDigestCalculatorProviderBuilder().setProvider("BC").build()).setProvider("BC").build(passPhrase));
        JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(clear);
        PGPCompressedData cData = (PGPCompressedData) pgpFact.nextObject();

        pgpFact = new JcaPGPObjectFactory(cData.getDataStream());
        PGPLiteralData ld = (PGPLiteralData) pgpFact.nextObject();

        return Streams.readAll(ld.getInputStream());
    }
}
