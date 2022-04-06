package matrix.callbacks;


import matrix.bot.LoginData;
import org.bouncycastle.openpgp.PGPException;

import java.io.IOException;

public interface LoginCallback {
    void onResponse(LoginData data) throws IOException, PGPException;
}
