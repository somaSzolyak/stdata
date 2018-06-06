import java.util.Iterator;
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
        KeyHolder kh;
        Iterator<KeyHolder> iterator = keyChain.iterator();
        while (iterator.hasNext()) {
            kh = iterator.next();
            if (kh.getKeyName().equals(key)) {
                iterator.remove();
                break;
            }
        }
    }

    public List<String> getRedundantKeyList() {
        return redundantKeyList;
    }

    public List<String> getStringOnlyKeys() {
        return stringOnlyKeys;
    }

    public void addReducedKeyChain(List<KeyHolder> tmpContainer) {
        if (tmpContainer != null & tmpContainer.size() != 0) keyChain = tmpContainer;
    }
}
