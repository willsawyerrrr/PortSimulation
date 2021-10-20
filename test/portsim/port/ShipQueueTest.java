package portsim.port;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import portsim.ship.BulkCarrier;
import portsim.ship.ContainerShip;
import portsim.ship.NauticalFlag;
import portsim.ship.Ship;
import portsim.util.BadEncodingException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShipQueueTest {

    ShipQueue queue1;
    ShipQueue queue2;
    BulkCarrier carrier1;
    BulkCarrier carrier2;
    ContainerShip containerShip1;
    ContainerShip containerShip2;

    @Before
    public void setUp() {
        queue1 = new ShipQueue();
        queue2 = new ShipQueue();
        carrier1 = new BulkCarrier(
                1234567,
                "Alpha",
                "Australia",
                NauticalFlag.HOTEL,
                85
        );
        carrier2 = new BulkCarrier(
                4567890,
                "Bravo",
                "China",
                NauticalFlag.NOVEMBER,
                120
        );
        containerShip1 = new ContainerShip(
                9876543,
                "Charlie",
                "England",
                NauticalFlag.WHISKEY,
                8
        );
        containerShip2 = new ContainerShip(
                7654321,
                "Delta",
                "New Zealand",
                NauticalFlag.NOVEMBER,
                15
        );
    }

    @After
    public void tearDown() {
        queue1 = null;
        queue2 = null;
        Ship.resetShipRegistry();
    }

    @Test
    public void basicConstructorTest() {
        assertEquals(new ArrayList<>(), queue1.getShipQueue());
    }

    @Test
    public void addShipTest() {
        queue1.add(carrier1);
        assertEquals(1, queue1.getShipQueue().size());
        queue1.add(containerShip2);
        assertEquals(2, queue1.getShipQueue().size());
    }

    @Test
    public void peekEmptyQueueTest() {
        assertNull(queue1.peek());
    }

    @Test
    public void pollEmptyQueueTest() {
        assertNull(queue1.poll());
    }

    @Test
    public void peekSingleShipTest() {
        queue1.add(carrier1);
        assertEquals(carrier1, queue1.peek());
    }

    @Test
    public void pollSingleShipTest() {
        queue1.add(carrier1);
        assertEquals(carrier1, queue1.poll());
    }

    @Test
    public void pollMultipleTest() {
        queue1.add(carrier1);
        queue1.add(carrier2);
        queue1.add(containerShip1);
        queue1.add(containerShip2);
        assertEquals(containerShip1, queue1.poll());
        assertEquals(carrier1, queue1.poll());
        assertEquals(containerShip2, queue1.poll());
        assertEquals(carrier2, queue1.poll());

        // Adding in different order ensures the order the
        // ships are added does not affect the polled ships
        queue1.add(carrier1);
        queue1.add(containerShip2);
        queue1.add(carrier2);
        queue1.add(containerShip1);
        assertEquals(containerShip1, queue1.poll());
        assertEquals(carrier1, queue1.poll());
        assertEquals(containerShip2, queue1.poll());
        assertEquals(carrier2, queue1.poll());
    }

    @Test
    public void peekEqualsPollTest() {
        queue1.add(carrier1);
        queue1.add(containerShip2);
        queue1.add(carrier2);
        queue1.add(containerShip1);
        assertEquals(queue1.peek(), queue1.poll());
        assertEquals(queue1.peek(), queue1.poll());
        assertEquals(queue1.peek(), queue1.poll());
        assertEquals(queue1.peek(), queue1.poll());
    }

    @Test
    public void getShipQueueEmptyTest() {
        List<Ship> expected = new ArrayList<>();
        assertEquals(expected, queue1.getShipQueue());
    }

    @Test
    public void getShipQueueSingleTest() {
        List<Ship> expected = new ArrayList<>();
        queue1.add(containerShip2);
        expected.add(containerShip2);
        assertEquals(expected, queue1.getShipQueue());
    }

    @Test
    public void getShipQueueMultipleTest() {
        List<Ship> expected = new ArrayList<>();
        queue1.add(containerShip2);
        expected.add(containerShip2);
        queue1.add(carrier1);
        expected.add(carrier1);
        queue1.add(carrier2);
        expected.add(carrier2);
        queue1.add(containerShip1);
        expected.add(containerShip1);
        assertEquals(expected, queue1.getShipQueue());
    }

    @Test
    public void notAShipQueueNotEqualsTest() {
        Object o = new Object();
        assertNotEquals(queue1, o);
    }

    @Test
    public void differentShipsNotEqualsTest() {
        queue1.add(carrier1);
        queue1.add(carrier2);
        queue2.add(containerShip1);
        queue2.add(containerShip2);
        assertNotEquals(queue1, queue2);
    }

    @Test
    public void thisOneExtraNotEqualsTest() {
        queue1.add(carrier1);
        queue1.add(carrier2);
        queue1.add(containerShip2);
        queue2.add(carrier1);
        queue2.add(carrier2);
        assertNotEquals(queue1, queue2);
    }

    @Test
    public void otherOneExtraNotEqualsTest() {
        queue1.add(carrier1);
        queue1.add(carrier2);
        queue2.add(carrier1);
        queue2.add(carrier2);
        queue2.add(containerShip2);
        assertNotEquals(queue1, queue2);
    }

    @Test
    public void equalsTest() {
        queue1.add(carrier1);
        queue1.add(carrier2);
        queue2.add(carrier1);
        queue2.add(carrier2);
        assertEquals(queue1, queue2);
    }

    @Test
    public void sameShipsDifferentOrderEqualsTest() {
        queue1.add(carrier1);
        queue1.add(carrier2);
        queue2.add(carrier2);
        queue2.add(carrier1);
        assertNotEquals(queue1, queue2);
    }

    @Test
    public void emptyHashCodeTest() {
        assertEquals(queue1.hashCode(), queue2.hashCode());
    }

    @Test
    public void oneShipHashCodeTest() {
        queue1.add(carrier1);
        queue2.add(carrier1);
        assertEquals(queue1.hashCode(), queue2.hashCode());
    }

    @Test
    public void multipleShipHashCodeTest() {
        queue1.add(carrier1);
        queue1.add(containerShip2);
        queue2.add(carrier1);
        queue2.add(containerShip2);
        assertEquals(queue1.hashCode(), queue2.hashCode());
    }

    @Test
    public void emptyEncodeTest() {
        assertEquals("ShipQueue:0:", queue1.encode());
    }

    @Test
    public void singleShipEncodeTest() {
        queue1.add(carrier2);
        assertEquals("ShipQueue:1:4567890", queue1.encode());
    }

    @Test
    public void multipleShipsEncodeTest() {
        queue1.add(carrier2);
        queue1.add(containerShip1);
        queue1.add(carrier1);
        assertEquals("ShipQueue:3:4567890,9876543,1234567",
                queue1.encode());
    }

    @Test
    public void emptyValidFromStringTest() {
        ShipQueue queue = null;
        String encoding = "ShipQueue:0:";
        try {
            queue = ShipQueue.fromString(encoding);
        } catch (BadEncodingException ignored) {
            fail("This should be a valid encoding");
        }
        assertEquals(queue1, queue);
    }

    @Test
    public void singleShipValidFromStringTest() {
        ShipQueue queue = null;
        String encoding = "ShipQueue:1:4567890";

        queue1.add(carrier2);
        try {
            queue = ShipQueue.fromString(encoding);
        } catch (BadEncodingException ignored) {
            fail("This should be a valid encoding");
        }
        assertEquals(queue1, queue);
    }

    @Test
    public void multipleShipsValidFromStringTest() {
        ShipQueue queue = null;
        String encoding = "ShipQueue:3:4567890,9876543,1234567";

        queue1.add(carrier2);
        queue1.add(containerShip1);
        queue1.add(carrier1);
        try {
            queue = ShipQueue.fromString(encoding);
        } catch (BadEncodingException ignored) {
            fail("This should be a valid encoding");
        }
        assertEquals(queue1, queue);
    }

    @Test(expected = BadEncodingException.class)
    public void tooManyColonsInvalidFromStringTest()
            throws BadEncodingException {
        String encoding = "ShipQueue:2:1234567:7654321";
        ShipQueue.fromString(encoding);
    }

    @Test(expected = BadEncodingException.class)
    public void doesNotStartWithShipQueueInvalidFromStringTest()
            throws BadEncodingException {
        String encoding = "Queue:2:1234567,7654321";
        ShipQueue.fromString(encoding);
    }

    @Test(expected = BadEncodingException.class)
    public void numShipsNotAnIntegerInvalidFromStringTest()
            throws BadEncodingException {
        String encoding = "ShipQueue:two:1234567,7654321";
        ShipQueue.fromString(encoding);
    }

    @Test(expected = BadEncodingException.class)
    public void numShipsNotEqualNumberOfImoNumbersInvalidFromStringTest()
            throws BadEncodingException {
        String encoding = "ShipQueue:3:1234567,7654321";
        ShipQueue.fromString(encoding);
    }

    @Test(expected = BadEncodingException.class)
    public void singleShipDoesNotExistInvalidFromStringTest()
            throws BadEncodingException {
        String encoding = "ShipQueue:1:1863592";
        ShipQueue.fromString(encoding);
    }

    @Test(expected = BadEncodingException.class)
    public void multipleShipsOneDoesNotExistInvalidFromStringTest()
            throws BadEncodingException {
        String encoding = "ShipQueue:3:1234567,1863592,9876543";
        ShipQueue.fromString(encoding);
    }
}