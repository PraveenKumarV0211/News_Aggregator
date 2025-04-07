class MySet<T> {
    private MyMap<T, Boolean> map = new MyMap<>();

    public void add(T value) {
        map.put(value, true);
    }

    public boolean contains(T value) {
        return map.containsKey(value);
    }

    public MyList<T> getAll() {
        MyList<T> list = new MyList<>();
        for (int i = 0; i < map.bucketCount(); i++) {
            MyList<Boolean> bucketValues = map.getBucket(i);
            MyList<T> bucketKeys = map.getKeysInBucket(i); // You need this helper function
            for (int j = 0; j < bucketKeys.size(); j++) {
                list.add(bucketKeys.get(j));
            }
        }
        return list;
    }
}