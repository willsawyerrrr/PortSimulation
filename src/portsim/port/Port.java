package portsim.port;

import portsim.cargo.Cargo;
import portsim.evaluators.*;
import portsim.movement.CargoMovement;
import portsim.movement.Movement;
import portsim.movement.ShipMovement;
import portsim.ship.BulkCarrier;
import portsim.ship.ContainerShip;
import portsim.ship.Ship;
import portsim.util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Stream;

/**
 * A place where ships can come and dock with Quays to load / unload their
 * cargo.
 * <p>
 * Ships can enter a port through its queue. Cargo is stored within the port
 * at warehouses.
 */
public class Port implements Tickable, Encodable {
    /**
     * The name of this port used for identification
     */
    private String name;
    /**
     * The time since the simulation started running
     */
    private long time;
    /**
     * The queue of ships waiting to dock at the port
     */
    private ShipQueue shipQueue;
    /**
     * The quays associated with this port
     */
    private List<Quay> quays;
    /**
     * The cargo currently stored at the port at warehouses. Cargo unloaded
     * from trucks / ships
     */
    private List<Cargo> storedCargo;
    /**
     * The statistics evaluators associated with this port
     */
    private List<StatisticsEvaluator> evaluators;
    /**
     * The queue of movements ordered by the time of the movement
     */
    private PriorityQueue<Movement> movements;

    /**
     * Creates a new port with the given name.
     * <p>
     * The time since the simulation was started should be initialised as 0.
     * <p>
     * The list of quays in the port, stored cargo (warehouses) and statistics
     * evaluators should be initialised as empty lists.
     * <p>
     * An empty ShipQueue should be initialised, and a PriorityQueue should be
     * initialised to store movements ordered by the time of the movement (see
     * {@link Movement#getTime()}).
     *
     * @param name name of the port
     */
    public Port(String name) {
        this.name = name;
        this.time = 0;
        this.shipQueue = new ShipQueue();
        this.quays = new ArrayList<>();
        this.storedCargo = new ArrayList<>();
        this.evaluators = new ArrayList<>();
        this.movements =
                new PriorityQueue<>(Comparator.comparing(Movement::getTime));
    }

    /**
     * Creates a new port with the given name, time elapsed, ship queue,
     * quays and stored cargo.
     * <p>
     * The list of statistics evaluators should be initialised as an empty list.
     * <p>
     * A PriorityQueue should be initialised to store movements ordered by
     * the time of the movement (see {@link Movement#getTime()}).
     *
     * @param name        name of the port
     * @param time        number of minutes since simulation started
     * @param shipQueue   ships waiting to enter the port
     * @param quays       the port's quays
     * @param storedCargo the cargo stored at the port
     *
     * @throws IllegalArgumentException if time &lt; 0
     */
    public Port(String name, long time, ShipQueue shipQueue, List<Quay> quays,
                List<Cargo> storedCargo) throws IllegalArgumentException {
        if (time < 0) {
            throw new IllegalArgumentException("Time must be greater than or "
                    + "equal to 0: " + time);
        }
        this.name = name;
        this.time = time;
        this.shipQueue = shipQueue;
        this.quays = quays;
        this.storedCargo = storedCargo;
        this.evaluators = new ArrayList<>();
        this.movements =
                new PriorityQueue<>(Comparator.comparing(Movement::getTime));
    }

