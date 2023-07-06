package dev.nebulamc.inject.internal.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultimapTest {

    Multimap<String, String> map;

    @BeforeEach
    void setUp() {

        map = new Multimap<>();
    }

    @DisplayName("<init>(Multimap<K, V>)")
    @Nested
    class Init {

        @Test
        void testInitWhenMapIsNull() {

            assertThrows(NullPointerException.class, () -> new Multimap<>(null));
        }

        @Test
        void testInitWhenMapIsNotNull() {

            final List<String> values = new ArrayList<>(List.of("value 1", "value 2"));
            final Map<String, List<String>> map = Map.of("key", values);

            final Multimap<String, String> multimap = new Multimap<>(map);

            assertEquals(Map.of("key", List.of("value 1", "value 2")), multimap);
            values.add("value 3");
            assertEquals(Map.of("key", List.of("value 1", "value 2")), multimap);
        }
    }

    @DisplayName("add(K, V")
    @Nested
    class Add {

        @Test
        void testAddWhenKeyNotPresent() {

            map.add("key", "value");

            assertEquals(List.of("value"), map.get("key"));
        }

        @Test
        void testAddWhenKeyIsNull() {

            map.add(null, "value");

            assertEquals(List.of("value"), map.get(null));
        }

        @Test
        void testAddWhenValueIsNull() {

            map.add("key", null);

            assertEquals(Collections.singletonList(null), map.get("key"));
        }

        @Test
        void testAddWhenKeyPresent() {

            map.add("key", "value 1");
            map.add("key", "value 2");

            assertEquals(List.of("value 1", "value 2"), map.get("key"));
        }
    }

    @DisplayName("size()")
    @Nested
    class Size {

        @Test
        void testSizeWhenFirstInitialized() {

            final int size = map.size();

            assertEquals(0, size);
        }

        @Test
        void testSizeWhenEntryAdded() {

            map.add("key", "value");

            final int size = map.size();

            assertEquals(1, size);
        }

        @Test
        void testSizeWhenMultipleEntriesWithSameKeyAdded() {

            map.add("key", "value 1");
            map.add("key", "value 2");

            final int size = map.size();

            assertEquals(1, size);
        }

        @Test
        void testSizeWhenMultipleEntriesAdded() {

            map.add("key 1", "value 1");
            map.add("key 2", "value 2");

            final int size = map.size();

            assertEquals(2, size);
        }

        @Test
        void testSizeWhenEntryRemoved() {

            map.add("key", "value");
            map.remove("key");

            final int size = map.size();

            assertEquals(0, size);
        }
    }

    @DisplayName("isEmpty()")
    @Nested
    class IsEmpty {

        @Test
        void testIsEmptyWhenFirstInitialized() {

            final boolean isEmpty = map.isEmpty();

            assertTrue(isEmpty);
        }

        @Test
        void testIsEmptyWhenItemsAdded() {

            map.add("key", "value");

            final boolean isEmpty = map.isEmpty();

            assertFalse(isEmpty);
        }
    }

    @DisplayName("containsKey(Object)")
    @Nested
    class ContainsKey {

        @Test
        void testContainsKeyWhenKeyNotPresent() {

            final boolean containsKey = map.containsKey("key");

            assertFalse(containsKey);
        }

        @Test
        void testContainsKeyWhenKeyPresent() {

            map.add("key", "value");

            final boolean containsKey = map.containsKey("key");

            assertTrue(containsKey);
        }
    }

    @DisplayName("containsValue(Object)")
    @Nested
    class ContainsValue {

        @Test
        void testContainsValueWhenValueNotPresent() {

            final boolean containsValue = map.containsValue(List.of("value"));

            assertFalse(containsValue);
        }

        @Test
        void testContainsValueWhenValuePresent() {

            map.add("key", "value");

            final boolean containsValue = map.containsValue(List.of("value"));

            assertTrue(containsValue);
        }
    }

    @DisplayName("get(Object)")
    @Nested
    class Get {

        @Test
        void testGetWhenKeyNotPresent() {

            final List<String> values = map.get("key");

            assertNull(values);
        }

        @Test
        void testGetWhenKeyPresent() {

            map.add("key", "value");

            final List<String> values = map.get("key");

            assertEquals(List.of("value"), values);
        }
    }

    @DisplayName("put(K, List<V>)")
    @Nested
    class Put {

        @Test
        void testPutWhenKeyNotPresent() {

            map.put("key", List.of("value"));

            assertEquals(List.of("value"), map.get("key"));
        }

        @Test
        void testPutWhenKeyPresent() {

            map.add("key", "value");

            map.put("key", List.of("value 1", "value 2"));

            assertEquals(List.of("value 1", "value 2"), map.get("key"));
        }
    }

    @DisplayName("remove(Object)")
    @Nested
    class Remove {

        @Test
        void testRemoveWhenKeyNotPresent() {

            final List<String> values = map.remove("key");

            assertNull(values);
        }

        @Test
        void testRemoveWhenKeyPresent() {

            map.add("key", "value");

            final List<String> values = map.remove("key");

            assertNull(map.get("key"));
            assertEquals(List.of("value"), values);
        }
    }

    @DisplayName("putAll(Map<? extends K, ? extends List<V>>)")
    @Nested
    class PutAll {

        @Test
        void testPutAllWhenMapEmpty() {

            map.putAll(Map.of());

            assertTrue(map.isEmpty());
        }

        @Test
        void testPutAllWhenMapNotEmpty() {

            map.add("key 1", "value 1"); // should be overridden by putAll

            map.putAll(Map.of("key 1", List.of("value 1"), "key 2", List.of("value 2")));

            assertEquals(List.of("value 1"), map.get("key 1"));
            assertEquals(List.of("value 2"), map.get("key 2"));
        }
    }

    @DisplayName("clear()")
    @Nested
    class Clear {

        @SuppressWarnings("ConstantConditions")
        @Test
        void testClearWhenMapEmpty() {

            map.clear();

            assertTrue(map.isEmpty());
        }

        @SuppressWarnings("ConstantConditions")
        @Test
        void testClearWhenMapNotEmpty() {

            map.add("key", "value");

            map.clear();

            assertTrue(map.isEmpty());
        }
    }

    @DisplayName("keySet()")
    @Nested
    class KeySet {

        @Test
        void testKeySetWhenMapEmpty() {

            final Set<String> keySet = map.keySet();

            assertEquals(Set.of(), keySet);
        }

        @Test
        void testKeySetWhenMapNotEmpty() {

            map.add("key 1", "value 1");
            map.add("key 2", "value 2");

            final Set<String> keySet = map.keySet();

            assertEquals(Set.of("key 1", "key 2"), keySet);
        }
    }

    @DisplayName("values()")
    @Nested
    class Values {

        @Test
        void testValuesWhenMapEmpty() {

            final Collection<List<String>> values = map.values();

            assertTrue(values.isEmpty());
        }

        @Test
        void testValuesWhenMapNotEmpty() {

            map.add("key 1", "value 1");
            map.add("key 2", "value 2");

            final Collection<List<String>> values = map.values();

            assertEquals(Set.of(List.of("value 1"), List.of("value 2")), new HashSet<>(values));
            // Use set to ignore order of values
        }
    }

    @DisplayName("entrySet()")
    @Nested
    class EntrySet {

        @Test
        void testEntrySetWhenMapEmpty() {

            final Set<Map.Entry<String, List<String>>> entrySet = map.entrySet();

            assertEquals(Set.of(), entrySet);
        }

        @Test
        void testEntrySetWhenMapNotEmpty() {

            map.add("key 1", "value 1");
            map.add("key 2", "value 2");

            final Set<Map.Entry<String, List<String>>> entrySet = map.entrySet();

            assertEquals(
                    Set.of(
                            Map.entry("key 1", List.of("value 1")),
                            Map.entry("key 2", List.of("value 2"))
                    ), entrySet);
        }
    }
}
