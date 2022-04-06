package global;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIO {
    public static String makeFile(String path) throws IOException {
        File file = new File(path);
        boolean result;
        try {
            result = file.createNewFile();  //creates a new file
            if (!result) {
                String[] x = path.split("\\.");
                makeFile(x[0] + ((int) (Math.random() * 2048)) + "." + x[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }

    public static void writeFile(String path, String message) throws IOException {
        Files.write(Paths.get(path), message.getBytes(StandardCharsets.UTF_8));
    }

    public static void printFile(String path) {
        Path filePath = Paths.get(path);

        //converting to UTF 8
        Charset charset = StandardCharsets.UTF_8;

        //try with resource
        try (BufferedReader bufferedReader = Files.newBufferedReader(filePath, charset)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String path) {
        Path filePath = Paths.get(path);

        //converting to UTF 8
        Charset charset = StandardCharsets.UTF_8;

        String ret = "";

        //try with resource
        try (BufferedReader bufferedReader = Files.newBufferedReader(filePath, charset)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
               ret += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    //unit test
    public static void main(String[] args) throws IOException {
        makeFile(Root.keys + "test.txt");
        writeFile(Root.keys + "test.txt", "this is a message");
    }
}
