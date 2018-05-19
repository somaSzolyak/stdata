public class FileAnalyzer {
    private KeyContainer keyContainer;
    private KeyRegex keyRegex;

    public FileAnalyzer(KeyContainer keyContainer, KeyRegex keyRegex) {
        this.keyContainer = keyContainer;
        this.keyRegex = keyRegex;
    }

    public void getKeysInLine(String line){
        keyContainer.add(keyRegex.getMatchesInString(line));
    }

    private void getKeysInFile(){}

}
