package portsim.cargo;

import portsim.util.BadEncodingException;

/**
 * Represents a shipping container, used for holding or transporting something.
 */
public class Container extends Cargo {
    /**
     * The type of the container
     */
    private ContainerType type;

    /**
     * Creates a new Container of the specified {@link ContainerType}, with the
     * given ID and destination.
     *
     * @param id          cargo ID
     * @param destination destination port
     * @param type        type of container
     *
     * @throws IllegalArgumentException if a cargo already exists with the
     *                                  given ID or ID &lt; 0
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
     */
    public ContainerType getType() {
        return type;
    }

    /**
     * Returns true if and only if this Container is equal to the other given
     * Container
     * <p>
     * For two Containers to be equal, they must have the same ID,
     * destination and type.
     *
     * @param o other object to check for equality
     *
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Container)) {
            return false;
        }
        Container container = (Container) o;
        return super.equals(container)
                && type == container.getType();
    }

    /**
     * Returns the hash code of this Container.
     * <p>
     * Two Containers that are equal according to the {@link #equals(Object)}
     * method should have the same hash code.
     *
     * @return hash code of this Container.
     */
    @Override
    public int hashCode() {
        return super.hashCode() * type.hashCode();
    }

    /**
     * Returns the human-readable string representation of this Container.
     * <p>
     * The format of the string to return is
     * <pre>Container id to destination [type]</pre>
     * Where:
     * <ul>
     *   <li><pre>id</pre> is the id of this cargo </li>
     *   <li><pre>destination</pre> is the destination of the cargo </li>
     *   <li><pre>type</pre> is the type of cargo</li>
     * </ul>
     * <p>
     * For example: <pre>Container 42 to Brazil [OTHER]</pre>
     *
     * @return string representation of this Container
     */
    @Override
    public String toString() {
        return String.format("%s [%s]",
            super.toString(),
            this.type);
    }

    /**
     * Returns the machine-readable string representation of this Container.
     * <p>
     * The format of the string to return is
     * <p>
     * <pre>Container:id:destination:type</pre>
     * <p>
     * Where:
     * <ul>
     *     <li><pre>id</pre> is the id of this cargo</li>
     *     <li><pre>destination</pre> is the destination of this cargo</li>
     *     <li><pre>type</pre> is the container type</li>
     * </ul>
     * <p>
     * For example: <pre>Container:3:Australia:OPEN_TOP</pre>
     *
     * @return encoded string representation of this Cargo
     */
    public String encode() {
        return String.format("%s:%s",
                super.encode(),
                type);
    }

    /**
     * Reads a piece of container from its representation in the given array
     * of strings.
     * <p>
     * This is a helper method called by {@link Cargo#fromString(String)}.
     *
     * @param attributes string representations of the attributes required to
     *                   create a container object
     *
     * @return decoded container instance
     *
     * @throws BadEncodingException if the format of the given arguments is
     *                              invalid according to the rules defined
     *                              within {@link Cargo#fromString(String)}
     */
    static Container fromString(String[] attributes)
            throws BadEncodingException {
        int id;
        String destination;
        ContainerType type;

        try {
            id = Integer.parseInt(attributes[1]);
            destination = attributes[2];
            type = ContainerType.valueOf(attributes[3]);
        } catch (NumberFormatException ignored) {
            throw new BadEncodingException();
        }

        return new Container(id, destination, type);
    }
}
