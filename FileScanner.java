import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileScanner {
    public String getFileContents(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String contents = "";
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                contents += line;
            }
            return contents;
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return null;
    }

    public FileScanner(String fileName) {
        getFileContents(fileName);
    }
}
