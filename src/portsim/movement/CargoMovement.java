package portsim.movement;

import portsim.cargo.Cargo;
import portsim.util.BadEncodingException;
import portsim.util.NoSuchCargoException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * The movement of cargo coming into or out of the port.
 */
public class CargoMovement extends Movement {

    /**
     * The cargo that will be involved in the movement
     */
    private List<Cargo> cargo;

    /**
     * Creates a new cargo movement with the given action time and direction to
     * be undertaken with the given cargo.
     *
     * @param time      the time the movement should occur
     * @param direction the direction of the movement
     * @param cargo     the cargo to be moved
     *
     * @throws IllegalArgumentException if time &lt; 0
     */
    public CargoMovement(long time, MovementDirection direction,
                         List<Cargo> cargo) throws IllegalArgumentException {
        super(time, direction);
        this.cargo = cargo;
    }

    /**
     * Returns the cargo that will be moved.
     * <p>
     * Adding or removing elements from the returned list should not affect
     * the original list.
     *
     * @return all cargo in the movement
     */
    public List<Cargo> getCargo() {
        return new ArrayList<>(cargo);
    }

    /**
     * Returns the human-readable string representation of this CargoMovement.
     * <p>
     * The format of the string to return is
     * <pre>DIRECTION CargoMovement to occur at time involving num piece(s) of
     * cargo </pre>
     * Where:
     * <ul>
     *   <li><pre>DIRECTION</pre> is the direction of the movement </li>
     *   <li><pre>time</pre> is the time the movement is meant to occur </li>
     *   <li><pre>num</pre> is the number of cargo pieces that are being
     *   moved</li>
     * </ul>
     * <p>
     * For example: <pre>OUTBOUND CargoMovement to occur at 135 involving 5
     * piece(s) of cargo </pre>
     *
     * @return string representation of this CargoMovement
     */
    @Override
    public String toString() {
        return String.format("%s involving %d piece(s) of cargo",
            super.toString(),
            this.cargo.size());
    }

    /**
     * Returns the machine-readable string representation of this movement.
     * <p>
     * The format of the string to return is:
     * <pre>CargoMovement:time:direction:numCargo:ID1,ID2,...</pre>
     * <p>
     * Where:
     * <ul>
     *     <li><pre>time</pre> is the time that the movement will be
     *     actioned</li>
     *     <li><pre>direction</pre> is the direction of the movement</li>
     *     <li><pre>numCargo</pre> is the number of the cargo in the movement
     *     </li>
     *     <li><pre>ID1,ID2,...</pre> are the IDs of the cargo in the
     *     movement separated by a comma ','. There should be no trailing
     *     comma after the last ID.</li>
     * </ul>
     * <p>
     * For example:
     * <pre>CargoMovement:120:INBOUND:3:22,23,12</pre>
     *
     * @return encoded string representation of this movement
     */
    public String encode() {
        StringJoiner joiner = new StringJoiner(",");
        this.getCargo().forEach(cargo ->
                joiner.add(String.valueOf(cargo.getId())));
        return String.format("%s:%d:%s",
                super.encode(),
                this.cargo.size(),
                joiner);
    }

    /**
     * Creates a cargo movement from a string encoding.
     * <p>
     * The format of the string should match the encoded representation of a
     * cargo movement, as described in {@link #encode()}.
     * <p>
     * The encoded string is invalid if any of the following conditions are
     * true:
     * <ul>
     *     <li>The number of colons (:) detected was more/fewer than
     *     expected.</li>
     *     <li>The given string is not a CargoMovement encoding</li>
     *     <li>The time is not a long (i.e. cannot be parsed by
     *     <pre>Long.parseLong(String)}).</pre></li>
     *     <li>The time is less than zero (0).</li>
     *     <li>The movementDirection is not one of the valid directions (See
     *         {@link MovementDirection}).</li>
     *     <li>The number of ids is not an int (i.e. cannot be parsed by
     *     <pre>Integer.parseInt(String)</pre>).</li>
     *     <li>The number of ids is less than one (1).</li>
     *     <li>An id is not a int (i.e. cannot be parsed by
     *     <pre>Integer.parseInt(String)</pre>).</li>
     *     <li>An id is less than zero (0).</li>
     *     <li>There is no cargo that exists with a specified id.</li>
     * </ul>
     *
     * @param string string containing the encoded CargoMovement
     *
     * @return decoded CargoMovement instance
     *
     * @throws BadEncodingException if the format of the given string is
     *                              invalid according to the rules above
     */
    public static CargoMovement fromString(String string)
            throws BadEncodingException {
        String[] attributes = string.split(":");

        long time;
        MovementDirection direction;
        int numCargo;
        String[] cargoIds;
        List<Cargo> cargo;

        if (!attributes[0].equals("CargoMovement")
                || attributes.length != 5
                || string.endsWith(",")) {
            throw new BadEncodingException();
        }

        try {
            time = Long.parseLong(attributes[1]);
            if (time < 0) {
                throw new BadEncodingException();
            }

            direction = MovementDirection.valueOf(attributes[2]);

            numCargo = Integer.parseInt(attributes[3]);
            if (numCargo < 1) {
                throw new BadEncodingException();
            }

            cargoIds = attributes[4].split(",");
            if (numCargo != cargoIds.length) {
                throw new BadEncodingException();
            }

            cargo = new ArrayList<>();
            for (String rawId : cargoIds) {
                int id = Integer.parseInt(rawId);
                if (id < 0) {
                    throw new BadEncodingException();
                }
                cargo.add(Cargo.getCargoById(id));
            }
        } catch (IllegalArgumentException | NoSuchCargoException ignored) {
            throw new BadEncodingException();
        }

        return new CargoMovement(time, direction, cargo);
    }
}
