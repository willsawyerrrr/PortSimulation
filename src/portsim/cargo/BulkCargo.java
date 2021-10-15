package portsim.cargo;

import portsim.util.BadEncodingException;

/**
 * Bulk cargo is commodity cargo that is transported unpacked in large
 * quantities.
 */
public class BulkCargo extends Cargo {
    /**
     * The weight in tonnes of the bulk cargo
     */
    private int tonnage;

    /**
     * The type of bulk cargo
     */
    private BulkCargoType type;

    /**
     * Creates a new Bulk Cargo with the given ID, destination, tonnage and
     * type.
     *
     * @param id          cargo ID
     * @param destination destination port
     * @param tonnage     the weight of the cargo
     * @param type        the type of cargo
     *
     * @throws IllegalArgumentException if a cargo already exists with the
     *                                  given ID or ID &lt; 0 or tonnage &lt; 0
     */
    public BulkCargo(int id, String destination, int tonnage,
                     BulkCargoType type) throws IllegalArgumentException {
        super(id, destination);
        if (tonnage < 0) {
            throw new IllegalArgumentException("The cargo tonnage must be "
                + "greater than or equal to 0: " + tonnage);
        }
        this.tonnage = tonnage;
        this.type = type;
    }

    /**
     * Returns the weight in tonnes of this bulk cargo.
     *
     * @return cargo tonnage
     */
    public int getTonnage() {
        return tonnage;
    }

    /**
     * Returns the BulkCargoType of this bulk cargo.
     *
     * @return cargo type
     */
    public BulkCargoType getType() {
        return type;
    }

    /**
     * Returns true if and only if this BulkCargo is equal to the other given
     * BulkCargo.
     * <p>
     * For two BulkCargo to be equal, they must have the same ID, destination,
     * type and tonnage.
     *
     * @param o other object to check equality
     *
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BulkCargo)) {
            return false;
        }
        BulkCargo bulkCargo = (BulkCargo) o;
        return super.equals(bulkCargo)
                && type == bulkCargo.getType()
                && tonnage == bulkCargo.getTonnage();
    }

    /**
     * Returns the hash code of this BulkCargo.
     * <p>
     * Two BulkCargo are equal according to {@link #equals(Object)} method
     * should have the same hash code.
     *
     * @return hash code of this BulkCargo.
     */
    @Override
    public int hashCode() {
        return super.hashCode() % type.hashCode() + tonnage;
    }

    /**
     * Returns the human-readable string representation of this BulkCargo.
     * <p>
     * The format of the string to return is
     * <pre>BulkCargo id to destination [type - tonnage]</pre>
     * Where:
     * <ul>
     *   <li><pre>id</pre> is the id of this cargo </li>
     *   <li><pre>destination</pre> is the destination of the cargo </li>
     *   <li><pre>type</pre> is the type of cargo</li>
     *   <li><pre>tonnage</pre> is the tonnage of the cargo</li>
     * </ul>
     * For example: <pre>BulkCargo 42 to Brazil [OIL - 420]</pre>
     *
     * @return string representation of this BulkCargo
     */
    @Override
    public String toString() {
        return String.format("%s [%s - %d]",
                super.toString(),
                this.type,
                this.tonnage);
    }

    /**
     * Returns the machine-readable string representation of this BulkCargo.
     * <p>
     * The format of the string to return is
     * <p>
     * <pre>BulkCargo:id:destination:type:tonnage</pre>
     * <p>
     * Where:
     * <ul>
     *     <li><pre>id</pre>is the id of this cargo</li>
     *     <li><pre>destination</pre> is the destination of this cargo</li>
     *     <li><pre>type</pre> is the bulk cargo type</li>
     *     <li><pre>tonnage</pre> is the bulk cargo weight in tonnes</li>
     * </ul>
     * <p>
     * For example:
     * <pre>BulkCargo:2:Germany:GRAIN:50</pre>
     *
     * @return encoded string representation of this Cargo
     */
    @Override
    public String encode() {
        return String.format("%s:%s:%d",
                super.encode(),
                this.type,
                this.tonnage);
    }

    /**
     * Reads a piece of bulk cargo from its representation in the given array
     * of strings.
     * <p>
     * This is a helper method called by {@link Cargo#fromString(String)}.
     *
     * @param attributes string representations of the attributes required to
     *                   create a bulk cargo object
     *
     * @return decoded bulk cargo instance
     *
     * @throws BadEncodingException if the format of the given arguments is
     *                              invalid according to the rules defined
     *                              within {@link Cargo#fromString(String)}
     */
    static BulkCargo fromString(String[] attributes)
            throws BadEncodingException {
        int id;
        int tonnage;
        String destination;
        BulkCargoType type;

        try {
            id = Integer.parseInt(attributes[1]);
            destination = attributes[2];
            type = BulkCargoType.valueOf(attributes[3]);
            tonnage = Integer.parseInt(attributes[4]);
        } catch (IllegalArgumentException ignored) {
            throw new BadEncodingException();
        }

        if (id < 0 || tonnage < 0) {
            throw new BadEncodingException();
        }

        return new BulkCargo(id, destination, tonnage, type);
    }
}
