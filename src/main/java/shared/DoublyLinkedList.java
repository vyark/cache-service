package shared;

import com.google.common.base.Preconditions;
import com.google.common.cache.RemovalListener;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DoublyLinkedList<T> {

    private DefaultNode<T> defaultNode;
    private LinkedListNode<T> head;
    private LinkedListNode<T> tail;
    private AtomicInteger size;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private RemovalListener removalListener;

    public DoublyLinkedList() {
        this.defaultNode = new DefaultNode<T>(this);
        clear();
    }

    public DoublyLinkedList<T> removalListener(RemovalListener listener) {
        Preconditions.checkState(this.removalListener == null);
        this.removalListener = (RemovalListener) Preconditions.checkNotNull(listener);
        return this;
    }

    public void clear() {
        head = defaultNode;
        tail = defaultNode;
        size = new AtomicInteger(0);
    }

    public int size() {
        return size.get();
    }

    public boolean isEmpty() {
        return head.isEmpty();
    }

    public boolean contains(T value) {
        this.lock.readLock().lock();
        try {
            return search(value).hasElement();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public LinkedListNode<T> search(T value) {
        this.lock.readLock().lock();
        try {
            return head.find(value);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public LinkedListNode<T> add(T value) {
        this.lock.writeLock().lock();
        try {
            head = new Node<T>(value, head, this);
            if (tail.isEmpty()) {
                tail = head;
            }
            size.incrementAndGet();
            return head;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public boolean addAll(Collection<T> values) {
        this.lock.writeLock().lock();
        try {
            for (T value : values) {
                if (add(value).isEmpty()) {
                    return false;
                }
            }
            return true;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public LinkedListNode<T> remove(T value) {
        this.lock.writeLock().lock();
        try {
            LinkedListNode<T> linkedListNode = head.find(value);
            if (!linkedListNode.isEmpty()) {
                if (linkedListNode == tail) {
                    tail = tail.getPrev();
                }
                if (linkedListNode == head) {
                    head = head.getNext();
                }
                linkedListNode.detach();
                size.decrementAndGet();
            }
            return linkedListNode;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public LinkedListNode<T> removeTail() {
        this.lock.writeLock().lock();
        try {
            LinkedListNode<T> oldTail = tail;
            if (oldTail == head) {
                tail = head = defaultNode;
            } else {
                tail = tail.getPrev();
                oldTail.detach();
            }
            if (!oldTail.isEmpty()) {
                size.decrementAndGet();
            }
            return oldTail;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public LinkedListNode<T> moveToFront(LinkedListNode<T> node) {
        return node.isEmpty() ? defaultNode : updateAndMoveToFront(node, node.getElement());
    }

    public LinkedListNode<T> updateAndMoveToFront(LinkedListNode<T> node, T newValue) {
        this.lock.writeLock().lock();
        try {
            if (node.isEmpty() || (this != (node.getListReference()))) {
                return defaultNode;
            }
            detach(node);
            add(newValue);
            return head;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    private void detach(LinkedListNode<T> node) {
        if (node != tail) {
            node.detach();
            if (node == head) {
                head = head.getNext();
            }
            size.decrementAndGet();
        } else {
            removeTail();
        }
    }
}