    /**
     * Creates a port instance by reading various ship, quay, cargo, movement
     * and evaluator entities from the given reader.
     * <p>
     * The provided file should be in the format:
     * <p>
     * <pre>Name</pre><br>
     * <pre>Time</pre><br>
     * <pre>numCargo</pre><br>
     * <pre>EncodedCargo</pre><br>
     * <pre>EncodedCargo...</pre><br>
     * <pre>numShips</pre><br>
     * <pre>EncodedShip</pre><br>
     * <pre>EncodedShip...</pre><br>
     * <pre>numQuays</pre><br>
     * <pre>EncodedQuay</pre><br>
     * <pre>EncodedQuay...</pre><br>
     * <pre>ShipQueue:numShipsInQueue:shipID,shipID,...</pre><br>
     * <pre>StoredCargo:numCargo:cargoID,cargoID,...</pre><br>
     * <pre>Movements:numMovements</pre><br>
     * <pre>EncodedMovement</pre><br>
     * <pre>EncodedMovement...</pre><br>
     * <pre>Evaluators:numEvaluators:EvaluatorSimpleName,
     * EvaluatorSimpleName,...</pre><br>
     * <p>
     * As specified by {@link #encode()}
     * <p>
     * The encoded string is invalid if any of the following conditions are
     * true:
     * <ul>
     *     <li>The time is not a valid long (i.e. cannot be parsed by
     *     <pre>Long.parseLong(String)</pre>).</li>
     *     <li>The number of cargo is not an integer (i.e. cannot be parsed
     *     by <pre>Integer.parseInt(String)</pre>).</li>
     *     <li>The number of cargo to be read in does not match the number
     *     specified above. (ie. too many / few encoded cargo following
     *     the number).</li>
     *     <li>An encoded cargo line throws a {@link BadEncodingException}</li>
     *     <li>The number of ships is not an integer (i.e. cannot be parsed
     *     by <pre>Integer.parseInt(String)</pre>).</li>
     *     <li>The number of ship to be read in does not match the number
     *     specified above. (ie. too many / few encoded ships following the
     *     number).</li>
     *     <li>An encoded ship line throws a {@link BadEncodingException}.</li>
     *     <li>The number of quays is not an integer (i.e. cannot be parsed by
     *     <pre>Integer.parseInt(String)</pre>).</li>
     *     <li>The number of quays to be read in does not match the number
     *     specified above. (ie. too many / few encoded quays following the
     *     number).</li>
     *     <li>An encoded quay line throws a {@link BadEncodingException}.</li>
     *     <li>The shipQueue does not follow the last encoded quay.</li>
     *     <li>The number of ships in the shipQueue is not an integer (i.e.
     *     cannot be parsed by <pre>Integer.parseInt(String)</pre>).</li>
     *     <li>The imoNumber of the ships in the shipQueue are not valid
     *     longs (i.e. cannot be parsed by
     *     <pre>Long.parseLong(String)</pre>).</li>
     *     <li>Any imoNumber read does not correspond to a valid ship in the
     *     simulation.</li>
     *     <li>The storedCargo does not follow the encoded shipQueue.</li>
     *     <li>The number of cargo in the storedCargo is not an integer (i.e.
     *     cannot be parsed by <pre>Integer.parseInt(String)</pre>).</li>
     *     <li>The id of the cargo in the storedCargo are not valid Integers.
     *     (i.e. cannot be parsed by <pre>Integer.parseInt(String)</pre>).</li>
     *     <li>Any cargo id read does not correspond to a valid cargo in the
     *     simulation.</li>
     *     <li>The movements do not follow the encoded storedCargo.</li>
     *     <li>The number of movements is not an integer (i.e. cannot be
     *     parsed by <pre>Integer.parseInt(String)</pre>).</li>
     *     <li>The number of movements to be read in does not match the
     *     number specified above. (ie. too many / few encoded movements
     *     following the number).</li>
     *     <li>An encoded movement line throws a
     *     {@link BadEncodingException}.</li>
     *     <li>The evaluators fo not follow the encoded movements.</li>
     *     <li>The number of evaluators is not an integer (i.e. cannot be
     *     parsed by <pre>Integer.parseInt(String)</pre>).</li>
     *     <li>The number of evaluators to be read in does not match the
     *     number specified above. (ie. too many / few encoded evaluators
     *     following the number).</li>
     *     <li>An encoded evaluator name does not match any of the possible
     *     evaluator classes</li>
     *     <li>If any of the following lines are missing:
     *         <ol>
     *             <li>Name</li>
     *             <li>Time</li>
     *             <li>Number of Cargo</li>
     *             <li>Number of Ships</li>
     *             <li>Number of Quays</li>
     *             <li>ShipQueue</li>
     *             <li>StoredCargo</li>
     *             <li>Movements</li>
     *             <li>Evaluators</li>
     *         </ol>
     *     </li>
     * </ul>
     *
     * @param reader reader from which to load all info
     *
     * @return port created by reading from given reader
     *
     * @throws IOException if an IOException is encountered when
     *                     reading from the reader
     * @throws BadEncodingException if the reader reads a line that
     *                              does not adhere to the rules above
     *                              indicating that the contents of the
     *                              reader are invalid
     */
    public static Port initialisePort(Reader reader)
            throws IOException, BadEncodingException {
        String name;
        long time;
        int numCargo;
        int numShips;
        int numQuays;
        int numMovements;
        int numEvaluators;
        ShipQueue shipQueue = new ShipQueue();
        List<Quay> quays = new ArrayList<>();
        List<Cargo> storedCargo = new ArrayList<>();
        Port port;
        try (BufferedReader br = new BufferedReader(reader)) {
            name = br.readLine();
            time = Long.parseLong(br.readLine());
            numCargo = Integer.parseInt(br.readLine());
            for (int i = 1; i <= numCargo; i++) {
                storedCargo.add(Cargo.fromString(br.readLine()));
            }
            numShips = Integer.parseInt(br.readLine());
            for (int i = 1; i <= numShips; i++) {
                Ship.fromString(br.readLine());
            }
            numQuays = Integer.parseInt(br.readLine());
            for (int i = 1; i <= numQuays; i++) {
                quays.add(Quay.fromString(br.readLine()));
            }

            String queueLine = br.readLine();
            String[] splitQueueLine = queueLine.split(":");
            if (numColons(queueLine) != 2 || queueLine.endsWith(",")) {
                throw new BadEncodingException();
            }
            int numShipsInQueue = Integer.parseInt(splitQueueLine[1]);
            if (numShipsInQueue != 0) {
                shipQueueHelper(splitQueueLine[2], numShipsInQueue).forEach(
                        shipQueue::add);
            }

            String storedCargoLine = br.readLine();
            String[] splitStoredCargoLine = storedCargoLine.split(":");
            if (storedCargoLine.endsWith(",")
                    || numColons(storedCargoLine) != 2) {
                throw new BadEncodingException();
            }
            int numStoredCargo = Integer.parseInt(splitStoredCargoLine[1]);
            if (numStoredCargo != 0) {
                storedCargo.addAll(storedCargoHelper(splitStoredCargoLine[2],
                                numStoredCargo));
            }

            String movementLine = br.readLine();
            if (movementLine.endsWith(",") || numColons(movementLine) != 1) {
                throw new BadEncodingException();
            }
            numMovements = Integer.parseInt(movementLine.split(":")[1]);
            for (int i = 1; i <= numMovements; i++) {
                String line = br.readLine();
                if (line.startsWith("Cargo")) {
                    CargoMovement.fromString(line);
                } else if (line.startsWith("Ship")) {
                    ShipMovement.fromString(line);
                } else {
                    throw new BadEncodingException();
                }
            }
            port = new Port(name, time, shipQueue, quays, storedCargo);

            String evaluatorLine = br.readLine();
            String[] evaluators = evaluatorLine.split(":");
            if (numColons(evaluatorLine) != 2 || evaluatorLine.endsWith(",")) {
                throw new BadEncodingException();
            }
            numEvaluators = Integer.parseInt(evaluators[1]);
            if (numEvaluators != 0) {
                for (int i = 2; i <= numEvaluators + 1; i++) {
                    switch (evaluators[i]) {
                        case "CargoDecompositionEvaluator":
                            port.addStatisticsEvaluator(
                                    new CargoDecompositionEvaluator());
                            break;
                        case "QuayOccupancyEvaluator":
                            port.addStatisticsEvaluator(
                                    new QuayOccupancyEvaluator(port));
                            break;
                        case "ShipFlagEvaluator":
                            port.addStatisticsEvaluator(
                                    new ShipFlagEvaluator());
                            break;
                        case "ShipThroughputEvaluator":
                            port.addStatisticsEvaluator(
                                    new ShipThroughputEvaluator());
                            break;
                        default:
                            throw new BadEncodingException();
                    }
                }
            }
        } catch (BadEncodingException | ArrayIndexOutOfBoundsException ignore) {
            throw new BadEncodingException();
        }
        reader.close();
        return port;
    }

