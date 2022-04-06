package matrix.callbacks;

import org.bouncycastle.openpgp.PGPException;

import java.io.IOException;

public interface DataCallback {
    void onData(Object data) throws IOException, PGPException;
}
