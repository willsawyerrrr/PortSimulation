package portsim.movement;

import portsim.ship.Ship;
import portsim.util.BadEncodingException;
import portsim.util.NoSuchShipException;

/**
 * The movement of a ship coming into or out of the port.
 */
public class ShipMovement extends Movement {

    /**
     * The ship entering of leaving the Port
     */
    private Ship ship;

    /**
     * Creates a new ship movement with the given action time and direction to
     * be undertaken with the given ship.
     *
     * @param time      the time the movement should occur
     * @param direction the direction of the movement
     * @param ship      the ship which that is waiting to move
     *
     * @throws IllegalArgumentException if time &lt; 0
     */
    public ShipMovement(long time, MovementDirection direction, Ship ship)
            throws IllegalArgumentException {
        super(time, direction);
        this.ship = ship;
    }

    /**
     * Returns the ship undertaking the movement.
     *
     * @return movements ship
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * Returns the human-readable string representation of this ShipMovement.
     * <p>
     * The format of the string to return is:
     * <pre>DIRECTION ShipMovement to occur at time involving the ship
     * name</pre>
     * Where:
     * <ul>
     *   <li><pre>DIRECTION</pre> is the direction of the movement </li>
     *   <li><pre>time</pre> is the time the movement is meant to occur </li>
     *   <li><pre>name</pre> is the name of the ship that is being moved</li>
     * </ul>
     * <p>
     * For example: <pre> OUTBOUND ShipMovement to occur at 135 involving the
     * ship Voyager</pre>
     *
     * @return string representation of this ShipMovement
     */
    @Override
    public String toString() {
        return String.format("%s involving the ship %s",
            super.toString(),
            this.ship.getName());
    }

    /**
     * Returns the machine-readable string representation of this ship movement.
     * <p>
     * The format of the string to return is:
     * <pre>ShipMovement:time:direction:imoNumber</pre>
     * <p>
     * Where:
     * <ul>
     *     <li><pre>time</pre> is the time that the movement will be
     *     actioned</li>
     *     <li><pre>direction</pre> is the direction of the movement</li>
     *     <li><pre>imoNumber</pre> is the imoNUmber of the ship that is
     *     moving</li>
     * </ul>
     * <p>
     * For example: <pre>ShipMovement:120:INBOUND:1258691</pre>
     *
     * @return encoded string representation of this movement
     */
    @Override
    public String encode() {
        return String.format("%s:%d",
                super.encode(),
                this.ship.getImoNumber());
    }

    /**
     * Creates a ship movement from a string encoding.
     * <p>
     * The format of the string should match the encoded representation of a
     * ship movement, as described in {@link #encode()}.
     * <p>
     * The encoded string is invalid if any of the following conditions are
     * true:
     * <ul>
     *     <li>The number of colons (:) detected was more/fewer than
     *     expected.</li>
     *     <li>The time is not a long (i.e. cannot be parsed by
     *     <pre>Long.parseInt(String)</pre>).</li>
     *     <li>The time is less than zero (0).</li>
     *     <li>The movementDirection is not one of the valid directions (See
     *     {@link MovementDirection}.</li>
     *     <li>The imoNumber is not a long (i.e. cannot be parsed by
     *     <pre>Long.parseLong(String)</pre>).</li>
     *     <li>The imoNumber is less than zero (0).</li>
     *     <li>There is no ship that exists with specified imoNumber.</li>
     * </ul>
     *
     * @param string string containing the encoded ShipMovement
     *
     * @return decoded ShipMovement instance
     *
     * @throws BadEncodingException if the format of the given string is
     *                              invalid according to the rules above
     */
    public static ShipMovement fromString(String string)
            throws BadEncodingException {
        String[] attributes = string.split(":");

        long time;
        long imoNumber;
        MovementDirection direction;
        Ship ship;

        if (!attributes[0].equals("ShipMovement") || attributes.length != 4) {
            throw new BadEncodingException();
        }

        try {
            time = Long.parseLong(attributes[1]);
            if (time < 0) {
                throw new BadEncodingException();
            }

            direction = MovementDirection.valueOf(attributes[2]);

            imoNumber = Long.parseLong(attributes[3]);
            ship = Ship.getShipByImoNumber(imoNumber);
        } catch (IllegalArgumentException | NoSuchShipException ignored) {
            throw new BadEncodingException();
        }

        return new ShipMovement(time, direction, ship);
    }
}