    private static long numColons(String line) {
        Stream<String> colons =
                Stream.of(line).filter(character -> character.equals(":"));
        return colons.count();
    }

    /**
     * Helper method called by {@link Port#initialisePort(Reader)} to get
     * appropriate ships to add to ShipQueue.
     *
     * @param ids comma-separated IMO Numbers of ships to be added to the queue
     * @param expected the number of ships which are expected to be added
     *
     * @return list of ships to add the queue
     *
     * @throws BadEncodingException if the number of IMO Numbers is not equal
     *                              to the expected number or one of the
     *                              given IMO Numbers does not correspond to
     *                              an existing ship
     */
    private static List<Ship> shipQueueHelper(String ids, int expected)
            throws BadEncodingException {
        List<Ship> queue = new ArrayList<>();
        String[] splitIds = ids.split(",");
        if (splitIds.length != expected) {
            throw new BadEncodingException();
        }
        try {
            for (String id : splitIds) {
                long imoNumber = Long.parseLong(id);
                queue.add(Ship.getShipByImoNumber(imoNumber));
            }
        } catch (NoSuchShipException ignored) {
            throw new BadEncodingException();
        }
        return queue;
    }

    /**
     * Helper method called by {@link Port#initialisePort(Reader)} to get
     * appropriate cargo to add to stored cargo.
     *
     * @param ids comma-separated IDs of cargo to be added to the queue
     * @param expected the number of cargo which are expected to be added
     *
     * @return list of cargo to add the stored cargo
     *
     * @throws BadEncodingException if the number of IDs is not equal to the
     *                              expected number or one of the given IDs
     *                              does not correspond to existing cargo
     */
    private static List<Cargo> storedCargoHelper(String ids, int expected)
            throws BadEncodingException {
        List<Cargo> cargo = new ArrayList<>();
        String[] splitIds = ids.split(",");
        if (splitIds.length != expected) {
            throw new BadEncodingException();
        }
        try {
            for (String id : splitIds) {
                int parsedId = Integer.parseInt(id);
                cargo.add(Cargo.getCargoById(parsedId));
            }
        } catch (NoSuchCargoException ignored) {
            throw new BadEncodingException();
        }
        return cargo;
    }

