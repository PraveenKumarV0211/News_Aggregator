public class MyList<T> {
    private Object[] data;
    private int size;

    public MyList(String[] strings) {
        data = new Object[10];
        size = 0;
    }
    public MyList() {
        data = new Object[10];
        size = 0;
    }

    public void removeLast() {
        if (size > 0) {
            data[size - 1] = null;
            size--;
        }
    }



    public void add(T value) {
        if (size == data.length) resize();
        data[size++] = value;
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return (T) data[index];
    }
    public void set(int index, T value) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        data[index] = value;
    }

    public int size() {
        return size;
    }

    private void resize() {
        Object[] newData = new Object[data.length * 2];
        for (int i = 0; i < size; i++) newData[i] = data[i];
        data = newData;
    }
}
