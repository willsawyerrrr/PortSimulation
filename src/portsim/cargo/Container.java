package portsim.cargo;

/**
 * Represents a shipping container, used for holding or transporting something.
 *
 * @ass1_partial
 */
public class Container extends Cargo {
    /**
     * The type of the container
     */
    private ContainerType type;

    /**
     * Creates a new Container of the specified {@link ContainerType},
     * with the given ID and destination.
     *
     * @param id          cargo ID
     * @param destination destination port
     * @param type        type of container
     * @throws IllegalArgumentException if a cargo already exists with the
     *                                  given ID or ID &lt; 0
     * @ass1
     */
    public Container(int id, String destination, ContainerType type)
        throws IllegalArgumentException {
        super(id, destination);
        this.type = type;
    }

    /**
     * Returns the type of this container.
     *
     * @return container type
     * @ass1
     */
    public ContainerType getType() {
        return type;
    }

    /**
     * Returns the human-readable string representation of this Container.
     * <p>
     * The format of the string to return is
     * <pre>Container id to destination [type]</pre>
     * Where:
     * <ul>
     *   <li>{@code id} is the id of this cargo </li>
     *   <li>{@code destination} is the destination of the cargo </li>
     *   <li>{@code type} is the type of cargo</li>
     * </ul>
     * For example: <pre>Container 42 to Brazil [OTHER]</pre>
     *
     * @return string representation of this Container
     * @ass1
     */
    @Override
    public String toString() {
        return String.format("%s [%s]",
            super.toString(),
            this.type);
    }

}
