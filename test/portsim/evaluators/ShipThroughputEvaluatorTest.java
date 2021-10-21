package portsim.evaluators;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import portsim.cargo.*;
import portsim.movement.CargoMovement;
import portsim.movement.Movement;
import portsim.movement.MovementDirection;
import portsim.movement.ShipMovement;
import portsim.ship.BulkCarrier;
import portsim.ship.NauticalFlag;
import portsim.ship.Ship;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShipThroughputEvaluatorTest {
    ShipThroughputEvaluator eval;
    BulkCarrier carrier;
    List<Cargo> cargo;
    Cargo bulkCargo1;
    Cargo bulkCargo2;
    Cargo container1;
    Cargo container2;

    @Before
    public void setUp() {
        eval = new ShipThroughputEvaluator();
        carrier = new BulkCarrier(1234567, "Alpha", "Australia",
                NauticalFlag.WHISKEY, 15);
        cargo = new ArrayList<>();
        bulkCargo1 = new BulkCargo(1, "Australia", 80, BulkCargoType.GRAIN);
        bulkCargo2 = new BulkCargo(2, "China", 100, BulkCargoType.MINERALS);
        container1 = new Container(3, "New Zealand", ContainerType.STANDARD);
        container2 = new Container(4, "America", ContainerType.OPEN_TOP);
    }

    @After
    public void tearDown() {
        eval = null;
        Ship.resetShipRegistry();
        Cargo.resetCargoRegistry();
    }

    @Test
    public void constructorTest() {
        assertEquals(0, eval.getThroughputPerHour());
        assertEquals(0, eval.getTime());
    }

    @Test
    public void processValidMovementTest() {
        Movement validMovement = new ShipMovement(5,
                MovementDirection.OUTBOUND, carrier);

        assertEquals(0, eval.getThroughputPerHour());
        eval.onProcessMovement(validMovement);
        assertEquals(1, eval.getThroughputPerHour());
    }

    @Test
    public void processInvalidCargoMovementTest() {
        List<Cargo> cargo = new ArrayList<>();
        cargo.add(bulkCargo1);
        cargo.add(bulkCargo2);
        cargo.add(container1);
        cargo.add(container2);

        Movement invalidMovement = new CargoMovement(3,
                MovementDirection.OUTBOUND, cargo);

        assertEquals(0, eval.getThroughputPerHour());
        eval.onProcessMovement(invalidMovement);
        assertEquals(0, eval.getThroughputPerHour());
    }

    @Test
    public void processInvalidInboundMovementTest() {
        Movement invalidMovement = new ShipMovement(3,
                MovementDirection.INBOUND, carrier);

        assertEquals(0, eval.getThroughputPerHour());
        eval.onProcessMovement(invalidMovement);
        assertEquals(0, eval.getThroughputPerHour());
    }

    @Test
    public void removeMovementAfter60MinutesTest() {
        Movement validMovement = new ShipMovement(3,
                MovementDirection.OUTBOUND, carrier);

        while (eval.getTime() < 65) {
            eval.elapseOneMinute();
            switch (eval.getTime()) {
                case 2:
                    assertEquals(0, eval.getThroughputPerHour());
                    break;
                case 3:
                    eval.onProcessMovement(validMovement);
                    assertEquals(1, eval.getThroughputPerHour());
                    break;
                case 62:
                    assertEquals(1, eval.getThroughputPerHour());
                    break;
                case 63:
                    assertEquals(1, eval.getThroughputPerHour());
                    break;
                case 64:
                    assertEquals(0, eval.getThroughputPerHour());
            }
        }
    }
}