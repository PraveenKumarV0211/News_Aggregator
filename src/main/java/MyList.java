public class MyList<T> {
    private Object[] data; 
    private int size;      

    // Constructor with unused string array
    public MyList(String[] strings) {
        data = new Object[10];
        size = 0;
    }

    // Default constructor
    public MyList() {
        data = new Object[10];
        size = 0;
    }

    // Remove the last element
    public void removeLast() {
        if (size > 0) {
            data[size - 1] = null;
            size--;
        }
    }

    // Add a new element
    public void add(T value) {
        if (size == data.length) resize();
        data[size++] = value;
    }

    // Get element at index
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return (T) data[index];
    }

    // Set value at index
    public void set(int index, T value) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        data[index] = value;
    }

    // Get number of elements
    public int size() {
        return size;
    }

    // Resize internal array
    private void resize() {
        Object[] newData = new Object[data.length * 2];
        for (int i = 0; i < size; i++) newData[i] = data[i];
        data = newData;
    }
}
