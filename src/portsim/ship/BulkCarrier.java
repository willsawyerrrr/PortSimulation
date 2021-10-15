package portsim.ship;

import portsim.cargo.BulkCargo;
import portsim.cargo.Cargo;
import portsim.port.BulkQuay;
import portsim.port.Quay;
import portsim.util.BadEncodingException;
import portsim.util.NoSuchCargoException;

import java.util.Objects;

/**
 * Represents a ship capable of carrying bulk cargo.
 */
public class BulkCarrier extends Ship {
    /**
     * Maximum amount of cargo in tonnes that the ship can carry
     */
    private int tonnageCapacity;

    /**
     * The cargo loaded on the ship
     */
    private BulkCargo cargo;

    /**
     * Creates a new bulk carrier with the given IMO number, name, origin
     * port, nautical flag and cargo capacity.
     *
     * @param imoNumber  unique identifier
     * @param name       name of the ship
     * @param originFlag port of origin
     * @param flag       the nautical flag this ship is flying
     * @param capacity   the tonnage capacity of this ship
     *
     * @throws IllegalArgumentException if a ship already exists with the given
     *                                  imoNumber, imoNumber &lt; 0, imoNumber
     *                                  is not 7 digits long or if the tonnage
     *                                  capacity is &lt; than 0
     */
    public BulkCarrier(long imoNumber, String name, String originFlag,
                       NauticalFlag flag, int capacity)
            throws IllegalArgumentException {
        super(imoNumber, name, originFlag, flag);
        if (capacity < 0) {
            throw new IllegalArgumentException("The tonnage capacity of the "
                + "ship must be positive: " + capacity);
        }
        this.tonnageCapacity = capacity;
        this.cargo = null;
    }

    /**
     * Check if this ship can dock with the specified quay.
     * <p>
     * The conditions for a compatible quay are:
     * <ol>
     *     <li>Quay must be a BulkQuay.</li>
     *     <li>The quay's maximum cargo weight must be &ge; the weight of the
     *     cargo on board the ship in tonnes.</li>
     * </ol>
     *
     * @param quay quay to be checked
     *
     * @return true if the Quay satisfies the conditions else false
     */
    @Override
    public boolean canDock(Quay quay) {
        if (quay instanceof BulkQuay) {
            BulkQuay bulkQuay = (BulkQuay) quay;
            return this.cargo == null
                    || bulkQuay.getMaxTonnage() >= this.cargo.getTonnage();
        }
        return false;
    }

    /**
     * Checks whether the specified cargo can be loaded onto the ship.
     * <p>
     * The given cargo can only be loaded if all the following conditions are true:
     * <ol>
     *     <li>The ship does not have any cargo on board</li>
     *     <li>The cargo given is a BulkCargo</li>
     *     <li>The cargo tonnage is less than or equal to the ship's tonnage
     *     capacity</li>
     *     <li>The cargo's destination is the same as the ships origin
     *     country</li>
     * </ol>
     *
     * @param cargo cargo to be loaded
     *
     * @return true if the Cargo satisfies the conditions else false
     */
    @Override
    public boolean canLoad(Cargo cargo) {
        if (this.cargo != null) {
            return false;
        }
        if (!(cargo instanceof BulkCargo)) {
            return false;
        }
        if (((BulkCargo) cargo).getTonnage() > tonnageCapacity) {
            return false;
        }
        return cargo.getDestination().equals(this.getOriginFlag());
    }

    /**
     * Loads the specified cargo onto the ship.
     *
     * @param cargo cargo to be loaded
     *
     * @require Cargo given is able to be loaded onto this ship according to
     *          {@link BulkCarrier#canLoad(Cargo)}
     */
    @Override
    public void loadCargo(Cargo cargo) {
        this.cargo = (BulkCargo) cargo;
    }

    /**
     * Unloads the cargo from the ship.
     * <p>
     * The ship's current cargo should be set to {@code null} at the end of the
     * operation.
     *
     * @return the ships cargo
     *
     * @throws NoSuchCargoException if the ship has already been unloaded
     */
    public BulkCargo unloadCargo() throws NoSuchCargoException {
        if (cargo == null) {
            throw new NoSuchCargoException("Cargo has already been unloaded");
        }
        BulkCargo unload = cargo;
        cargo = null;
        return unload;
    }

