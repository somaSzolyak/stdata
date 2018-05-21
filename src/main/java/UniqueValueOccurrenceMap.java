import java.util.HashMap;
import java.util.Map;

public class UniqueValueOccurrenceMap {
    private Map<String, Integer> uniqueValueOccurrence;

    public UniqueValueOccurrenceMap(String valueKey) {
        this.uniqueValueOccurrence = new HashMap<String, Integer>();
        add(valueKey);
    }

    // TODO: 2018.05.21. remove shout
    public boolean isMyValueKey(String valueKey){
        return uniqueValueOccurrence.containsKey(valueKey);
    }

    public void increaseOccurrence(String valueKey) {
        uniqueValueOccurrence.put(valueKey, uniqueValueOccurrence.get(valueKey)+1);
    }

    public void add(String valueKey) {
        uniqueValueOccurrence.put(valueKey, 1);
    }
}
