package portsim.port;

import portsim.ship.ContainerShip;
import portsim.ship.NauticalFlag;
import portsim.ship.Ship;
import portsim.util.BadEncodingException;
import portsim.util.Encodable;
import portsim.util.NoSuchShipException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Queue of ships waiting to enter a Quay at the port. Ships are chosen based on
 * their priority.
 */
public class ShipQueue implements Encodable {
    /**
     * The ships waiting to dock at the port
     */
    private List<Ship> queue;

    /**
     * Constructs a new ShipQueue with an initially empty queue of ships.
     */
    public ShipQueue() {
        queue = new ArrayList<>();
    }

    /**
     * Gets the next ship to enter the port and removes it from the queue.
     * <p>
     * The same rules as described in {@link #peek()} should be used for
     * determining which ship to remove and return.
     *
     * @return next ship to dock
     */
    public Ship poll() {
        Ship ship = peek();
        queue.remove(ship);
        return ship;
    }

    /**
     * Returns the next ship waiting to enter the port. The queue should not
     * change.
     * <p>
     * The rules for determining which ship in the queue should be returned
     * next are as follows:
     * <ol>
     *     <li>If a ship carrying dangerous cargo, it should be returned. If
     *     more than one ship is carrying dangerous cargo return the one added
     *     to the queue first.</li>
     *     <li>If a ship requires medical assistance, it should be returned. If
     *     more than one ship requires medical assistance, return the one added
     *     to the queue first.</li>
     *     <li>If a ship is ready to be docked, it should be returned. If more
     *     than one ship is ready to be docked, return the one added to the
     *     queue first.</li>
     *     <li>If there is a container ship in the queue, return the one added
     *     to the queue first.</li>
     *     <li>If this point is reached and no ship has been returned, return
     *     the ship that was added to the queue first.</li>
     *     <li>If there are no ships in the queue, return null.</li>
     * </ol>
     *
     * @return next ship in queue
     */
    public Ship peek() {
        if (queue.isEmpty()) {
            return null;
        }
        for (Ship ship : queue) {
            if (ship.getFlag() == NauticalFlag.BRAVO) {
                return ship;
            }
        }
        for (Ship ship : queue) {
            if (ship.getFlag() == NauticalFlag.WHISKEY) {
                return ship;
            }
        }
        for (Ship ship : queue) {
            if (ship.getFlag() == NauticalFlag.HOTEL) {
                return ship;
            }
        }
        for (Ship ship : queue) {
            if (ship instanceof ContainerShip) {
                return ship;
            }
        }
        return queue.get(0);
    }

    /**
     * Adds the specified ship to the queue.
     *
     * @param ship to be added to queue
     */
    public void add(Ship ship) {
        queue.add(ship);
    }

    /**
     * Returns a list containing all the ships currently stored in this
     * ShipQueue.
     * <p>
     * The order of ships in the returned list should be the order in which the
     * ships were added to the queue.
     * <p>
     * Adding or removing elements from the returned list should not affect the
     * original list.
     *
     * @return ships in queue
     */
    public List<Ship> getShipQueue() {
        return new ArrayList<>(queue);
    }

    /**
     * Returns true if and only if this queue is equal to the other given ship
     * queue.
     * <p>
     * For two ship queue to be equal, they must have the same ships in the
     * queue, in the same order.
     *
     * @param o other object to check equality
     *
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ShipQueue)) {
            return false;
        }
        ShipQueue other = (ShipQueue) o;
        return queue.equals(other.getShipQueue());
    }

    /**
     * Returns the hash code of this ship queue.
     * <p>
     * Two ship queues that are equal according to {@link #equals(Object)}
     * method should have the same hash code.
     *
     * @return hash code of this ship queue.
     */
    @Override
    public int hashCode() {
        int code = 1;
        for (Ship ship : queue) {
            code = code * ship.hashCode();
        }
        return code;
    }

    /**
     * Returns the machine-readable string representation of this ShipQueue.
     * <p>
     * The format of the string to return is:
     * <pre>ShipQueue:numShipsInQueue:shipID,shipID,...</pre>
     * <p>
     * Where:
     * <ul>
     *     <li>numShipsInQueue is the number of ships in the ship queue in the
     *     port</li>
     *     <li>if present (numShipsInQueue &gt; 0): shipIDs is each ship's ID
     *     in the aforementioned queue</li>
     * </ul>
     *
     * For example: <pre>ShipQueue:0:</pre> OR
     * <pre>ShipQueue:2:3456789,1234567</pre>
     *
     * @return encoded string representation of this ShipQueue
     */
    @Override
    public String encode() {
        StringJoiner joiner = new StringJoiner(",");
        for (Ship ship : getShipQueue()) {
            joiner.add(String.valueOf(ship.getImoNumber()));
        }
        return String.format("ShipQueue:%d:%s",
                getShipQueue().size(),
                joiner);
    }

    /**
     * Creates a ship queue from a string encoding.
     * <p>
     * The format of the string should match the encoded representation of a
     * ship queue, as described in {@link #encode()}.
     * <p>
     * The encoded string is invalid if any of the following conditions are
     * true:
     * <ul>
     *     <li>The number of colons (:) detected was more/fewer than
     *     expected.</li>
     *     <li>The string does not start with the literal string
     *     "<pre>ShipQueue</pre>".</li>
     *     <li>The number of ships in the shipQueue is not an integer (i.e.
     *     cannot be parsed by <pre>Integer.parseInt(String)</pre>).</li>
     *     <li>The imoNumber of the ships in the shipQueue are not valid longs.
     *     (i.e. cannot be parsed by <pre>Long.parseLong(String)</pre>).</li>
     *     <li>Any imoNumber read does not correspond to a valid ship in the
     *     simulation.</li>
     * </ul>
     *
     * @param string string containing the encoded ShipQueue
     *
     * @return decoded ship queue instance
     *
     * @throws BadEncodingException if the format of the given string is
     *                              invalid according to the rules above
     */
    public static ShipQueue fromString(String string)
            throws BadEncodingException {
        int numShips;
        ShipQueue queue = new ShipQueue();

        String[] attributes = string.split(":");

        if (attributes.length < 2
                || attributes.length > 3
                || !attributes[0].equals("ShipQueue")
                || string.endsWith(",")
                || (string.endsWith(":") && attributes.length == 3)) {
            throw new BadEncodingException();
        }

        try {
            numShips = Integer.parseInt(attributes[1]);
        } catch (NumberFormatException ignored) {
            throw new BadEncodingException();
        }

        if (numShips != 0) {
            String[] rawImoNums = attributes[2].split(",");
            if (numShips != rawImoNums.length) {
                throw new BadEncodingException();
            }
            Long[] imoNums = new Long[rawImoNums.length];
            for (int i = 0; i < rawImoNums.length; i++) {
                try {
                    imoNums[i] = Long.parseLong(rawImoNums[i]);
                } catch (NumberFormatException ignored) {
                    throw new BadEncodingException();
                }
            }

            for (Long imoNum : imoNums) {
                try {
                    queue.add(Ship.getShipByImoNumber(imoNum));
                } catch (NoSuchShipException ignored) {
                    throw new BadEncodingException();
                }
            }
        }

        return queue;
    }
}
