import java.util.List;

public class KeyContainer {
    private List<KeyHolder> keyChain;
    private List<String> redundantKeyList;
    // keys that are only validly associated with string values are held in this list
    private List<String> stringOnlyKeys;

    public KeyContainer(List<KeyHolder> keyChain, List<String> redundantKeyList, List<String> stringOnlyKeys) {
        this.keyChain = keyChain;
        this.redundantKeyList = redundantKeyList;
        this.stringOnlyKeys = stringOnlyKeys;
    }

    private void add(String key){
        if (isKeyKnown(key) != null) isKeyKnown(key).increaseOccurrence();
        else {
            KeyHolder newKey = new KeyHolder(key, stringOnlyKeys.contains(key));
            keyChain.add(newKey);
        }
    }

    public void add(List<String> keyList) {
        for (String key : keyList) {
            if (redundantKeyList.contains(key)) {
                continue;
            }
            this.add(key);
        }
    }

    public void add(KeyHolder keyHolder) {
        keyChain.add(keyHolder);
    }

    private KeyHolder isKeyKnown(String key) {
        for (KeyHolder keyHolder : keyChain) {
            if (keyHolder.getKeyName().equals(key)) return keyHolder;
        }
        return null;
    }

    public List<KeyHolder> getData() {
        return keyChain;
    }

    public void remove(String key) {
        keyChain.remove(key);
    }

    public List<String> getRedundantKeyList() {
        return redundantKeyList;
    }

    public List<String> getStringOnlyKeys() {
        return stringOnlyKeys;
    }
}
