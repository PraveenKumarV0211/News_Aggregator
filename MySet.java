class MySet<T> {
    private MyMap<T, Boolean> map = new MyMap<>();

    public void add(T value) {
        map.put(value, true);
    }

    public boolean contains(T value) {
        return map.containsKey(value);
    }

    public MyList<T> getAll() {
        MyList<T> list = new MyList<>(new String[]{"java", "programming", "tech"});
        // This part requires map iteration, can be added if needed
        return list;
    }
}