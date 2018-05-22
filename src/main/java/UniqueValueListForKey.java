import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UniqueValueListForKey {
    private UniqueValueOccurrenceMap valueMap;
    private String keyName;
    private List<String> stringOnlyKeys;

    public UniqueValueListForKey(String keyName, List<String> stringOnlyKeys) {
        this.valueMap = null;
        this.keyName = keyName;
        this.stringOnlyKeys = stringOnlyKeys;
    }

    public String getKeyName() {
        return keyName;
    }

    public UniqueValueOccurrenceMap getValueMap() {
        return valueMap;
    }

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
        if (stringOnlyKeys.contains(keyName)) {
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
}
