import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValueRegex {
    // within this String has to be a '~' that's going t be replaced with the actual key
    private String valueSearcherRegex;
    private String key = null;
    private int startOffset;
    private int endOffset;
    private Pattern pattern;
    private Matcher matcher;

    public ValueRegex(String valueSearcherRegex, int endOffset) {
        if (valueSearcherRegex.contains("~")) {
            this.valueSearcherRegex = valueSearcherRegex;
            this.endOffset = endOffset;
        } else {
            throw new IllegalArgumentException("valueSearcherRegex has to contain '~'");
        }
    }

    public void setKey(String key) {
        this.key = key;
        //todo to change this in the future -> breaks my heart but it is the easiest way to hardcode this +4 in here
        startOffset = key.length() + 4;
    }

    public List<String> getMatchesInString(String string) {
        if (key == null) {
            throw new NullPointerException("Key is not yet bound!");
        }
        ArrayList<String> matchList = new ArrayList<String>();
        bindPattern();
        matcher = pattern.matcher(string);
        while (matcher.find()) {
            matchList.add(string.substring(matcher.start() + startOffset, matcher.end() - endOffset));
        }
        freePattern();
        return matchList;

    }

    private void bindPattern() {
        valueSearcherRegex = valueSearcherRegex.replaceAll("~", key);
        pattern = Pattern.compile(valueSearcherRegex);
    }

    private void freePattern() {
        valueSearcherRegex = valueSearcherRegex.replaceAll(key, "~");
        pattern = null;
    }

    public String getKey() {
        return key;
    }
}
