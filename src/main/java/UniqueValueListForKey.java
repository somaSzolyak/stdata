import java.util.List;

public class UniqueValueListForKey {
    private UniqueValueOccurrenceMap valueMap;
    private String keyName;

    public UniqueValueListForKey(String keyName) {
        this.valueMap = null;
        this.keyName = keyName;
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
        if (!valueMap.isMyValueKey(value)) {
            this.valueMap.add(value);
        } else {
            valueMap.increaseOccurrence(value);
        }
    }
}
