package shared;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class DoublyLinkedListTest {
    private DoublyLinkedList<CacheElement> doublyLinkedList = new DoublyLinkedList();
    private CacheElement cacheElement = new CacheElement("key", "value");
    private LinkedListNode next = new DefaultNode<>(doublyLinkedList);
    private final Node<CacheElement> node = new Node<CacheElement>(cacheElement, next, doublyLinkedList);

    @Test
    public void testCleanUp() {
        doublyLinkedList.add(cacheElement);

        assertEquals(1, doublyLinkedList.size());

        doublyLinkedList.clear();

        assertTrue(doublyLinkedList.isEmpty());
    }

    @Test
    public void testIfContains() {
        doublyLinkedList.add(cacheElement);

        assertTrue(doublyLinkedList.contains(cacheElement));
    }

    @Test
    public void testSearch() {
        doublyLinkedList.add(cacheElement);

        assertEquals(cacheElement, doublyLinkedList.search(cacheElement).getElement());
    }

    @Test
    public void testAdd() {
        doublyLinkedList.add(cacheElement);
        doublyLinkedList.add(new CacheElement("key1", "value1"));

        assertEquals(2, doublyLinkedList.size());
    }

    @Test
    public void testAddAll() {
        doublyLinkedList.addAll(Arrays.asList(cacheElement, new CacheElement("key1", "value1")));

        assertEquals(2, doublyLinkedList.size());
    }

    @Test
    public void testRemove() {
        doublyLinkedList.add(cacheElement);

        assertEquals(1, doublyLinkedList.size());

        doublyLinkedList.remove(cacheElement);

        assertTrue(doublyLinkedList.isEmpty());
    }

    @Test
    public void testRemoveTail() {
        doublyLinkedList.add(cacheElement);

        assertEquals(1, doublyLinkedList.size());

        doublyLinkedList.removeTail();

        assertTrue(doublyLinkedList.isEmpty());
    }

    @Test
    public void testMoveToFront() {
        DefaultNode<CacheElement> defaultNode = new DefaultNode<>(doublyLinkedList);

        assertInstanceOf(DefaultNode.class, doublyLinkedList.moveToFront(defaultNode));
    }

    @Test
    public void testUpdateAndMoveToFront() {
        LinkedListNode next = new DefaultNode<>(doublyLinkedList);
        Node<CacheElement> node = new Node<CacheElement>(cacheElement, next, doublyLinkedList);

        assertEquals(node.getElement(), doublyLinkedList.updateAndMoveToFront(node, cacheElement).getElement());
    }
}
