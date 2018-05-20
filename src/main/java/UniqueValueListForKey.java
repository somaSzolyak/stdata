import java.util.List;

public class UniqueValueListForKey {
    List<UniqueValueOccurrenceMap> valueList;
    String keyName;

    public UniqueValueListForKey(List<UniqueValueOccurrenceMap> valueList, String keyName) {
        this.valueList = valueList;
        this.keyName = keyName;
    }

    public void add(String keyName, String value) {
        if (this.keyName.equals(keyName)) {
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
}
