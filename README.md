# Shipping Port Simulation

## Assignments 1 and 2 for CSSE2002, Semester 2 2021

### Assignment 1
In this assignment, and continuing into the second assignment, you will build a simple simulation of a cargo port system. The first assignment will focus on implementing the classes that provide the core model for the system.

A cargo port system manages the movement of ships around a port, including loading and unloading ships at quays, storing cargo at the port temporarily, and managing a queue of ships waiting offshore to dock at the port. The system also keeps track of statistics relating to the port’s operations (more about this in assignment 2).

A port is made up of several quays, which are designed to accommodate specific types of ships. Ships are separated into two types: container ships and bulk carriers. Container ships carry shipping containers of various types, while bulk carriers are designed to hold large quantities of bulk cargo, such as grain, minerals, or oil.

Each ship has various characteristics which are known to the port system, including the ship’s name, unique International Maritime Organisation (IMO) number, country of origin, nautical flag, and cargo capacity. These characteristics determine whether the ship is able to dock at each of the port’s quays, which cargo can be loaded onto the ship, and whether the ship will be prioritised in the queue of ships waiting to dock.

The port system keeps track of all cargo that enters and exits the port. Each piece of cargo is identified by a unique identifer, and has a destination associated with it. Cargo is classified as either a shipping container or bulk cargo.

To represent the actions of ships and cargo moving into and out of the port, the port system uses Movement classes, specifically ShipMovement and CargoMovement. These movements store the direction of the movement (inbound or outbound), the time at which it is occurring and the cargo/ship that is being moved.

An inbound cargo movement represents cargo being delivered at the port by land vehicles (trains, trucks, etc.) to be loaded onto ships. An outbound cargo movement represents cargo being removed from storage at the port and picked up by land vehicles. An inbound ship movement represents a ship arriving in the waters nearby the port and wishing to dock. An outbound ship movement represents a ship departing from its quay and leaving the port.

### Assignment 2
In this assignment, you will continue developing a simple simulation of a cargo port system, building on the core model of the system you implemented in assignment one.

In assignment two, additional logic is added to the Port class to process the movements that arequeued to be executed. Movements will now be actioned by the port when their action time is reached, moving cargo and ships in and out of the port.

The concept of time is introduced to the simulation. One tick in the simulation represents a minute in real life. The Tickable interface allows classes to specify behaviour that occurs on each simulation tick.

The StatisticsEvaluator abstract class provides a way to monitor various statistics relating to the port’s operations. Four subclasses of StatisticsEvaluator are to be implemented, which gather and report data on the cargo and ships that have moved through the port. More subclasses could be implemented in future to provide more insights into the collected data.

Ships that are arriving at the port must wait in a ShipQueue before being allocated a quay to dock at. The ShipQueue prioritises inbound ships based on a set of rules that check each ship’s current status. For example, ships carrying dangerous cargo must be docked before any other ship waiting in the queue.

To ensure that each Cargo and Ship instance must have a unique cargo ID and IMO number respectively, the concept of the cargo/ship registry is introduced. These registries are a global mapping of IDs/IMO numbers to Cargo/Ship instances, where duplicate keys are not allowed. Each new Cargo/Ship instance registers itself with its registry inside the respective constructor.

To facilitate saving and loading the state of the simulation to and from a file, the Encodable interface has been introduced. Any class implementing this interface must implement the encode() method, which encodes the current state of the class to a machine-readable string. Similarly, classes that can be encoded can also be decoded from a string to a class instance through their fromString() method.

Many classes now override the default equals() and hashCode() method implementations inherited from Object, allowing instances to be checked for equality according to their internal state.

A Graphical User Interface (GUI) using JavaFX has been provided in the portsim.display package. The GUI consists of three classes: View is responsible for creating the visual elements displayed on screen, PortCanvas handles the graphics and drawing of the port in the main canvas, while ViewModel manages interaction between the View and the core classes of the model (e.g. Port). The Launcher class in the portsim package initialises the GUI and passes the save file to the ViewModel.
