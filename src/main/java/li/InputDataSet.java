package li;

import java.util.*;

public class InputDataSet {
    // R – number of rows of the grid (1 ≤ R ≤ 10000)
    public final int numberOfRows;

    // C – number of columns of the grid (1 ≤ C ≤ 10000)
    public final int numberOfColumns;

    // F – number of vehicles in the fleet (1 ≤ F ≤ 1000)
    public final int numberOfVehicles;

    // N – number of rides (1 ≤ N ≤ 10000)
    public final int numberOfRides;

    // B – per-ride bonus for starting the ride on time (1 ≤ B ≤ 10000)
    public final int bonus;

    // T – number of steps in the simulation (1 ≤ T ≤ 10 )
    public final int steps;

    public final Set<Ride> rides;

    public final List<Vehicle> vehicles;

    public InputDataSet(int numberOfRows, int numberOfColumns, int numberOfVehicles, int numberOfRides, int bonus, int steps, Set<Ride> rides, List<Vehicle> vehicles) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.numberOfVehicles = numberOfVehicles;
        this.numberOfRides = numberOfRides;
        this.bonus = bonus;
        this.steps = steps;
        this.rides = rides;
        this.vehicles = vehicles;
    }
}


