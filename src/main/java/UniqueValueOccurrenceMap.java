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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key :
                uniqueValueOccurrence.keySet()) {
            stringBuilder.append("\t\t'" + key + "\' : " + uniqueValueOccurrence.get(key) + "\n");
        }
        return stringBuilder.toString();
    }
}
