package portsim.evaluators;

import portsim.cargo.BulkCargoType;
import portsim.cargo.ContainerType;
import portsim.movement.Movement;

import java.util.Map;

/**
 * Collects data on what types of cargo are passing through the port. Gathers
 * data on all derivatives of the cargo class.
 * <p>
 * The data gathered is a count of how many times each type of cargo has
 * entered the port. This includes a count of how many times the port has
 * received "BulkCargo" or "Container" class cargo. As well as a count of how
 * many times the port has seen each cargo subclass type
 * ({@link portsim.cargo.ContainerType} and
 * {@link portsim.cargo.BulkCargoType}).
 */
public class CargoDecompositionEvaluator extends StatisticsEvaluator {
    /**
     * Constructs a new CargoDecompositionEvaluator.
     */
    public CargoDecompositionEvaluator() {
        super();
    }

    /**
     * Returns the distribution of which cargo types that have entered the port.
     *
     * @return cargo distribution map
     */
    public Map<String, Integer> getCargoDistribution() {
        return null;
    }

    /**
     * Returns the distribution of bulk cargo types that have entered the port.
     *
     * @return bulk cargo distribution map
     */
    public Map<BulkCargoType, Integer> getBulkCargoDistribution() {
        return null;
    }

    /**
     * Returns the distribution of container types that have entered the port.
     *
     * @return container distribution map
     */
    public Map<ContainerType, Integer> getContainerDistribution() {
        return null;
    }

    /**
     * Updates the internal distributions of cargo types using the given
     * movement.
     * <p>
     * If the movement is not an {@code INBOUND} movement, this method returns
     * immediately without taking any action.
     * <p>
     * If the movement is an {@code INBOUND} movement, do the following:
     * <p>
     * <ul>
     *     <li>
     *         If the movement is a ShipMovement, Retrieve the cargo from the
     *         ships and for each piece of cargo:
     *         <ol>
     *             <li>
     *                 If the cargo class (Container / BulkCargo) has been seen
     *                 before (simple name exists as a key in the cargo map) ->
     *                 increment that number
     *             </li>
     *             <li>
     *                 If the cargo class has not been seen before then add its
     *                 class simple name as a key in the map with a
     *                 corresponding value of 1
     *             </li>
     *             <li>
     *                 If the cargo type (Value of ContainerType /
     *                 BulkCargoType) for the given cargo class has been seen
     *                 before (exists as a key in the map) increment that number
     *             </li>
     *             <li>
     *                 If the cargo type (Value of ContainerType /
     *                 BulkCargoType) for the given cargo class has not been
     *                 seen before add as a key in the map with a
     *                 corresponding value of 1
     *             </li>
     *     </ol>
     *     </li>
     *     <li>
     *         <ol>
     *             If the movement is a CargoMovement, Retrieve the cargo
     *             from the movement. For the cargo retrieved:
     *             <li>
     *                 Complete steps 1-4 as given above for ShipMovement
     *             </li>
     *         </ol>
     *     </li>
     * </ul>
     *
     * @param movement movement to read
     */
    public void onProcessMovement(Movement movement) {}
}
