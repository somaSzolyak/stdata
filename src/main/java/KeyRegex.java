import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyRegex {
    // with the keySearcherRegex I can find any desired key
    private String keySearcherRegex;
    // however I need to be able to strip the unneeded characters
    // startOffset describes where the substring (key) starts counted from the start of the original string in the matched group
    private int startOffset;
    // endOffset describes where the substring (key) ends counted from the end of the original string in the matched group
    private int endOffset;
    private Pattern pattern;
    private Matcher matcher;

    public KeyRegex(String keySearcherRegex, int startOffset, int endOffset) {
        this.keySearcherRegex = keySearcherRegex;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.pattern = Pattern.compile(keySearcherRegex);
    }

    public List<String> getMatchesInString(String string) {
        ArrayList<String> matchList = new ArrayList<String>();
        matcher = pattern.matcher(string);
        while (matcher.find()) {
            matchList.add(string.substring(matcher.start()+startOffset, matcher.end()-endOffset));
        }
        return matchList;
    }

    public void printMatches(String text) {
        pattern = Pattern.compile(keySearcherRegex);
        matcher = pattern.matcher(text);
        // Check all occurrences
        while (matcher.find()) {
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end());
            System.out.println(" Found: " + matcher.group());
        }
    }
}
