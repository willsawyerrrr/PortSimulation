package portsim.ship;


import portsim.cargo.Cargo;
import portsim.cargo.Container;
import portsim.port.ContainerQuay;
import portsim.port.Quay;
import portsim.util.BadEncodingException;
import portsim.util.NoSuchCargoException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Represents a ship capable of carrying shipping containers.
 *
 * @ass1_partial
 */
public class ContainerShip extends Ship {
    /**
     * Maximum number of containers the ship can carry
     */
    private int containerCapacity;

    /**
     * The containers currently on the ship
     */
    private List<Container> containers;

    /**
     * Creates a new container ship with the given IMO number, name and origin
     * port, nautical flag and cargo capacity.
     *
     * @param imoNumber  unique identifier
     * @param name       name of the ship
     * @param originFlag port of origin
     * @param flag       the nautical flag this ship is flying
     * @param capacity   the container capacity of this ship
     *
     * @throws IllegalArgumentException if a ship already exists with the given
     *                                  imoNumber, imoNumber &lt; 0,
     *                                  imoNumber is not 7 digits long or if
     *                                  the container capacity is &lt; than 0
     *
     * @ass1
     */
    public ContainerShip(long imoNumber, String name, String originFlag,
                         NauticalFlag flag, int capacity) throws IllegalArgumentException {
        super(imoNumber, name, originFlag, flag);
        if (capacity < 0) {
            throw new IllegalArgumentException("The container capacity of the"
                + " ship must be positive: " + capacity);
        }
        this.containerCapacity = capacity;
        this.containers = new ArrayList<>();
    }

    /**
     * Checks if this ship can dock with the specified quay.
     * <p>
     * The conditions for a compatible quay are:
     * <ol>
     *     <li>Quay must be a ContainerQuay.</li>
     *     <li>The quays maximum number of containers must be &ge; the number
     *     of containers currently on board.</li>
     * </ol>
     *
     * @param quay quay to be checked
     *
     * @return true if the Quay satisfies the conditions else false
     *
     * @ass1
     */
    @Override
    public boolean canDock(Quay quay) {
        if (quay instanceof ContainerQuay) {
            ContainerQuay containerQuay = (ContainerQuay) quay;
            // check if quay can handle ship cargo
            return containerQuay.getMaxContainers() >= this.containers.size();
        }
        return false;
    }

