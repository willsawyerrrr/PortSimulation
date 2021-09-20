package portsim.movement;

import portsim.cargo.Cargo;


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
}
