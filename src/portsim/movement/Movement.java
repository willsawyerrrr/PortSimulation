package portsim.movement;

import portsim.util.Encodable;

import java.util.Comparator;

/**
 * The movement of ships or cargo coming into or out of the port from land or
 * sea.
 */
public abstract class Movement implements Encodable {
    /**
     * The time in minutes that the movement should be completed
     */
    private long time;

    /**
     * The direction of the movement in relation to the port
     */
    private MovementDirection direction;

    /**
     * A comparison function, which imposes a <em>total ordering</em> on
     * Movements.
     */
    public static class MovementComparator implements Comparator<Movement> {
        /**
         * Compares two Movements for order. Returns a negative integer,
         * zero, or a positive integer as the first argument is to occur
         * before, at the same time as, or after  the second.
         *
         * @param m1 the first movement to be compared
         * @param m2 the second movement to be compared
         *
         * @return a negative integer, zero, or a positive integer as the first
         *         argument is to occur before, at the same time as, or after
         *         the second
         *
         * @throws NullPointerException if an argument is null and this
         *                              comparator does not permit null
         *                              arguments
         * @throws ClassCastException if the arguments' types prevent them from
         *                            being compared by this comparator
         */
        @Override
        public int compare(Movement m1, Movement m2)
                throws NullPointerException, ClassCastException {
            Long time1 = m1.getTime();
            Long time2 = m2.getTime();
            return time1.compareTo(time2);
        }
    }

    /**
     * Creates a new movement with the given action time and direction.
     *
     * @param time      the time the movement should occur
     * @param direction the direction of the movement
     *
     * @throws IllegalArgumentException if time &lt; 0
     */
    public Movement(long time, MovementDirection direction)
            throws IllegalArgumentException {
        if (time < 0) {
            throw new IllegalArgumentException("Time must be greater than or "
                + "equal to 0: " + time);
        }
        this.time = time;
        this.direction = direction;
    }

    /**
     * Returns the time the movement should be actioned.
     *
     * @return movement time
     */
    public long getTime() {
        return time;
    }

    /**
     * Returns the direction of the movement.
     *
     * @return movement direction
     */
    public MovementDirection getDirection() {
        return direction;
    }

    /**
     * Returns the human-readable string representation of this Movement.
     * <p>
     * The format of the string to return is
     * <pre>DIRECTION MovementClass to occur at time</pre>
     * Where:
     * <ul>
     *   <li><pre>DIRECTION</pre> is the direction of the movement </li>
     *   <li><pre>MovementClass</pre> is the Movement class name</li>
     *   <li><pre>time</pre> is the time the movement is meant to occur</li>
     * </ul>
     * For example: <pre>INBOUND Movement to occur at 120</pre>
     *
     * @return string representation of this Movement
     */
    @Override
    public String toString() {
        return String.format("%s %s to occur at %d",
            this.direction,
            this.getClass().getSimpleName(),
            this.time);
    }

    /**
     * Returns the machine-readable string representation of this movement.
     * <p>
     * The format of the string to return is:
     * <pre>MovementClass:time:direction</pre>
     * <p>
     * Where:
     * <ul>
     *     <li><pre>MovementClass</pre> is the Movement class name</li>
     *     <li><pre>time</pre> is the time that the movement will be
     *     actioned</li>
     *     <li><pre>direction</pre> is the direction of the movement</li>
     * </ul>
     * <p>
     * For example: <pre>CargoMovement:120:INBOUND</pre>
     *
     * @return encoded string representation of this movement
     */
    @Override
    public String encode() {
        return String.format("%s:%d:%s",
                this.getClass().getSimpleName(),
                time,
                direction);
    }
}
