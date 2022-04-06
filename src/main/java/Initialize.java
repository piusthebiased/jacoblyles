import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Initialize {
    public static void check() throws IOException {
        FileUtils.forceMkdir(new File("res/painlessKeys/address"));
        FileUtils.forceMkdir(new File("res/xmr/"));
    }
}
