package fruitshop.comm;

import fruitshop.inv.domain.Inventory;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class KeyedItemRepo implements Repo<KeyedItem> {
    private static volatile KeyedItemRepo instance;

    public static KeyedItemRepo getInstance() {
        if (instance == null) {
            synchronized (Inventory.class) {
                if (instance == null) {
                    instance = new KeyedItemRepo();
                }
            }
        }
        return instance;
    }

    private KeyedItemRepo() {
    }

    private Map<String, List<KeyedItem>> repo = new HashMap<>();

    @Override
    public KeyedItem find(String namespace, String key) {
        return repo.get(namespace + "-" + key).get(0);
    }

    @Override
    public List<KeyedItem> findList(String namespace, String key) {
        return repo.get(namespace + "-" + key);
    }

    @Override
    public void save(String namespace, KeyedItem item) {
        String key = item.getKey();
        repo.putIfAbsent(namespace + "-" + key, new ArrayList<>());
        repo.get(namespace + "-" + key).add(item);
    }

    @Override
    public void delete() {
        repo.clear();
    }
}
