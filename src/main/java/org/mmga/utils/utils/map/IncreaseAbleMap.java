package org.mmga.utils.utils.map;

import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author wzp
 * @version 1.0
 * @since 2023/5/31 23:17:44
 */
public interface IncreaseAbleMap<K,V> {
    void add(K key, V value);
    boolean delete(K key, V value);
    void forEach(K key, Consumer<? super V> action);
}
