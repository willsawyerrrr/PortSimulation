package portsim.evaluators;

import portsim.movement.Movement;
import portsim.port.Port;
import portsim.port.Quay;

/**
 * Evaluator to monitor how many quays are currently occupied at the port.
 */
public class QuayOccupancyEvaluator extends StatisticsEvaluator {
    /**
     * Port for the QuayOccupancyEvaluator to monitor for quays.
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
        return 0;
    }

    /**
     * QuayOccupancyEvaluator does not make use of {@code onProcessMovement
     * ()}, so this method can be left empty.
     * <p>
     * Does nothing. This method is not used by this evaluator.
     *
     * @param movement movement to read
     */
    public void onProcessMovement(Movement movement) {}
}
