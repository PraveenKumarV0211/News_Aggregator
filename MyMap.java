class MyMap<K, V> {
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

    public MyMap() {
        buckets = new Entry[SIZE];
    }
    public MyList<V> getBucket(int index) {
        MyList<V> values = new MyList<>();
        Entry<K, V> node = buckets[index];
        while (node != null) {
            values.add(node.value);
            node = node.next;
        }
        return values;
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % SIZE;
    }
    public int bucketCount() {
        return buckets.length;
    }


    public void put(K key, V value) {
        int index = getIndex(key);
        Entry<K, V> node = buckets[index];

        if (node == null) {
            buckets[index] = new Entry<>(key, value);
            return;
        }

        while (node != null) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
            if (node.next == null) break;
            node = node.next;
        }
        node.next = new Entry<>(key, value);
    }

    public V get(K key) {
        int index = getIndex(key);
        Entry<K, V> node = buckets[index];
        while (node != null) {
            if (node.key.equals(key)) return node.value;
            node = node.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }
}