    /**
     * Returns the current cargo onboard this vessel.
     *
     * @return bulk cargo on the vessel
     */
    public BulkCargo getCargo() {
        return cargo;
    }

    /**
     * Returns true if and only if this BulkCarrier is equal to the other given
     * BulkCarrier.
     * <p>
     * For two BulkCarriers to be equal, they must have the same name, flag,
     * origin flag, IMO number, and tonnage capacity.
     *
     * @param o other object to check equality
     *
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BulkCarrier)) {
            return false;
        }
        BulkCarrier carrier = (BulkCarrier) o;
        return super.equals(carrier)
                && tonnageCapacity == carrier.tonnageCapacity;
    }

    /**
     * Returns the hash code of this BulkCarrier.
     * <p>
     * Two BulkCarriers that equal according to {@link #equals(Object)} method
     * should have the same hash code.
     *
     * @return hash code of this BulkCarrier.
     */
    @Override
    public int hashCode() {
        return super.hashCode() * tonnageCapacity;
    }

    /**
     * Returns the human-readable string representation of this BulkCarrier.
     * <p>
     * The format of the string to return is
     * <pre>BulkCarrier name from origin [flag] carrying cargoType</pre>
     * Where:
     * <ul>
     *   <li><pre>name</pre> is the name of this ship </li>
     *   <li><pre>origin</pre> is the country of origin of this ship </li>
     *   <li><pre>flag</pre> is the nautical flag of this ship </li>
     *   <li><pre>cargoType</pre> is the type of cargo on board or the literal
     *   String <pre>nothing</pre> if there is no cargo currently on board</li>
     * </ul>
     * For example:
     * <pre>BulkCarrier Evergreen from Australia [BRAVO] carrying OIL</pre>
     *
     * @return string representation of this BulkCarrier
     */
    @Override
    public String toString() {
        String base = super.toString() + " carrying ";
        return this.cargo != null ? base + this.cargo.getType() : base
            + "nothing";
    }

    /**
     * Returns the machine-readable string representation of this BulkCarrier.
     * <p>
     * The format of the string to return is:
     * <pre>BulkCarrier:imoNumber:name:origin:flag:capacity:id</pre>
     * <p>
     * Where:
     * <ul>
     *     <li><pre>imoNumber</pre> is the IMO number of the ship</li>
     *     <li><pre>name</pre> is the name of this ship</li>
     *     <li><pre>origin</pre> is the country of origin of this ship</li>
     *     <li><pre>flag</pre> is the nautical flag of this ship</li>
     *     <li><pre>capacity</pre> is the tonnage capacity of this ship</li>
     *     <li><pre>id</pre> is the ID of the cargo on the ship or an empty
     *     string "" if there is none</li>
     * </ul>
     * <p>
     * For example:
     * <pre>BulkCarrier:1248691:Voyager:NewZealand:HOTEL:200:3</pre> OR
     * <pre>BulkCarrier:1248291:Searcher:Australia:BRAVE:220:</pre>
     *
     * @return encoded string representation of this Ship
     */
    @Override
    public String encode() {
        return String.format("%s:%d:%d",
                super.encode(),
                tonnageCapacity,
                cargo.getId());
    }

    // TODO: Add JavaDoc here.
    static BulkCarrier fromString(String[] attributes)
            throws BadEncodingException {
        long imoNumber;
        String name;
        String originFlag;
        NauticalFlag flag;
        int capacity;
        int cargoId;
        BulkCarrier carrier;

        try {
            imoNumber = Long.parseLong(attributes[1]);
            name = attributes[2];
            originFlag = attributes[3];
            flag = NauticalFlag.valueOf(attributes[4]);
            capacity = Integer.parseInt(attributes[5]);

            try {
                cargoId = Integer.parseInt(attributes[6]);
                if (cargoId < 0) {
                    throw new BadEncodingException();
                }
            } catch (NumberFormatException nfe) {
                cargoId = -1;
            }

            carrier = new BulkCarrier(imoNumber, name, originFlag, flag,
                    capacity);

            if (cargoId != -1) {
                Cargo cargo = Cargo.getCargoById(cargoId);
                if (carrier.canLoad(cargo)) {
                    carrier.loadCargo(cargo);
                } else {
                    throw new BadEncodingException();
                }
            }

        } catch (IllegalArgumentException | NoSuchCargoException ignored) {
            throw new BadEncodingException();
        }

        return carrier;
    }
}
