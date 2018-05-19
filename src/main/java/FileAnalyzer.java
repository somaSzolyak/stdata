import java.io.IOException;

public class FileAnalyzer {
    private KeyContainer keyContainer;
    private KeyRegex keyRegex;

    public FileAnalyzer(KeyContainer keyContainer, KeyRegex keyRegex) {
        this.keyContainer = keyContainer;
        this.keyRegex = keyRegex;
    }

    private void getKeysInLine(String line){
        keyContainer.add(keyRegex.getMatchesInString(line));
    }

    public void getKeysInFile(TextFileReader textFileReader) throws IOException {
        int lineCount = 0;
        String line;
        while ((line= textFileReader.ReadNextLine()) != null) {
            getKeysInLine(line);
            lineCount++;
        }
        System.out.println(keyContainer.getKeysInJSON());
        System.out.println(lineCount);
    }

}
