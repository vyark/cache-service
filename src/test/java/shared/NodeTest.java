package shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {
    private DoublyLinkedList<CacheElement> list = new DoublyLinkedList<CacheElement>();
    private CacheElement cacheElement = new CacheElement("key", "value");
    private LinkedListNode next = new DefaultNode<>(list);
    private Node<CacheElement> node = new Node<CacheElement>(cacheElement, next, list);

    @Test
    public void testHasElement() {
        assertTrue(node.hasElement());
    }

    @Test
    public void testWhetherIsEmpty() {
        assertFalse(node.isEmpty());
    }

    @Test
    public void testGetElement() {
        assertEquals(cacheElement, node.getElement());
    }

    @Test
    public void getListReference() {
        assertEquals(list, node.getListReference());
    }

    @Test
    public void testSetPrev() {
        assertEquals(node, node.setPrev(node));
    }

    @Test
    public void testSetNext() {
        assertEquals(node, node.setNext(node));
    }

    @Test
    public void testGetPrev() {
        assertEquals(next, node.getPrev());
    }

    @Test
    public void testGetNext() {
        assertEquals(next, node.getNext());
    }

    @Test
    public void testFind() {
        assertEquals(node, node.find(cacheElement));
    }
}
