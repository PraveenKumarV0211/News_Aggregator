class MyHeap<T> {
    private MyList<T> heap = new MyList<>();
    private java.util.Comparator<T> comparator;

    public MyHeap(java.util.Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public void add(T value) {
        heap.add(value);
        bubbleUp(heap.size() - 1);
    }

    public T peek() {
        return heap.size() > 0 ? heap.get(0) : null;
    }

    public T poll() {
        if (heap.size() == 0) return null;
        T top = heap.get(0);
        T last = heap.get(heap.size() - 1);
        heap.set(0, last);
        heap.removeLast();
        bubbleDown(0);
        return top;
    }


    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;

            if (comparator.compare(heap.get(index), heap.get(parent)) > 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    private void bubbleDown(int index) {
        int size = heap.size();
        while (index < size) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;

            if (left < size && comparator.compare(heap.get(left), heap.get(largest)) > 0) {
                largest = left;
            }
            if (right < size && comparator.compare(heap.get(right), heap.get(largest)) > 0) {
                largest = right;
            }
            if (largest == index) break;

            swap(index, largest);
            index = largest;
        }
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public MyList<T> toSortedList() {
        MyList<T> result = new MyList<>();
        MyHeap<T> copy = new MyHeap<>(comparator);

        for (int i = 0; i < heap.size(); i++) {
            copy.add(heap.get(i));
        }

        while (copy.peek() != null) {
            result.add(copy.poll());
        }

        return result;
    }
}
