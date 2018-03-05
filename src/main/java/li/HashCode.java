package li;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class HashCode {

    private static final Map<String, String> fileSet;
    private InputDataSet dataSet;

    static {
        Map<String, String> files = ImmutableMap.of(
                "problem/a_example.in", "submissions/a_example.out"
                , "problem/b_should_be_easy.in", "submissions/b_should_be_easy.out"
                , "problem/c_no_hurry.in", "submissions/c_no_hurry.out"
                , "problem/d_metropolis.in", "submissions/d_metropolis.out"
                , "problem/e_high_bonus.in", "submissions/e_high_bonus.out"
        );
        fileSet = Collections.unmodifiableMap(files);
    }

    public static void main(String[] args) throws IOException {

        for (Map.Entry<String, String> files : fileSet.entrySet()) {
            System.out.println(" ========================================= ");
            System.out.println(" File: " + files.getKey());
            HashCode hashCode = new HashCode();
            hashCode.readInputDataSet(files.getKey());
            hashCode.simulation();
            hashCode.writeSubmission(files.getValue());
        }
    }

    private void readInputDataSet(String inFile) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inFile), StandardCharsets.ISO_8859_1);

        // The first line of the input file contains the following integer numbers separated by single spaces:
        // R – number of rows of the grid (1 ≤ R ≤ 10000)
        // C – number of columns of the grid (1 ≤ C ≤ 10000)
        // F – number of vehicles in the fleet (1 ≤ F ≤ 1000)
        // N – number of rides (1 ≤ N ≤ 10000)
        // B – per-ride bonus for starting the ride on time (1 ≤ B ≤ 10000)
        // T – number of steps in the simulation (1 ≤ T ≤ 10 )
        List<String> parameterLine = Splitter.on(CharMatcher.breakingWhitespace()).splitToList(lines.remove(0));
        int numberOfRows = Integer.parseInt(parameterLine.get(0));
        int numberOfColumns = Integer.parseInt(parameterLine.get(1));
        int numberOfVehicles = Integer.parseInt(parameterLine.get(2));
        int numberOfRides = Integer.parseInt(parameterLine.get(3));
        int bonus = Integer.parseInt(parameterLine.get(4));
        int steps = Integer.parseInt(parameterLine.get(5));

        List<Vehicle> vehicles = new ArrayList<>(numberOfVehicles);
        for (int i = 0; i < numberOfVehicles; i++) {
            vehicles.add(new Vehicle());
        }

        Set<Ride> rides = new HashSet<>(numberOfRides);

        int rideNumber = 0;
        for (String line : lines) {
            // N subsequent lines of the input file describe the individual rides, from ride 0 to ride N − 1 . Each line
            // contains the following integer numbers separated by single spaces:
            // a – the row of the start intersection (0 ≤ a < R)
            // b – the column of the start intersection (0 ≤ b < C)
            // x – the row of the finish intersection (0 ≤ x < R)
            // y – the column of the finish intersection (0 ≤ y < C)
            // s – the earliest start(0 ≤ s < T)
            // f – the latest finish (0 ≤ f ≤ T) , (f ≥ s + |x − a| + |y − b|)
            List<String> lineItems = Splitter.on(CharMatcher.breakingWhitespace()).splitToList(line);
            int startX = Integer.parseInt(lineItems.get(0));
            int startY = Integer.parseInt(lineItems.get(1));
            int endX = Integer.parseInt(lineItems.get(2));
            int endY = Integer.parseInt(lineItems.get(3));
            int earliestStart = Integer.parseInt(lineItems.get(4));
            int latestFinish = Integer.parseInt(lineItems.get(5));
            int routeLength = Math.abs(startX - endX) + Math.abs(startY - endY);

            Ride ride = new Ride(rideNumber, startX, startY, endX, endY, earliestStart, latestFinish, routeLength);

            rides.add(ride);
            rideNumber++;
        }
        dataSet = new InputDataSet(numberOfRows, numberOfColumns, numberOfVehicles, numberOfRides, bonus, steps, rides, vehicles);
    }

    private void writeSubmission(String outFile) throws IOException {
        // Each line describing the rides of a vehicle must contain the following integers separated by single spaces:
        // M – number of rides assigned to the vehicle (0 ≤ M ≤ N)
        // R0, R1, .. ride numbers assigned to the vehicle, in the order in which the vehicle will perform them
        Files.write(Paths.get(outFile),
                dataSet.vehicles.stream()
                        .map(Vehicle::toString)
                        .collect(Collectors.toList()));
    }

    private void simulation() {

        System.out.println(" Steps: " + String.valueOf(dataSet.steps));
        for (int step = 0; step < dataSet.steps; step++) {
            if (step % 1000 == 0) System.out.print(String.valueOf(step) + " ");
            selectNextRide(step);
            drive(step);
        }
        System.out.println();
    }

    private void selectNextRide(int step) {
        for (Vehicle vehicle : dataSet.vehicles) {
            if (!vehicle.hasOrder()) {
                Ride nextRide = null;

                int minDeadhead = Integer.MAX_VALUE;
                for (Ride ride : dataSet.rides) {
                    int distanceToStart = Math.abs(ride.startX - vehicle.getX()) + Math.abs(ride.startY - vehicle.getY());
                    // Ignore routes that cannot be completed in time.
                    if (step + distanceToStart + ride.routeLength > ride.latestFinish) continue;

                    if (step + distanceToStart < ride.earliestStart) {
                        int waitingTime = ride.earliestStart - (step + distanceToStart);
                        if ((waitingTime + distanceToStart) < minDeadhead) {
                            minDeadhead = (waitingTime + distanceToStart);
                            nextRide = ride;
                        }
                    } else if (step + distanceToStart >= ride.earliestStart) {
                       if (distanceToStart < minDeadhead) {
                           minDeadhead = distanceToStart;
                           nextRide = ride;
                       }
                    }
                }

                if (nextRide != null) {
                    vehicle.setOrder(nextRide, step);
                    dataSet.rides.remove(nextRide);
                }

            }
        }
    }

    private void drive(int step) {
        for (Vehicle vehicle : dataSet.vehicles) {
            if (vehicle.hasOrder()) {
                vehicle.drive(step);
            }
        }
    }
}
