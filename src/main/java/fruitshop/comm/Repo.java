package fruitshop.comm;

import java.util.List;

public interface Repo<T> {
    void save(String namespace, T t);

    T find(String namespace, String key);

    List<T> findList(String namespace, String key);

    void delete();
}