    /**
     * Adds a movement to the PriorityQueue of movements.
     * <p>
     * If the given movement's action time is less than the current number of
     * minutes elapsed than an <pre>IllegalArgumentException</pre> should be
     * thrown.
     *
     * @param movement movement to add
     *
     * @throws IllegalArgumentException if the movement's action time has
     *                                  already passed
     */
    public void addMovement(Movement movement) throws IllegalArgumentException {
        if (movement.getTime() < time) {
            throw new IllegalArgumentException("This movement should have "
                    + "already occurred.");
        }
        movements.add(movement);
    }

    /**
     * Processes a movement.
     * <p>
     * The action taken depends on the type of movement to be processed.
     * <p>
     * If the movement is a ShipMovement:
     * <ul>
     *     <li>If the movement direction is INBOUND then the ship should be
     *     added to the ship queue.</li>
     *     <li>If the movement direction is OUTBOUND then any cargo stored in
     *     the port whose destination is the ship's origin port should be
     *     added to the ship according to
     *     {@link portsim.ship.Ship#canLoad(Cargo)}. Next, the ship should be
     *     removed from the quay it is currently docked in (if any).</li>
     * </ul>
     * <p>
     * If the movement is a CargoMovement:
     * <ul>
     *     <li>If the movement direction is INBOUND then all of the cargo
     *     that is being moved should be added to the port's stored cargo.</li>
     *     <li>If the movement direction is OUTBOUND then all cargo with the
     *     given IDs should be removed from the port's stored cargo.</li>
     * </ul>
     * <p>
     * Finally, the movement should be forwarded onto each statistics evaluator
     * stored by the port by calling
     * {@link StatisticsEvaluator#onProcessMovement(Movement)}.
     *
     * @param movement movement to execute
     */
    public void processMovement(Movement movement) {
        if (movement instanceof ShipMovement) {
            ShipMovement shipMovement = (ShipMovement) movement;
            switch (shipMovement.getDirection()) {
                case INBOUND:
                    shipQueue.add(shipMovement.getShip());
                    break;
                case OUTBOUND:
                    Ship ship = shipMovement.getShip();
                    // Load appropriate cargo to the ship and remove it from
                    // the warehouse
                    Stream<Cargo> involvedCargo =
                            storedCargo.stream().filter(ship::canLoad);
                    involvedCargo.forEach(ship::loadCargo);
                    involvedCargo.forEach(cargo -> storedCargo.remove(cargo));
                    // Departs ship from its quay
                    quays.stream().filter(quay ->
                            quay.getShip().equals(shipMovement.getShip()))
                            .forEach(Quay::shipDeparts);
                    break;
            }
        } else if (movement instanceof CargoMovement) {
            CargoMovement cargoMovement = (CargoMovement) movement;
            switch (cargoMovement.getDirection()) {
                case INBOUND:
                    storedCargo.addAll(cargoMovement.getCargo());
                    break;
                case OUTBOUND:
                    cargoMovement.getCargo().forEach(moving ->
                            storedCargo.removeIf(
                                stored -> stored.getId() == moving.getId()));
            }
        }

        evaluators.forEach(eval -> eval.onProcessMovement(movement));
    }

