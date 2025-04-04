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
        return list;
    }
}