    /**
     * Checks whether the specified cargo can be loaded onto the ship.
     * <p>
     * The given cargo can only be loaded if all the following conditions are true:
     * <ol>
     *     <li>The cargo given is a Container</li>
     *     <li>The current number of containers on board is less than the
     *     container capacity</li>
     *     <li>The cargo's destination is the same as the ships origin
     *     country</li>
     * </ol>
     *
     * @param cargo cargo to be loaded
     *
     * @return true if the Cargo satisfies the conditions else false
     *
     * @ass1
     */
    @Override
    public boolean canLoad(Cargo cargo) {
        if (!(cargo instanceof Container)) {
            return false;
        }
        if (this.containers.size() == containerCapacity) {
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
     * {@link ContainerShip#canLoad(Cargo)}
     *
     * @ass1
     */
    @Override
    public void loadCargo(Cargo cargo) {
        this.containers.add((Container) cargo);
    }

    /**
     * Unloads the cargo from the ship.
     * <p>
     * The ship's cargo should be set to an empty list.
     *
     * @return the ship's cargo before it was unloaded
     *
     * @throws NoSuchCargoException if the ship has already been unloaded
     *                              (i.e. the ship has no cargo onboard)
     *
     * @ass1
     */
    public List<Container> unloadCargo() throws NoSuchCargoException {
        if (containers.size() == 0) {
            throw new NoSuchCargoException("Cargo has already been unloaded");
        }
        List<Container> unload = new ArrayList<>(containers);
        containers = new ArrayList<>();
        return unload;
    }

    /**
     * Returns the current cargo onboard this vessel.
     * <p>
     * Adding or removing elements from the returned list should not affect the original list.
     *
     * @return containers on the vessel
     *
     * @ass1
     */
    public List<Container> getCargo() {
        return new ArrayList<>(containers);
    }

    /**
     * Returns true if an only if this ContainerShip is equal to the other
     * given ContainerShip.
     *
     * For to ContainerShips to be equal, they must have the same name, fla,
     * IMO number, and container capacity.
     *
     * @param o other object to check equality
     *
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ContainerShip)) {
            return false;
        }
        ContainerShip containerShip = (ContainerShip) o;
        return super.equals(containerShip)
                && containerCapacity == containerShip.containerCapacity;
    }

    /**
     * Returns the hash code of this ContainerShip.
     *
     * Two ContainerShips that are equal according to {@link #equals(Object)}
     * method should have the same hash code.
     *
     * @return hash code of this ContainerShip.
     */
    @Override
    public int hashCode() {
        return super.hashCode() * containerCapacity;
    }

    /**
     * Returns the human-readable string representation of this ContainerShip.
     * <p>
     * The format of the string to return is
     * <pre>ContainerShip name from origin [flag] carrying num containers</pre>
     * Where:
     * <ul>
     *   <li>{@code name} is the name of this ship </li>
     *   <li>{@code origin} is the country of origin of this ship </li>
     *   <li>{@code flag} is the nautical flag of this ship </li>
     *   <li>{@code num} is the number of containers on board </li>
     * </ul>
     * For example:
     * <pre>
     * ContainerShip Evergreen from Australia [BRAVO] carrying 3 containers</pre>
     *
     * @return string representation of this ContainerShip
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s carrying %d containers",
            super.toString(),
            this.containers.size());
    }

    /**
     * Returns the machine-readable string representation of this Ship.
     *
     * The format of the string to return is
     *
     * {@code ShipClass:imoNumber:name:origin:flag:capacity:cargoNum:[ID1,
     * ID2,...]}
     *
     * Where:
     * <ul>
     *     <li>
     *         {@code ShipClass} is the Ship class name
     *     </li>
     *     <li>
     *         {@code imoNumber} is the IMO number of the ship
     *     </li>
     *     <li>
     *         {@code name} is the name of this ship
     *     </li>
     *     <li>
     *         {@code origin} is the country of origin of this ship
     *     </li>
     *     <li>
     *         {@code flag} is the nautical flag of this ship
     *     </li>
     *     <li>
     *         {@code capacity} is the container capacity of this ship
     *     </li>
     *     <li>
     *         {@code cargoNum} is the number of containers currently on board
     *     </li>
     *     <li>
     *         {@code ID1,ID2,...} are the IDs of the cargo on the ship
     *         separated with a comma or an empty string "" if there are none
     *     </li>
     * </ul>
     *
     * For example:
     * {@code ContainerShip:1338622:Columbus:Unknown:HOTEL:200:3:23,1,51}
     * or
     * {@code ContainerShip:1338622:Columbus:Unknown:HOTEL:200:0:}
     *
     * @return encoded string representation of this Ship
     */
    @Override
    public String encode() {
        StringJoiner joiner = new StringJoiner(",");
        for (Container container : this.getCargo()) {
            joiner.add(String.valueOf(container.getId()));
        }
        return String.format("%s:%d:%d:%s",
                super.encode(),
                containerCapacity,
                this.getCargo().size(),
                joiner);
    }

    static ContainerShip fromString(String[] attributes)
            throws BadEncodingException {
        long imoNumber;
        String name, originFlag;
        NauticalFlag flag;
        int capacity, numCargo;
        String[] rawCargoIds;
        int[] parsedCargoIds;
        List<Cargo> cargo;
        ContainerShip ship;

        try {
            imoNumber = Long.parseLong(attributes[1]);
            name = attributes[2];
            originFlag = attributes[3];
            flag = NauticalFlag.valueOf(attributes[4]);
            capacity = Integer.parseInt(attributes[5]);

            ship = new ContainerShip(imoNumber, name, originFlag, flag,
                    capacity);

            numCargo = Integer.parseInt(attributes[6]);
            if (numCargo < 0) {
                throw new BadEncodingException();
            }

            rawCargoIds = attributes[7].split(",");
            if (numCargo != rawCargoIds.length) {
                throw new BadEncodingException();
            }

            parsedCargoIds = new int[numCargo];
            for (int i = 0; i <= numCargo - 1; i++) {
                parsedCargoIds[i] = Integer.parseInt(rawCargoIds[i]);
            }

            cargo = new ArrayList<>();
            for (int id : parsedCargoIds) {
                if (id < 0) {
                    throw new BadEncodingException();
                }
                if (ship.canLoad(Container.getCargoById(id))) {
                    cargo.add(Container.getCargoById(id));
                }
            }
        } catch (IllegalArgumentException | NoSuchCargoException ignored) {
            throw new BadEncodingException();
        }

        return ship;
    }
}
