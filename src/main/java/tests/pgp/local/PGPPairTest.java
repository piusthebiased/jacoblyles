package tests.pgp.local;

import global.FileIO;

import static crypto.pgp.local.KeyGenerator.writePEMKeys;

public class PGPPairTest {
    public static void main(String[] args) throws Exception {
        writePEMKeys("res/", "pius1212", "secret");

        FileIO.printFile("res/public.key");
        FileIO.printFile("res/private.key");
    }
}
