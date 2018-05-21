import java.util.List;
import java.util.Map;

public class KeyContainer {
    private Map<String, Integer> keysInJSON;
    private List<String> redundantKeyList;

    public KeyContainer(Map<String, Integer> keysInJSON, List<String> redundantKeyList) {
        this.keysInJSON = keysInJSON;
        this.redundantKeyList = redundantKeyList;
    }

    private void add(String key){
        int count = keysInJSON.containsKey(key) ? keysInJSON.get(key) : 0;
        keysInJSON.put(key, count + 1);
    }

    public void add(List<String> keyList) {
        for (String key : keyList) {
            if (redundantKeyList.contains(key)) {
                continue;
            }
            this.add(key);
        }
    }

    public Map<String, Integer> getData() {
        return keysInJSON;
    }

    public void remove(String key) {
        keysInJSON.remove(key);
    }

    public List<String> getRedundantKeyList() {
        return redundantKeyList;
    }
}
