package lru;

import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.MapMaker;
import shared.Cache;
import shared.CacheElement;
import shared.DoublyLinkedList;
import shared.LinkedListNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * LRU (Least Recently Used) — discards items that have not been used for a long time
 * (more than other elements in the cache).
 */
public class LRUCache<K, V> implements Cache<K, V> {
    private int size;
    private ConcurrentMap<K, LinkedListNode<CacheElement<K, V>>> linkedListNodeMap;
    private DoublyLinkedList<CacheElement<K, V>> doublyLinkedList;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int evictionNumber;
    private long start;
    private long end;
    private List<Long> times = new ArrayList();
    private RemovalListener removalListener;

    public LRUCache(int size, RemovalListener<Object, Object> removalListener) {
        this.size = size;
        this.linkedListNodeMap = new MapMaker().concurrencyLevel(4).initialCapacity(size).makeMap();
        this.doublyLinkedList = new DoublyLinkedList<>();
        this.removalListener = removalListener;
    }

    @Override
    public boolean put(K key, V value) {
        this.lock.writeLock().lock();
        start = System.currentTimeMillis();
        try {
            CacheElement<K, V> item = new CacheElement<K, V>(key, value);
            LinkedListNode<CacheElement<K, V>> newNode;
            if (this.linkedListNodeMap.containsKey(key)) {
                LinkedListNode<CacheElement<K, V>> node = this.linkedListNodeMap.get(key);
                newNode = doublyLinkedList.updateAndMoveToFront(node, item);
            } else {
                if (this.size() >= this.size) {
                    this.evictElement();
                }
                newNode = this.doublyLinkedList.add(item);
            }
            if (newNode.isEmpty()) {
                end = System.currentTimeMillis();
                times.add(end - start);
                return false;
            }
            this.linkedListNodeMap.put(key, newNode);
            times.add(end - start);
            end = System.currentTimeMillis();
            return true;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<V> get(K key) {
        this.lock.readLock().lock();
        try {
            LinkedListNode<CacheElement<K, V>> linkedListNode = this.linkedListNodeMap.get(key);
            if (linkedListNode != null && !linkedListNode.isEmpty()) {
                linkedListNodeMap.put(key, this.doublyLinkedList.moveToFront(linkedListNode));
                return Optional.of(linkedListNode.getElement().getValue());
            }
            return Optional.empty();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public int size() {
        return doublyLinkedList.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        this.lock.writeLock().lock();
        try {
            linkedListNodeMap.clear();
            doublyLinkedList.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }


    private boolean evictElement() {
        this.lock.writeLock().lock();
        evictionNumber++;
        try {
            LinkedListNode<CacheElement<K, V>> linkedListNode = doublyLinkedList.removeTail();
            if (linkedListNode.isEmpty()) {
                return false;
            }
            System.out.println("Value removed - " + linkedListNode);
            linkedListNodeMap.remove(linkedListNode.getElement().getKey());
            removalListener.onRemoval(RemovalNotification.create(linkedListNode.getElement().getKey(), linkedListNode.getElement().getValue(), RemovalCause.REPLACED));
            return true;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public void displayCache() {
        linkedListNodeMap.values().stream()
                .forEach(cacheElementLinkedListNode -> System.out.println(cacheElementLinkedListNode.getElement()));
    }

    @Override
    public void displayStatistics() {
        System.out.println("eviction count - " + evictionNumber);
        System.out.println("average time - " + getAverageTime());
    }

    private Long getAverageTime() {
        Long total = 0L;
        for (Long l : times)
            total += l;
        return (total / times.size());
    }
}