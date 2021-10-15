package portsim.evaluators;

import portsim.cargo.*;
import portsim.movement.CargoMovement;
import portsim.movement.Movement;
import portsim.movement.MovementDirection;
import portsim.movement.ShipMovement;
import portsim.ship.BulkCarrier;
import portsim.ship.ContainerShip;
import portsim.ship.Ship;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collects data on what types of cargo are passing through the port. Gathers
 * data on all derivatives of the cargo class.
 * <p>
 * The data gathered is a count of how many times each type of cargo has entered
 * the port. This includes a count of how many times the port has received
 * "BulkCargo" or "Container" class cargo. As well as a count of how many times
 * the port has seen each cargo subclass type
 * ({@link portsim.cargo.ContainerType} and
 * {@link portsim.cargo.BulkCargoType}).
 */
public class CargoDecompositionEvaluator extends StatisticsEvaluator {
    /**
     * Record of cargo seen at this port
     */
    private Map<String, Integer> cargoStats;

    /**
     * Record of bulk cargo seen at this port
     */
    private Map<BulkCargoType, Integer> bulkCargoStats;

    /**
     * Record of containers seen at this port
     */
    private Map<ContainerType, Integer> containerStats;

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
        return new HashMap<>(cargoStats);
    }

    /**
     * Returns the distribution of bulk cargo types that have entered the port.
     *
     * @return bulk cargo distribution map
     */
    public Map<BulkCargoType, Integer> getBulkCargoDistribution() {
        return new HashMap<>(bulkCargoStats);
    }

    /**
     * Returns the distribution of container types that have entered the port.
     *
     * @return container distribution map
     */
    public Map<ContainerType, Integer> getContainerDistribution() {
        return new HashMap<>(containerStats);
    }

    /**
     * Updates the internal distributions of cargo types using the given
     * movement.
     * <p>
     * If the movement is not an <pre>INBOUND</pre> movement, this method 
     * returns immediately without taking any action.
     * <p>
     * If the movement is an {@code INBOUND} movement, do the following:
     * <ul>
     *     <li>If the movement is a ShipMovement, Retrieve the cargo from the
     *     ships and for each piece of cargo:
     *         <ol>
     *             <li>If the cargo class (Container / BulkCargo) has been seen
     *             before (simple name exists as a key in the cargo map) -&gt;
     *             increment that number</li>
     *             <li>If the cargo class has not been seen before then add its
     *             class simple name as a key in the map with a corresponding
     *             value of 1 </li>
     *             <li>If the cargo type (Value of ContainerType / 
     *             BulkCargoType) for the given cargo class has been seen 
     *             before (exists as a key in the map) increment that
     *             number</li>
     *             <li>If the cargo type (Value of ContainerType / 
     *             BulkCargoType) for the given cargo class has not been seen
     *             before add as a key in the map with a corresponding value 
     *             of 1</li>
     *         </ol>
     *     </li>
     *     <li>If the movement is a CargoMovement, Retrieve the cargo from 
     *     the movement. For the cargo retrieved:
     *         <ol>
     *             <li>Complete steps 1-4 as given above for ShipMovement</li>
     *         </ol>
     *     </li>
     * </ul>
     *
     * @param movement movement to read
     */
    @Override
    public void onProcessMovement(Movement movement) {
        if (movement.getDirection() == MovementDirection.INBOUND
                && movement instanceof ShipMovement) {
            Ship ship = ((ShipMovement) movement).getShip();

            if (ship instanceof BulkCarrier) {
                BulkCargo bulkCargo = ((BulkCarrier) ship).getCargo();

                updateValue(cargoStats, bulkCargo.getClass().getSimpleName());
                updateValue(bulkCargoStats, bulkCargo.getType());
            } else if (ship instanceof ContainerShip) {
                List<Container> containers = ((ContainerShip) ship).getCargo();

                for (Container con : containers) {
                    updateValue(cargoStats, con.getClass().getSimpleName());
                    updateValue(containerStats, con.getType());
                }
            }
        } else if (movement.getDirection() == MovementDirection.INBOUND
                && movement instanceof CargoMovement) {
            CargoMovement cargoMovement = (CargoMovement) movement;
            List<Cargo> cargo = cargoMovement.getCargo();

            for (Cargo piece : cargo) {
                updateValue(cargoStats, piece.getClass().getSimpleName());

                if (cargo instanceof BulkCargo) {
                    updateValue(bulkCargoStats, ((BulkCargo) cargo).getType());
                } else if (cargo instanceof Container) {
                    updateValue(containerStats, ((Container) cargo).getType());
                }
            }
        }
    }

    /**
     * Increments the value mapped to by the given key, if it exists.
     * Otherwise, creates a new map entry with value of 1.
     *
     * @param statistics cargo seen at this port
     * @param key cargo class
     */
    private void updateValue(Map<String, Integer> statistics,
                             String key) {
        if (statistics.containsKey(key)) {
            Integer old = statistics.get(key);
            statistics.replace(key, old + 1);
        } else {
            statistics.put(key, 1);
        }
    }

    /**
     * Increments the value mapped to by the given key, if it exists. Otherwise,
     * creates a new map entry with value of 1.
     *
     * @param statistics cargo seen at this port
     * @param key type of bulk cargo
     */
    private void updateValue(Map<BulkCargoType, Integer> statistics,
                             BulkCargoType key) {
        if (statistics.containsKey(key)) {
            Integer old = statistics.get(key);
            statistics.replace(key, old + 1);
        } else {
            statistics.put(key, 1);
        }
    }

    /**
     * Increments the value mapped to by the given key, if it exists. Otherwise,
     * creates a new map entry with value of 1.
     *
     * @param statistics cargo seen at this port
     * @param key type of container
     */
    private void updateValue(Map<ContainerType, Integer> statistics,
                             ContainerType key) {
        if (statistics.containsKey(key)) {
            Integer old = statistics.get(key);
            statistics.replace(key, old + 1);
        } else {
            statistics.put(key, 1);
        }
    }
}
