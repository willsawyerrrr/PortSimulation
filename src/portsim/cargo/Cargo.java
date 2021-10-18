package portsim.cargo;

import portsim.util.BadEncodingException;
import portsim.util.Encodable;
import portsim.util.NoSuchCargoException;

import java.util.HashMap;
import java.util.Map;

/**
 * Denotes a cargo whose function is to be transported via a Ship or land
 * transport.
 * <p>
 * Cargo is kept track of via its ID.
 */
public abstract class Cargo implements Encodable {
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
     * When a new piece of cargo is created, it should be added to the cargo
     * registry.
     *
     * @param id          cargo ID
     * @param destination destination port
     *
     * @throws IllegalArgumentException if a cargo already exists with the
     *                                  given ID or ID &lt; 0
     */
    public Cargo(int id, String destination) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("Cargo ID must be greater than "
                + "or equal to 0: " + id);
        } else if (cargoRegistry.containsKey(id)) {
            throw new IllegalArgumentException("Cargo already exists with id: "
                    + id);
        }
        this.id = id;
        this.destination = destination;
        cargoRegistry.put(id, this);
    }

    /**
     * Retrieve the ID of this piece of cargo.
     *
     * @return the cargo's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieve the destination of this piece of cargo.
     *
     * @return the cargo's destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Returns the global registry of all pieces of cargo, as a mapping from
     * cargo IDs to Cargo instances.
     * <p>
     * Adding or removing elements from the returned map should not affect the
     * original map.
     *
     * @return cargo registry
     */
    public static Map<Integer, Cargo> getCargoRegistry() {
        return new HashMap<>(cargoRegistry);
    }

    /**
     * Checks if a cargo exists in the simulation using its ID.
     *
     * @param id unique key to identify cargo
     *
     * @return true if there is a cargo stored in the registry with key
     * <pre>id</pre> false otherwise
     */
    public static boolean cargoExists(int id) {
        return cargoRegistry.containsKey(id);
    }

    /**
     * Returns the cargo specified by the given ID.
     *
     * @param id unique key to identify cargo
     *
     * @return cargo specified by the id
     *
     * @throws NoSuchCargoException if the cargo does not exist in the registry
     */
    public static Cargo getCargoById(int id) throws NoSuchCargoException {
        if (cargoRegistry.containsKey(id)) {
            return cargoRegistry.get(id);
        }
        throw new NoSuchCargoException("No cargo exists with id: " + id);
    }

    /**
     * Returns true if and only if this cargo is equal to the other given cargo.
     * <p>
     * For two cargo to be equal, they must have the same ID and destination.
     *
     * @param o other object to check equality
     *
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cargo)) {
            return false;
        }
        Cargo cargo = (Cargo) o;
        return id == cargo.getId()
                && destination.equals(cargo.getDestination());
    }

    /**
     * Returns the hash code of this cargo.
     * <p>
     * Two cargo are equal according to {@link #equals(Object)} method should
     * have the same hash code.
     *
     * @return hash code of this cargo.
     */
    @Override
    public int hashCode() {
        return id * destination.hashCode();
    }

    /**
     * Returns the human-readable string representation of this cargo.
     * <p>
     * The format of the string to return is
     * <pre>CargoClass id to destination</pre>
     * Where:
     * <ul>
     *   <li><pre>CargoClass</pre> is the cargo class name</li>
     *   <li><pre>id</pre> is the id of this cargo </li>
     *   <li><pre>destination</pre> is the destination of the cargo </li>
     * </ul>
     * <p>
     * For example: <pre>Container 55 to New Zealand</pre>
     *
     * @return string representation of this Cargo
     */
    @Override
    public String toString() {
        return String.format("%s %d to %s",
            this.getClass().getSimpleName(),
            this.id,
            this.destination);
    }

    /**
     * Returns the machine-readable string representation of this Cargo.
     * <p>
     * The format of the string to return is
     * <p>
     * <pre>CargoClass:id:destination}</pre>
     * Where:
     * <ul>
     *     <li><pre>CargoClass</pre> is the Cargo class name</li>
     *     <li><pre>id</pre> is the id of this cargo</li>
     *     <li><pre>destination</pre> is the destination of this cargo</li>
     * </ul>
     * <p>
     * For example: <pre>Container:3:Australia</pre> OR <pre>BulkCargo:2:France
     * </pre>
     *
     * @return encoded string representation of this Cargo
     */
    public String encode() {
        return String.format("%s:%d:%s",
                this.getClass().getSimpleName(),
                this.id,
                this.destination);
    }

    /**
     * Reads a piece of cargo from its encoded representation in the given
     * string.
     * <p>
     * The format of the given string should match the encoded representation
     * of a Cargo, as described in {@link #encode()} (and subclasses).
     * <p>
     * The encoded string is invalid if any of the following conditions are
     * true:
     * <ul>
     *     <li>The number of colons (:) detected was more/fewer than
     *     expected.</li>
     *     <li>The cargo id is not an integer (i.e. cannot be parsed by
     *     {@code Integer.parseInt(String)}).</li>
     *     <li>The cargo id is less than one (1).</li>
     *     <li>A piece of cargo with the specified ID already exists</li>
     *     <li>The cargo type specified is not one of {@link BulkCargoType}
     *     or {@link ContainerType}</li>
     *     <li>If the cargo type is a BulkCargo:
     *         <ol>
     *             <li>The cargo weight in tonnes is not an integer (i.e.
     *             cannot be parsed by {@code Integer.parseInt(String)}).</li>
     *             <li>The cargo weight in tonnes in less than one (1).</li>
     *         </ol>
     *     </li>
     * </ul>
     *
     * @param string string containing the encoded cargo
     *
     * @return decoded cargo instance
     *
     * @throws BadEncodingException if the format of the given string is
     *                              invalid according to the rules above
     */
    public static Cargo fromString(String string) throws BadEncodingException {
        String[] attributes = string.split(":");

        if (attributes.length == 0) {
            throw new BadEncodingException();
        }

        if (attributes[0].equals("BulkCargo") && attributes.length == 5) {
            return BulkCargo.fromString(attributes);
        } else if (attributes[0].equals("Container") && attributes.length == 4) {
            return Container.fromString(attributes);
        }
        throw new BadEncodingException();
    }

    /**
     * Resets the global cargo registry.
     * <p>
     * This utility method is for the testing suite.
     */
    public static void resetCargoRegistry() {
        cargoRegistry = new HashMap<>();
    }
}
