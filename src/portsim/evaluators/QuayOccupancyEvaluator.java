package portsim.evaluators;

import portsim.movement.Movement;
import portsim.port.Port;
import portsim.port.Quay;

/**
 * Evaluator to monitor how many quays are currently occupied at the port.
 */
public class QuayOccupancyEvaluator extends StatisticsEvaluator {
    /**
     * Port to monitor for quays.
    */
    private Port port;

    /**
     * Constructs a new QuayOccupancyEvaluator
     *
     * @param port port to monitor quays
     */
    public QuayOccupancyEvaluator(Port port) {
        super();
        this.port = port;
    }

    /**
     * Return the number of quays that are currently occupied.
     * <p>
     * A quay is occupied if {@link Quay#isEmpty()} returns false.
     *
     * @return number of quays
     */
    public int getQuaysOccupied() {
        // TODO: Debug through the test of this method, including all other
        //  called methods. Determine why this is returning 1, instead of 2.
        int occupied = 0;
        for (Quay quay : port.getQuays()) {
            if (!quay.isEmpty()) {
                occupied++;
            }
        }
        return occupied;
    }

    /**
     * QuayOccupancyEvaluator does not make use of <pre>onProcessMovement()
     * </pre>, so this method can be left empty.
     * <p>
     * Does nothing. This method is not used by this evaluator.
     *
     * @param movement movement to read
     */
    @Override
    public void onProcessMovement(Movement movement) {}
}
