package org.mmga.utils.utils.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author wzp
 * @version 1.0
 * @since 2023/5/31 23:19:31
 */
public class IncreaseAbleHashMap<K,V> extends HashMap<K, List<V>> implements IncreaseAbleMap<K,V> {
    @Override
    public void add(K key, V value) {
        List<V> oldList = super.getOrDefault(value, new ArrayList<>());
        oldList.add(value);
        super.put(key, oldList);
    }
    @Override
    public boolean delete(K key, V value){
        List<V> oldList = super.getOrDefault(value, new ArrayList<>());
        if (!oldList.remove(value)) {
            return false;
        }
        super.put(key, oldList);
        return true;
    }

    @Override
    public void forEach(K key, Consumer<? super V> action) {
        super.getOrDefault(key, new ArrayList<>()).forEach(action);
    }
}
