import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyHolder {
    private UniqueValueOccurrenceMap valueMap;
    private String keyName;
    private int keyOccurrenceCounter;
    // true if this key's values should only be strings that contains no numbers
    private boolean isStringOnlyKey;

    public KeyHolder(String keyName, boolean stringOnlyKey) {
        this.valueMap = null;
        this.keyName = keyName;
        this.isStringOnlyKey = stringOnlyKey;
        this.keyOccurrenceCounter = 1;
    }

    public String getKeyName() {
        return keyName;
    }

    public UniqueValueOccurrenceMap getValueMap() {
        return valueMap;
    }

    public int getKeyOccurrenceCounter() { return keyOccurrenceCounter; }

    public void add(String keyName, List<String> valueList) {
        if (!this.keyName.equals(keyName)) {
            throw new IllegalArgumentException("You are trying to add a value to the wrong key");
        }
        if (this.valueMap == null & valueList.size() > 0) {
            this.valueMap = new UniqueValueOccurrenceMap(valueList.get(0));
        }
        for (String value : valueList) {
            add(value);
        }
    }

    private void add(String value) {
        // must hardcode key selection
        if (isStringOnlyKey) {
            Pattern pattern = Pattern.compile("[\\d]+");
            Matcher matcher = pattern.matcher(value);
            if (!matcher.find()) {
                addNewOrIncrementValue(value);
            }
        } else {
            addNewOrIncrementValue(value);
        }
    }

    private void addNewOrIncrementValue(String value) {
        if (!valueMap.isMyValueKey(value)) {
            valueMap.add(value);
        } else {
            valueMap.increaseOccurrence(value);
        }
    }

    public void increaseOccurrence() {
        this.keyOccurrenceCounter += 1;
    }
}
