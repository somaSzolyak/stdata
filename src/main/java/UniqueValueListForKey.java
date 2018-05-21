import java.util.ArrayList;
import java.util.List;

public class UniqueValueListForKey {
    private List<UniqueValueOccurrenceMap> valueList;
    private String keyName;

    public UniqueValueListForKey(String keyName) {
        this.valueList = null;
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void add(String keyName, List<String> valueList) {
        if (!this.keyName.equals(keyName)) {
            throw new IllegalArgumentException("You are trying to add a value to the wrong key");
        }
        if (this.valueList == null) {
            this.valueList = new ArrayList<UniqueValueOccurrenceMap>();
        }
        for (String value : valueList) {
            add(value);
        }
    }

    private void add(String value) {
        for (UniqueValueOccurrenceMap valueMap : valueList) {
            if (!valueMap.isMyValueKey(value)) {
                valueList.add(new UniqueValueOccurrenceMap(value));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UniqueValueListForKey{\n" + "\tkeyName :'" + keyName + '\'' + '\n');
        for (UniqueValueOccurrenceMap valueOccurrenceMap : valueList) {
            stringBuilder.append(valueOccurrenceMap.toString());
        }
        stringBuilder.append("}");
         return stringBuilder.toString();
    }
}
