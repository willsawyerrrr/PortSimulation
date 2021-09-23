package portsim.movement;

import portsim.cargo.Cargo;
import portsim.util.BadEncodingException;


import java.util.ArrayList;
import java.util.List;

/**
 * The movement of cargo coming into or out of the port.
 *
 * @ass1_partial
 */
public class CargoMovement extends Movement {

    /**
     * The cargo that will be involved in the movement
     */
    private List<Cargo> cargo;

    /**
     * Creates a new cargo movement with the given action time and direction
     * to be undertaken with the given cargo.
     *
     * @param time      the time the movement should occur
     * @param direction the direction of the movement
     * @param cargo     the cargo to be moved
     * @throws IllegalArgumentException if time &lt; 0
     * @ass1
     */
    public CargoMovement(long time, MovementDirection direction,
                         List<Cargo> cargo) throws IllegalArgumentException {
        super(time, direction);
        this.cargo = cargo;
    }

    /**
     * Returns the cargo that will be moved.
     * <p>
     * Adding or removing elements from the returned list should not affect the original list.
     *
     * @return all cargo in the movement
     * @ass1
     */
    public List<Cargo> getCargo() {
        return new ArrayList<>(cargo);
    }

    /**
     * Returns the human-readable string representation of this CargoMovement.
     * <p>
     * The format of the string to return is
     * <pre>
     * DIRECTION CargoMovement to occur at time involving num piece(s) of cargo </pre>
     * Where:
     * <ul>
     *   <li>{@code DIRECTION} is the direction of the movement </li>
     *   <li>{@code time} is the time the movement is meant to occur </li>
     *   <li>{@code num} is the number of cargo pieces that are being moved</li>
     * </ul>
     * <p>
     * For example: <pre>
     * OUTBOUND CargoMovement to occur at 135 involving 5 piece(s) of cargo </pre>
     *
     * @return string representation of this CargoMovement
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s involving %d piece(s) of cargo",
            super.toString(),
            this.cargo.size());
    }

    /**
     * Returns the machine-readable string representation of this movement.
     *
     * The format of the string to return is
     *
     * {@code CargoMovement:time:direction:numCargo:ID1,ID2,...}
     *
     * Where:
     * <ul>
     *     <li>
     *         {@code time} is the time that the movement will be actioned
     *     </li>
     *     <li>
     *         {@code direction} is the direction of the movement
     *     </li>
     *     <li>
     *         {@code numCargo} is the number of the cargo in the movement
     *     </li>
     *     <li>
     *         {@code ID1,ID2,...} are the IDs of the cargo in the movement
     *         separated by a comma ','. There should be no trailing comma
     *         after the last ID.
     *     </li>
     * </ul>
     *
     * For example:
     * {@code CargoMovement:120:INBOUND:3:22,23,12}
     *
     * @return encoded string representation of this movement
     */
    public String encode() {
        return "";
    }

    /**
     * Creates a cargo movement from a string encoding.
     *
     * The format of the string should match the encoded representation of a
     * cargo movement, as described in {@link #encode()}.
     *
     * The encoded string is invalid if any of the following conditions are
     * true:
     * <ul>
     *     <li>
     *         The number of colons (:) detected was more/fewer than expected.
     *     </li>
     *     <li>
     *         The given string is not a CargoMovement encoding
     *     </li>
     *     <li>
     *         The time is not a long (i.e. cannot be parsed by {@code Long
     *         .parseLong(String)}).
     *     </li>
     *     <li>
     *         The time is less than zero (0).
     *     </li>
     *     <li>
     *         The movementDirection is not one of the valid directions (See
     *         {@link MovementDirection}).
     *     </li>
     *     <li>
     *         The number of ids is not an int (i.e. cannot be parsed by
     *         {@code Integer.parseInt(String)}).
     *     </li>
     *     <li>
     *         The number of ids is less than one (1).
     *     </li>
     *     <li>
     *         An id is not a int (i.e. cannot be parsed by
     *         {@code Integer.parseInt(String)}).
     *     </li>
     *     <li>
     *         An id is less than zero (0).
     *     </li>
     *     <li>
     *         There is no cargo that exists with a specified id.
     *     </li>
     * </ul>
     *
     * @param string string containing the encoded CargoMovement
     *
     * @return decoded CargoMovement instance
     *
     * @throws BadEncodingException if the format of the given string is
     * invalid according to the rules above
     */
    public static CargoMovement fromString(String string)
            throws BadEncodingException {
        return null;
    }
}
