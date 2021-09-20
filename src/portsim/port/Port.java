package portsim.port;

import portsim.cargo.Cargo;
import portsim.movement.Movement;

import java.util.*;

/**
 * A place where ships can come and dock with Quays to load / unload their
 * cargo.
 * <p>
 * Ships can enter a port through its queue. Cargo is stored within the port at warehouses.
 *
 * @ass1_partial
 */
public class Port {

    /**
     * The name of this port used for identification
     */
    private String name;
    /**
     * The quays associated with this port
     */
    private List<Quay> quays;
    /**
     * The cargo currently stored at the port at warehouses. Cargo unloaded from trucks / ships
     */
    private List<Cargo> storedCargo;

    /**
     * Creates a new port with the given name.
     * <p>
     * The time since the simulation was started should be initialised as 0.
     * <p>
     * The list of quays in the port, stored cargo (warehouses) and statistics evaluators should be
     * initialised as empty lists.
     * <p>
     * An empty ShipQueue should be initialised, and a PriorityQueue should be initialised
     * to store movements ordered by the time of the movement (see {@link Movement#getTime()}).
     *
     * @param name name of the port
     * @ass1_partial
     */
    public Port(String name) throws IllegalArgumentException {
        this.name = name;
        this.quays = new ArrayList<Quay>();
        this.storedCargo = new ArrayList<Cargo>();
    }

    /**
     * Returns the name of this port.
     *
     * @return port's name
     * @ass1
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a list of all quays associated with this port.
     * <p>
     * Adding or removing elements from the returned list should not affect the original list.
     * <p>
     * The order in which quays appear in this list should be the same as
     * the order in which they were added by calling {@link #addQuay(Quay)}.
     *
     * @return all quays
     * @ass1
     */
    public List<Quay> getQuays() {
        return new ArrayList<>(this.quays);
    }

    /**
     * Returns the cargo stored in warehouses at this port.
     * <p>
     * Adding or removing elements from the returned list should not affect the original list.
     *
     * @return port cargo
     * @ass1
     */
    public List<Cargo> getCargo() {
        return new ArrayList<>(this.storedCargo);
    }

    /**
     * Adds a quay to the ports control.
     *
     * @param quay the quay to add
     * @ass1
     */
    public void addQuay(Quay quay) {
        this.quays.add(quay);
    }

}