    /**
     * Adds the given statistics evaluator to the port's list of evaluators.
     * <p>
     * If the port already has an evaluator of that type, no action should be
     * taken.
     *
     * @param eval statistics evaluator to add to the port
     */
    public void addStatisticsEvaluator(StatisticsEvaluator eval) {
        if (evaluators.stream().noneMatch(existing ->
                eval.getClass().equals(existing.getClass()))) {
            evaluators.add(eval);
        }
    }

    /**
     * Returns the name of this port.
     *
     * @return port's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the time since simulation started.
     *
     * @return time in minutes
     */
    public long getTime() {
        return time;
    }

    /**
     * Returns a list of all quays associated with this port.
     * <p>
     * Adding or removing elements from the returned list should not affect the
     * original list.
     * <p>
     * The order in which quays appear in this list should be the same as the
     * order in which they were added by calling {@link #addQuay(Quay)}.
     *
     * @return all quays
     */
    public List<Quay> getQuays() {
        return new ArrayList<>(this.quays);
    }

    /**
     * Returns the cargo stored in warehouses at this port.
     * <p>
     * Adding or removing elements from the returned list should not affect the
     * original list.
     *
     * @return port cargo
     */
    public List<Cargo> getCargo() {
        return new ArrayList<>(storedCargo);
    }

    /**
     * Returns the queue of ships waiting to be docked at this port.
     *
     * @return port's queue of ships
     */
    public ShipQueue getShipQueue() {
        return shipQueue;
    }

    /**
     * Returns the queue of movements waiting to be processed.
     *
     * @return movements queue
     */
    public PriorityQueue<Movement> getMovements() {
        return movements;
    }

    /**
     * Returns the list of evaluators at the port.
     * <p>
     * Adding or removing elements from the returned list should not affect the
     * original list.
     *
     * @return the ports evaluators
     */
    public List<StatisticsEvaluator> getEvaluators() {
        return new ArrayList<>(evaluators);
    }

    /**
     * Adds a quay to the ports control.
     *
     * @param quay the quay to add
     */
    public void addQuay(Quay quay) {
        this.quays.add(quay);
    }

    /**
     * Advances the simulation by one minute.
     * <p>
     * On each call to <pre>elapseOneMinute()</pre>, the following actions
     * should be completed by the port in order:
     * <p>
     * <ol>
     *     <li>Advance the simulation time by 1.</li>
     *     <li>If the time is a multiple of 10, attempt to bring a ship from
     *     the ship queue to any empty quay that matches the requirements from
     *     {@link portsim.ship.Ship#canDock(Quay)}. The ship should only be
     *     docked to one quay.</li>
     *     <li>If the time is a multiple of 5, all quays must unload the cargo
     *     from ships docked (if any) and add it to warehouses at the port (the
     *     Port's list of stored cargo).</li>
     *     <li>All movements stored in the queue whose action time is equal to
     *     the current time should be processed by
     *     {@link #processMovement(Movement)}.</li>
     *     <li>Call {@link StatisticsEvaluator#elapseOneMinute()} on all
     *     statistics evaluators</li>
     * </ol>
     */
    @Override
    public void elapseOneMinute() {
        time++;

        if (time % 10 == 0) {
            for (Quay quay : quays) {
                if (quay.isEmpty() && shipQueue.peek().canDock(quay)) {
                    quay.shipArrives(shipQueue.poll());
                }
            }
        }

        if (time % 5 == 0) {
            for (Quay quay : quays) {
                if (!quay.isEmpty()
                        && quay.getShip() instanceof BulkCarrier) {
                    BulkCarrier ship = (BulkCarrier) quay.getShip();
                    try {
                        storedCargo.add(ship.unloadCargo());
                    } catch (NoSuchCargoException ignored) {
                        // do nothing
                    }
                } else if (!quay.isEmpty()
                        && quay.getShip() instanceof ContainerShip) {
                    ContainerShip ship = (ContainerShip) quay.getShip();
                    try {
                        storedCargo.addAll(ship.unloadCargo());
                    } catch (NoSuchCargoException ignored) {
                        // do nothing
                    }
                }
            }
        }

        for (Movement movement : movements) {
            if (movement.getTime() == time) {
                this.processMovement(movement);
                movements.remove(movement);
            }
        }

        evaluators.forEach(eval -> eval.elapseOneMinute());
    }

