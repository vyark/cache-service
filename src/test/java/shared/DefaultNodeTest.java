package shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultNodeTest {
    private DoublyLinkedList<CacheElement> list = new DoublyLinkedList<CacheElement>();
    private DefaultNode<CacheElement> defaultNode = new DefaultNode(list);
    private CacheElement cacheElement = new CacheElement("key", "value");

    @Test
    public void testHasElement() {
        assertFalse(defaultNode.hasElement());
    }

    @Test
    public void testWhetherIsEmpty() {
        assertTrue(defaultNode.isEmpty());
    }

    @Test
    public void testGetElement() {
        assertThrows(IllegalArgumentException.class, () -> defaultNode.getElement());
    }

    @Test
    public void getListReference() {
        assertEquals(list, defaultNode.getListReference());
    }

    @Test
    public void testSetPrev() {
        assertEquals(defaultNode, defaultNode.setPrev(defaultNode));
    }

    @Test
    public void testSetNext() {
        assertEquals(defaultNode, defaultNode.setNext(defaultNode));
    }

    @Test
    public void testGetPrev() {
        assertEquals(defaultNode, defaultNode.getPrev());
    }

    @Test
    public void testGetNext() {
        assertEquals(defaultNode, defaultNode.getNext());
    }

    @Test
    public void testFind() {
        assertEquals(defaultNode, defaultNode.find(cacheElement));
    }
}
