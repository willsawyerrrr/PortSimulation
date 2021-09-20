package portsim.port;

import portsim.ship.Ship;


/**
 * Quay is a platform lying alongside or projecting into the water where
 * ships are moored for loading or unloading.
 *
 * @ass1_partial
 */
public abstract class Quay {
    /**
     * The ID of the quay
     */
    private int id;

    /**
     * The ship currently in the Quay
     */
    private Ship ship;

    /**
     * Creates a new Quay with the given ID, with no ship docked at the quay.
     *
     * @param id quay ID
     * @throws IllegalArgumentException if ID &lt; 0
     * @ass1
     */
    public Quay(int id) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("Quay ID must be greater than"
                + " or equal to 0: " + id);
        }
        this.id = id;
        this.ship = null;
    }

    /**
     * Get the id of this quay
     *
     * @return quay id
     * @ass1
     */
    public int getId() {
        return id;
    }

    /**
     * Docks the given ship at the Quay so that the quay becomes occupied.
     *
     * @param ship ship to dock to the quay
     * @ass1
     */
    public void shipArrives(Ship ship) {
        this.ship = ship;
    }

    /**
     * Removes the current ship docked at the quay.
     * The current ship should be set to {@code null}.
     *
     * @return the current ship or null if quay is empty.
     * @ass1
     */
    public Ship shipDeparts() {
        Ship current = this.ship;
        this.ship = null;
        return current;
    }

    /**
     * Returns whether a ship is currently docked at this quay.
     *
     * @return true if there is no ship docked else false
     * @ass1
     */
    public boolean isEmpty() {
        return this.ship == null;
    }

    /**
     * Returns the ship currently docked at the quay.
     *
     * @return ship at quay or null if no ship is docked
     * @ass1
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * Returns the human-readable string representation of this quay.
     * <p>
     * The format of the string to return is
     * <pre>QuayClass id [Ship: imoNumber]</pre>
     * Where:
     * <ul>
     * <li>{@code id} is the ID of this quay</li>
     * <li>{@code imoNumber} is the IMO number of the ship docked at this
     * quay, or {@code None} if the quay is unoccupied.</li>
     * </ul>
     * <p>
     * For example: <pre>BulkQuay 1 [Ship: 2313212]</pre> or
     * <pre>ContainerQuay 3 [Ship: None]</pre>
     *
     * @return string representation of this quay
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s %d [Ship: %s]",
            this.getClass().getSimpleName(),
            this.id,
            (this.ship != null ? this.ship.getImoNumber() : "None"));
    }

}
