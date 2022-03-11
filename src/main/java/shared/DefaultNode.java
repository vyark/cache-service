package shared;

public class DefaultNode<T> implements LinkedListNode<T> {
    private DoublyLinkedList<T> list;

    public DefaultNode(DoublyLinkedList<T> list) {
        this.list = list;
    }

    @Override
    public boolean hasElement() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public T getElement() throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }

    @Override
    public void detach() {
    }

    @Override
    public DoublyLinkedList<T> getListReference() {
        return list;
    }

    @Override
    public LinkedListNode<T> setPrev(LinkedListNode<T> next) {
        return next;
    }

    @Override
    public LinkedListNode<T> setNext(LinkedListNode<T> prev) {
        return prev;
    }

    @Override
    public LinkedListNode<T> getPrev() {
        return this;
    }

    @Override
    public LinkedListNode<T> getNext() {
        return this;
    }

    @Override
    public LinkedListNode<T> find(T value) {
        return this;
    }
}