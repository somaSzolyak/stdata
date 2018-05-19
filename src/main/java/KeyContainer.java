import java.util.List;
import java.util.Map;

public class KeyContainer {
    private Map<String, Integer> keysInJSON;

    public KeyContainer(Map<String, Integer> keysInJSON) {
        this.keysInJSON = keysInJSON;
    }

    //todo key addition should increase the value by 1 or add a new key and set value to 1
    private void add(String key){
        int count = keysInJSON.containsKey(key) ? keysInJSON.get(key) : 0;
        keysInJSON.put(key, count + 1);
    }

    public void add(List<String> keyList) {
        for (String key : keyList) {
            this.add(key);
        }
    }

    public Map<String, Integer> getKeysInJSON() {
        return keysInJSON;
    }
}