    /**
     * Returns the machine-readable string representation of this Port.
     * <p>
     * The format of the string to return is
     * <pre>Name</pre><br>
     * <pre>Time</pre><br>
     * <pre>numCargo</pre><br>
     * <pre>EncodedCargo</pre><br>
     * <pre>EncodedCargo...</pre><br>
     * <pre>numShips</pre><br>
     * <pre>EncodedShip</pre><br>
     * <pre>EncodedShip...</pre><br>
     * <pre>numQuays</pre><br>
     * <pre>EncodedQuay</pre><br>
     * <pre>EncodedQuay...</pre><br>
     * <pre>ShipQueue:numShipsInQueue:shipID,shipID,...</pre><br>
     * <pre>StoredCargo:numCargo:cargoID,cargoID,...</pre><br>
     * <pre>Movements:numMovements</pre><br>
     * <pre>EncodedMovement</pre><br>
     * <pre>EncodedMovement...</pre><br>
     * <pre>Evaluators:numEvaluators:EvaluatorSimpleName,
     * EvaluatorSimpleName,...</pre><br>
     * <p>
     * For example the minimum / default encoding would be:
     * <pre>PortName</pre><br>
     * <pre>0</pre><br>
     * <pre>0</pre><br>
     * <pre>0</pre><br>
     * <pre>0</pre><br>
     * <pre>ShipQueue:0:</pre><br>
     * <pre>StoredCargo:0:</pre><br>
     * <pre>Movements:0</pre><br>
     * <pre>Evaluators:0:</pre>
     *
     * @return encoded string representation of this Port
     */
    @Override
    public String encode() {
        List<Ship> ships = new ArrayList<>(shipQueue.getShipQueue());
        for (Quay quay : quays) {
            if (!quay.isEmpty()) {
                ships.add(quay.getShip());
            }
        }

        List<Cargo> allCargo = new ArrayList<>(getCargo());
        for (Ship ship : ships) {
            if (ship instanceof BulkCarrier) {
                BulkCarrier carrier = (BulkCarrier) ship;
                if (carrier.getCargo() != null) {
                    allCargo.add(carrier.getCargo());
                }
            } else if (ship instanceof ContainerShip) {
                ContainerShip containerShip = (ContainerShip) ship;
                if (containerShip.getCargo() != null) {
                    allCargo.addAll(containerShip.getCargo());
                }
            }
        }

        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(name);
        joiner.add(Long.toString(time));

        joiner.add(Integer.toString(allCargo.size()));
        if (allCargo.size() > 0) {
            allCargo.forEach(cargo -> joiner.add(cargo.encode()));
        }

        joiner.add(Integer.toString(ships.size()));
        joiner.add("\n");
        if (ships.size() > 0) {
            ships.forEach(ship -> joiner.add(ship.encode()));
        }

        joiner.add(Integer.toString(quays.size()));
        if (quays.size() > 0) {
            quays.forEach(quay -> joiner.add(quay.encode()));
        }

        List<Ship> queue = shipQueue.getShipQueue();
        StringJoiner queueJoiner = new StringJoiner(":");
        queueJoiner.add("ShipQueue");
        queueJoiner.add(Integer.toString(queue.size()));
        if (queue.size() > 0) {
            StringJoiner internalQueueJoiner = new StringJoiner(",");
            queue.forEach(ship -> internalQueueJoiner.add(
                    Long.toString(ship.getImoNumber())));
            queueJoiner.merge(internalQueueJoiner);
        }
        joiner.merge(queueJoiner);

        StringJoiner cargoJoiner = new StringJoiner(":");
        cargoJoiner.add("StoredCargo");
        cargoJoiner.add(Integer.toString(storedCargo.size()));
        if (storedCargo.size() > 0) {
            StringJoiner internalCargoJoiner = new StringJoiner(",");
            storedCargo.forEach(cargo -> internalCargoJoiner.add(
                    Integer.toString(cargo.getId())));
            cargoJoiner.merge(internalCargoJoiner);
        }
        joiner.merge(cargoJoiner);

        joiner.add(String.format("Movements:%d", movements.size()));
        joiner.add(Integer.toString(movements.size()));
        if (movements.size() > 0) {
            movements.forEach(movement -> joiner.add(movement.encode()));
        }

        StringJoiner evalJoiner = new StringJoiner(":");
        evalJoiner.add("Evaluators");
        evalJoiner.add(Integer.toString(evaluators.size()));
        if (evaluators.size() > 0) {
            StringJoiner internalEvalJoiner = new StringJoiner(",");
            evaluators.forEach(eval -> joiner.add(
                    eval.getClass().getSimpleName()));
            evalJoiner.merge(internalEvalJoiner);
        }
        joiner.merge(evalJoiner);

        return joiner.toString();
    }
}
