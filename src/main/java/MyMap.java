class MyMap<K, V> {
    // Internal class to represent key-value pairs
    private class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int SIZE = 100; 
    private Entry<K, V>[] buckets; 

    // Constructor
    public MyMap() {
        buckets = new Entry[SIZE];
    }

    // Get all values in a specific bucket index
    public MyList<V> getBucket(int index) {
        MyList<V> values = new MyList<>();
        Entry<K, V> node = buckets[index];
        while (node != null) {
            values.add(node.value);
            node = node.next;
        }
        return values;
    }

    // Get all keys in a specific bucket index
    public MyList<K> getKeysInBucket(int index) {
        MyList<K> keys = new MyList<>();
        Entry<K, V> node = buckets[index];
        while (node != null) {
            keys.add(node.key);
            node = node.next;
        }
        return keys;
    }

    // Hash the key to get the index
    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % SIZE;
    }

    // Get number of buckets
    public int bucketCount() {
        return buckets.length;
    }

    // Add or update a key-value pair
    public void put(K key, V value) {
        int index = getIndex(key);
        Entry<K, V> node = buckets[index];

        // If bucket is empty, insert directly
        if (node == null) {
            buckets[index] = new Entry<>(key, value);
            return;
        }

        // Traverse the list to update or add at end
        while (node != null) {
            if (node.key.equals(key)) {
                node.value = value; 
                return;
            }
            if (node.next == null) break;
            node = node.next;
        }

        // Add new entry at the end
        node.next = new Entry<>(key, value);
    }

    // Get the value for a given key
    public V get(K key) {
        int index = getIndex(key);
        Entry<K, V> node = buckets[index];
        while (node != null) {
            if (node.key.equals(key)) return node.value;
            node = node.next;
        }
        return null;
    }

    // Check if key exists in the map
    public boolean containsKey(K key) {
        return get(key) != null;
    }
}
