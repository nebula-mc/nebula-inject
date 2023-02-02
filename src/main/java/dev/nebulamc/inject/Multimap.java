package dev.nebulamc.inject;

import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An internal interface for a multi-map.
 *
 * @author Sparky983
 * @param <K> the key type
 * @param <V> the values type
 */
@NullMarked
final class Multimap<K extends @Nullable Object, V extends @Nullable Object>
        implements Map<K, List<V>> {

    private final Map<K, List<V>> map;

    Multimap() {

        this.map = new HashMap<>();
    }

    Multimap(final Map<K, List<V>> multimap) {

        Preconditions.requireNonNull(multimap, "multimap");

        this.map = new HashMap<>(multimap);

        map.entrySet().forEach((entry) -> entry.setValue(new LinkedList<>(entry.getValue())));
    }

    public void add(final K key, final V value) {

        map.computeIfAbsent(key, (k) -> new LinkedList<>()).add(value);
    }

    @Override
    public int size() {

        return map.size();
    }

    @Override
    public boolean isEmpty() {

        return map.isEmpty();
    }

    @Override
    public boolean containsKey(final @Nullable Object key) {

        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final @Nullable Object value) {

        return map.containsValue(value);
    }

    @Override
    public List<V> get(final @Nullable Object key) {

        return map.get(key);
    }

    @Override
    public List<V> put(final @Nullable K key, @Nullable final List<V> value) {

        return map.put(key, value);
    }

    @Override
    public List<V> remove(final @Nullable Object key) {

        return map.remove(key);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends List<V>> m) {

        map.putAll(m);
    }

    @Override
    public void clear() {

        map.clear();
    }

    @Override
    public Set<K> keySet() {

        return map.keySet();
    }

    @Override
    public Collection<List<V>> values() {

        return map.values();
    }

    @Override
    public Set<Entry<K, List<V>>> entrySet() {

        return map.entrySet();
    }
}
