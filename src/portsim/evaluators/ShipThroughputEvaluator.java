package portsim.evaluators;

import portsim.movement.Movement;
import portsim.movement.MovementDirection;
import portsim.movement.ShipMovement;

import java.util.ArrayList;
import java.util.List;

/**
 * Gathers data on how many ships pass through the port over time.
 * <p>
 * This evaluator only counts ships that have passed through the port in the
 * last hour (60 minutes).
 */
public class ShipThroughputEvaluator extends StatisticsEvaluator {
    /**
     * The movements of ships which have passed through the port in the last 60
     * minutes
     */
    private List<Movement> throughput;

    /**
     * Constructs a new ShipThroughputElevator.
     * <p>
     * Immediately after creating a new ShipThroughputEvaluator,
     * {@link #getThroughputPerHour()} should return 0.
     */
    public ShipThroughputEvaluator() {
        super();
        throughput = new ArrayList<>();
    }

    /**
     * Return the number of ships that have passed through the port in the last
     * 60 minutes.
     *
     * @return ships throughput
     */
    public int getThroughputPerHour() {
        return throughput.size();
    }

    /**
     * Updates the internal count of ships that have passed through the port
     * using the given movement.
     * <p>
     * If the movement is not an OUTBOUND ShipMovement, this method returns
     * immediately without taking any action.
     * <p>
     * Otherwise, the internal state of this evaluator should be modified
     * such that {@link ShipThroughputEvaluator#getThroughputPerHour()}
     * should return a value 1 more than before this method was called. e.g.
     * If the following code and output occurred over a program execution:
     * <p>
     * <table>
     *     <caption>Example of behaviour</caption>
     *     <tr>
     *         <th>Java method call</th>
     *         <th>Returned value</th>
     *     </tr>
     *     <tr>
     *         <td>getThroughputPerHour()</td>
     *         <td>1</td>
     *     </tr>
     *     <tr>
     *         <td>onProcessMovement(validMovement)</td>
     *         <td>void</td>
     *     </tr>
     *     <tr>
     *         <td>getThroughputPerHour()</td>
     *         <td>2</td>
     *     </tr>
     * </table>
     * <p>
     * Where <pre>validMovement</pre> is an OUTBOUND ShipMovement.
     *
     * @param movement movement to read
     */
    public void onProcessMovement(Movement movement) {
        if (movement instanceof ShipMovement
                && movement.getDirection() == MovementDirection.OUTBOUND) {
            throughput.add(movement);
        }
    }

    /**
     * Simulate a minute passing. The time since the evaluator was created
     * should be incremented by one.
     * <p>
     * If it has been more than 60 minutes since a ship exited the port, it
     * should no longer be counted towards the count returned by
     * {@link #getThroughputPerHour()}.
     */
    public void elapseOneMinute() {
        super.elapseOneMinute();
        throughput.removeIf(
                movement -> this.getTime() - movement.getTime() > 60);
    }
}
