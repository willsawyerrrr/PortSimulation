package portsim.ship;

import portsim.cargo.Cargo;
import portsim.port.Quay;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a ship whose movement is managed by the system.
 * <p>
 * Ships store various types of cargo which can be loaded and unloaded at a port.
 *
 * @ass1_partial
 */
public abstract class Ship {
    /**
     * Name of the ship
     */
    private String name;

    /**
     * Unique 7 digit identifier to identify this ship (no leading zero's [0])
     */
    private long imoNumber;

    /**
     * Port of origin of ship
     */
    private String originFlag;

    /**
     * Maritime flag designated for use on this ship
     */
    private NauticalFlag flag;

    /**
     * Database of all ships currently active in the simulation
     */
    private static Map<Long, Ship> shipRegistry = new HashMap<>();

    /**
     * Creates a new ship with the given
     * <a href="https://en.wikipedia.org/wiki/IMO_number">IMO number</a>,
     * name, origin port flag and nautical flag.
     * <p>
     * Finally, the ship should be added to the ship registry with the
     * IMO number as the key.
     *
     * @param imoNumber  unique identifier
     * @param name       name of the ship
     * @param originFlag port of origin
     * @param flag       the nautical flag this ship is flying
     * @throws IllegalArgumentException if a ship already exists with the given
     *                                  imoNumber, imoNumber &lt; 0 or imoNumber is not 7 digits
     *                                  long (no leading zero's [0])
     * @ass1_partial
     */
    public Ship(long imoNumber, String name, String originFlag,
                NauticalFlag flag) throws IllegalArgumentException {
        if (imoNumber < 0) {
            throw new IllegalArgumentException("The imoNumber of the ship "
                + "must be positive: " + imoNumber);
        }
        if (String.valueOf(imoNumber).length() != 7 || String.valueOf(imoNumber).startsWith("0")) {
            throw new IllegalArgumentException("The imoNumber of the ship "
                + "must have 7 digits (no leading zero's [0]): " + imoNumber);
        }
        this.imoNumber = imoNumber;
        this.name = name;
        this.originFlag = originFlag;
        this.flag = flag;
    }

    /**
     * Check if this ship can dock with the specified quay according
     * to the conditions determined by the ships type.
     *
     * @param quay quay to be checked
     * @return true if the Quay satisfies the conditions else false
     * @ass1
     */
    public abstract boolean canDock(Quay quay);

    /**
     * Checks if the specified cargo can be loaded onto the ship according
     * to the conditions determined by the ships type and contents.
     *
     * @param cargo cargo to be loaded
     * @return true if the Cargo satisfies the conditions else false
     * @ass1
     */
    public abstract boolean canLoad(Cargo cargo);

    /**
     * Loads the specified cargo onto the ship.
     *
     * @param cargo cargo to be loaded
     * @require Cargo given is able to be loaded onto this ship according to
     * the implementation of {@link Ship#canLoad(Cargo)}
     * @ass1
     */
    public abstract void loadCargo(Cargo cargo);

    /**
     * Returns this ship's name.
     *
     * @return name
     * @ass1
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns this ship's IMO number.
     *
     * @return imoNumber
     * @ass1
     */
    public long getImoNumber() {
        return this.imoNumber;
    }

    /**
     * Returns this ship's flag denoting its origin.
     *
     * @return originFlag
     * @ass1
     */
    public String getOriginFlag() {
        return this.originFlag;
    }

    /**
     * Returns the nautical flag the ship is flying.
     *
     * @return flag
     * @ass1
     */
    public NauticalFlag getFlag() {
        return this.flag;
    }

    /**
     * Returns the human-readable string representation of this Ship.
     * <p>
     * The format of the string to return is
     * <pre>ShipClass name from origin [flag]</pre>
     * Where:
     * <ul>
     *   <li>{@code ShipClass} is the Ship class</li>
     *   <li>{@code name} is the name of this ship</li>
     *   <li>{@code origin} is the country of origin of this ship</li>
     *   <li>{@code flag} is the nautical flag of this ship</li>
     * </ul>
     * For example: <pre>BulkCarrier Evergreen from Australia [BRAVO]</pre>
     *
     * @return string representation of this Ship
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s %s from %s [%s]",
            this.getClass().getSimpleName(),
            this.name,
            this.originFlag,
            this.flag);
    }

    /**
     * Resets the global ship registry.
     * This utility method is for the testing suite.
     *
     * @given
     */
    public static void resetShipRegistry() {
        Ship.shipRegistry = new HashMap<>();
    }
}
