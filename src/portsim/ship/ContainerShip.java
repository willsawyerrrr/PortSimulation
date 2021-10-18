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
     */
    public ContainerShip(long imoNumber, String name, String originFlag,
                         NauticalFlag flag, int capacity)
            throws IllegalArgumentException {
        super(imoNumber, name, originFlag, flag);
        if (capacity < 0) {
            throw new IllegalArgumentException("The container capacity of the "
                + "ship must be positive: " + capacity);
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
     * The given cargo can only be loaded if all the following conditions are
     * true:
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
     *          {@link ContainerShip#canLoad(Cargo)}
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
     * Adding or removing elements from the returned list should not affect the
     * original list.
     *
     * @return containers on the vessel
     */
    public List<Container> getCargo() {
        return new ArrayList<>(containers);
    }

    /**
     * Returns true if an only if this ContainerShip is equal to the other
     * given ContainerShip.
     * <p>
     * For to ContainerShips to be equal, they must have the same name, flag,
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
     * <p>
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
     *  <p>
     * Where:
     * <ul>
     *   <li><pre>name</pre> is the name of this ship </li>
     *   <li><pre>origin</pre> is the country of origin of this ship </li>
     *   <li><pre>flag</pre> is the nautical flag of this ship </li>
     *   <li><pre>num</pre> is the number of containers on board </li>
     * </ul>
     *  <p>
     * For example:
     * <pre>ContainerShip Evergreen from Australia [BRAVO] carrying 3
     * containers</pre>
     *
     * @return string representation of this ContainerShip
     */
    @Override
    public String toString() {
        return String.format("%s carrying %d containers",
            super.toString(),
            this.containers.size());
    }

    /**
     * Returns the machine-readable string representation of this Ship.
     * <p>
     * The format of the string to return is
     * <pre>ShipClass:imoNumber:name:origin:flag:capacity:cargoNum:[ID1,
     * ID2,...]</pre>
     * <p>
     * Where:
     * <ul>
     *     <li><pre>imoNumber</pre> is the IMO number of the ship</li>
     *     <li><pre>name</pre> is the name of this ship</li>
     *     <li><pre>origin</pre> is the country of origin of this ship</li>
     *     <li><pre>flag</pre> is the nautical flag of this ship</li>
     *     <li><pre>capacity</pre> is the container capacity of this ship</li>
     *     <li><pre>cargoNum</pre> is the number of containers currently on
     *     board</li>
     *     <li><pre>ID1,ID2,...</pre> are the IDs of the cargo on the ship
     *     separated with a comma or an empty string "" if there are none</li>
     * </ul>
     * <p>
     * For example:
     * <pre>ContainerShip:1338622:Columbus:Unknown:HOTEL:200:3:23,1,51</pre> OR
     * <pre>ContainerShip:1338622:Columbus:Unknown:HOTEL:200:0:</pre>
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

    /**
     * Reads a bulk container ship from its representation in the given array
     * of strings.
     * <p>
     * This is a helper method called by {@link Ship#fromString(String)}.
     *
     * @param attributes string representations of the attributes required to
     *                   create a container ship object
     *
     * @return decoded container ship instance
     *
     * @throws BadEncodingException if the format of the given arguments is
     *                              invalid according to the rules defined
     *                              within {@link Ship#fromString(String)}
     */
    static ContainerShip fromString(String[] attributes)
            throws BadEncodingException {
        long imoNumber;
        String name;
        String originFlag;
        NauticalFlag flag;
        int capacity;
        int numCargo;
        String[] cargoIds;
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

            cargoIds = attributes[7].split(",");
            if (numCargo != cargoIds.length) {
                throw new BadEncodingException();
            }

            cargo = new ArrayList<>();
            for (String rawId : cargoIds) {
                int id = Integer.parseInt(rawId);
                if (id > 0
                        || !Cargo.cargoExists(id)
                        || !ship.canLoad(Cargo.getCargoById(id))) {
                    throw new BadEncodingException();
                }
                cargo.add(Cargo.getCargoById(id));
            }
        } catch (IllegalArgumentException | NoSuchCargoException ignored) {
            throw new BadEncodingException();
        }

        return ship;
    }
}
