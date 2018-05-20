import java.util.HashMap;
import java.util.Map;

public class UniqueValueOccurrenceMap {
    private Map<String, Integer> uniqueValueOccurrence;

    public UniqueValueOccurrenceMap(String valueKey) {
        this.uniqueValueOccurrence = new HashMap<String, Integer>();
        uniqueValueOccurrence.put(valueKey, 1);
    }

    public boolean isMyValueKey(String valueKey){
        if (uniqueValueOccurrence.containsKey(valueKey)) {
            uniqueValueOccurrence.put(valueKey, uniqueValueOccurrence.get(valueKey)+1);
            return true;
        }
        return false;
    }
}
