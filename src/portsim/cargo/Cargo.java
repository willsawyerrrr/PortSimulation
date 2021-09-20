package portsim.cargo;

import java.util.HashMap;
import java.util.Map;

/**
 * Denotes a cargo whose function is to be transported via a Ship or land
 * transport.
 * <p>
 * Cargo is kept track of via its ID.
 *
 * @ass1_partial
 */
public abstract class Cargo {
    /**
     * The ID of the cargo instance
     */
    private int id;

    /**
     * Destination for this cargo
     */
    private String destination;

    /**
     * Database of all cargo currently active in the simulation
     */
    private static Map<Integer, Cargo> cargoRegistry = new HashMap<>();

    /**
     * Creates a new Cargo with the given ID and destination port.
     * <p>
     * When a new piece of cargo is created, it should be added to the cargo registry.
     * @param id          cargo ID
     * @param destination destination port
     * @throws IllegalArgumentException if a cargo already exists with the
     *                                  given ID or ID &lt; 0
     * @ass1_partial
     */
    public Cargo(int id, String destination) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("Cargo ID must be greater than"
                + " or equal to 0: " + id);
        }
        this.id = id;
        this.destination = destination;
    }

    /**
     * Retrieve the ID of this piece of cargo.
     *
     * @return the cargo's ID
     * @ass1
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieve the destination of this piece of cargo.
     *
     * @return the cargo's destination
     * @ass1
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Returns the human-readable string representation of this cargo.
     * <p>
     * The format of the string to return is
     * <pre>CargoClass id to destination</pre>
     * Where:
     * <ul>
     *   <li>{@code CargoClass} is the cargo class name</li>
     *   <li>{@code id} is the id of this cargo </li>
     *   <li>{@code destination} is the destination of the cargo </li>
     * </ul>
     * <p>
     * For example: <pre>Container 55 to New Zealand</pre>
     *
     * @return string representation of this Cargo
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s %d to %s",
            this.getClass().getSimpleName(),
            this.id,
            this.destination);
    }

    /**
     * Resets the global cargo registry.
     * This utility method is for the testing suite.
     *
     * @given
     */
    public static void resetCargoRegistry() {
        Cargo.cargoRegistry = new HashMap<>();
    }
}
