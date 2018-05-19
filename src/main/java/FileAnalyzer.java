public class FileAnalyzer {
    private KeyContainer keyContainer;
    private KeyRegex keyRegex;

    public FileAnalyzer(KeyContainer keyContainer, KeyRegex keyRegex) {
        this.keyContainer = keyContainer;
        this.keyRegex = keyRegex;
    }

    //todo adds keys to KeysInJson or increases the values associated with it
    public void getKeysInLine(String line){
        System.out.println(line);
        keyContainer.add(keyRegex.getMatchesInString(line));
    }

    private void getKeysInFile(){}

}
