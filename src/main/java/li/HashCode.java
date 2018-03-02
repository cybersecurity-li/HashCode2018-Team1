package li;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class HashCode {

    private int rows;
    private int columns;
    private int vehicles;
    private int ridesCount;
    private int bonus;
    private int steps;
    private int now = 0;

    private Ride[] rides;
    private Car[] cars;

    private static final Map<String, String> fileSet;
    static {
        Map<String, String> files = ImmutableMap.of(
                "problem/a_example.in", "submissions/a_example.out"
                ,"problem/b_should_be_easy.in", "submissions/b_should_be_easy.out"
                ,"problem/c_no_hurry.in", "submissions/c_no_hurry.out"
                ,"problem/d_metropolis.in", "submissions/d_metropolis.out"
                ,"problem/e_high_bonus.in", "submissions/e_high_bonus.out"
                );
        fileSet = Collections.unmodifiableMap(files);
    }

    public static void main(String[] args) throws IOException {
        List<String> resultList;

        for(Map.Entry<String, String> files: fileSet.entrySet()) {
            System.out.println(" ========================================= ");
            System.out.println(" File: " + files.getKey());
            HashCode hashCode = new HashCode();
            hashCode.readData(files.getKey());
            hashCode.simulation();
            hashCode.writeSubmission(files.getValue());
        }
    }

    private void readData(String inFile) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(inFile), StandardCharsets.ISO_8859_1);

        // Determine how many columns there are.
        List<String> parameterLine = Splitter.on(CharMatcher.breakingWhitespace()).splitToList(lines.remove(0));
        rows =  Integer.parseInt(parameterLine.get(0));
        columns =  Integer.parseInt(parameterLine.get(1));
        vehicles =  Integer.parseInt(parameterLine.get(2));
        ridesCount =  Integer.parseInt(parameterLine.get(3));
        bonus =  Integer.parseInt(parameterLine.get(4));
        steps =  Integer.parseInt(parameterLine.get(5));

        rides = new Ride[ridesCount];
        cars = new Car[vehicles];
        for (int i = 0; i<vehicles; i++) {
            cars[i] = new Car();
        }

        int rowKey = 0;
        for (String line: lines) {

            List<String> lineItems = Splitter.on(CharMatcher.breakingWhitespace()).splitToList(line);

            Ride ride = new Ride();
            ride.index = rowKey;
            ride.xStart = Integer.parseInt(lineItems.get(0));
            ride.yStart = Integer.parseInt(lineItems.get(1));
            ride.xEnd = Integer.parseInt(lineItems.get(2));
            ride.yEnd = Integer.parseInt(lineItems.get(3));
            ride.earliestStart = Integer.parseInt(lineItems.get(4));
            ride.latestFinish = Integer.parseInt(lineItems.get(5));
            ride.routeCost = Math.abs(ride.xStart - ride.xEnd) + Math.abs(ride.yStart - ride.yEnd);

            rides[rowKey] = ride;
            rowKey++;
        }
    }

    private void writeSubmission(String outFile) throws IOException {
        List<String> results = new ArrayList<>(vehicles);
        for(Car car: cars) {
            results.add(car.numerOfRides + " " + car.ridesString.toString());
        }

        Files.write(Paths.get(outFile), results);
    }

    private List<String> simulation() {
        List<String> result = new ArrayList<>();

        System.out.println(" Steps: " + String.valueOf(steps));
        for(now = 0; now < steps; now++) {
            if (now % 1000 == 0) System.out.print(String.valueOf(now) + " ");
            selectNextRide();
            driveCars();
        }

        return result;
    }

    private void selectNextRide() {
        for (Car car: cars) {
            if (!car.hasMission) {
                int distance = rows + columns + 1;
                Ride nearestRide = null;
                Ride possibleRide = null;
                int possibleWait = 0;
                for (Ride ride: rides) {
                    if (ride.done) continue;
                    int distanceToStart = Math.abs(ride.xStart - car.x) + Math.abs(ride.yStart - car.y);
                    if (now + distanceToStart < ride.earliestStart) {
                        if (possibleWait > ride.earliestStart - now - distanceToStart) {
                            possibleWait = ride.earliestStart - now - distanceToStart;
                            possibleRide = ride;
                        }
                    }
                    if (now + distanceToStart + ride.routeCost > ride.latestFinish) continue;

                    if (distanceToStart < distance) {
                        distance = distanceToStart;
                        nearestRide = ride;
                    }
                }

                int possibleDist = rows+columns+1;
                if (possibleRide != null) {
                    possibleDist = Math.abs(possibleRide.xStart - car.x) + Math.abs(possibleRide.yStart - car.y) + possibleWait;
                }
                if (possibleRide != null && nearestRide != null) {
                    if (possibleDist < distance) {
                        nearestRide = null;
                    }
                } else if (nearestRide != null) {
                    car.xStart = nearestRide.xStart;
                    car.yStart = nearestRide.yStart;
                    car.xEnd = nearestRide.xEnd;
                    car.yEnd = nearestRide.yEnd;
                    car.xTarget = nearestRide.xStart;
                    car.yTarget = nearestRide.yStart;
                    car.hasStarted = false;
                    car.hasMission = true;
                    car.rideIndex = nearestRide.index;
                    nearestRide.done = true;
                    car.counter = 0;
                }
            }
        }
    }

    private void driveCars() {
        for (Car car: cars) {
            if (!car.hasMission) continue;

            if (car.x == car.xTarget && car.y == car.yTarget) {
                if (car.hasStarted) System.err.println("Car is at target but is started");
                car.hasStarted = true;
                car.xTarget = car.xEnd;
                car.yTarget = car.yEnd;
            }

            // drive
            if (car.x != car.xTarget) {
                if (car.xTarget > car.x) {
                    car.x++;
                } else {
                    car.x--;
                }
            } else {
                if (car.yTarget > car.y) {
                    car.y++;
                } else {
                    car.y--;
                }
            }

            // Target?

            if (car.x == car.xTarget && car.y == car.yTarget) {
                if (!car.hasStarted) {
                    car.hasStarted = true;
                    car.xTarget = car.xEnd;
                    car.yTarget = car.yEnd;
                } else {
                    car.hasMission = false;
                    car.numerOfRides++;
                    car.ridesString.append(String.valueOf(car.rideIndex) + " ");
                }
            }
            car.counter++;
        }
    }
}
