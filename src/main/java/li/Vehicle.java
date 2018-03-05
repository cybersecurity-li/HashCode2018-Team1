package li;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Vehicle {

    private int x = 0;
    private int y = 0;

    private Ride order = null;
    private boolean hasPassenger = false;

    private List<Ride> ridesPerformed = new ArrayList<>();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasOrder() {
        return order != null;
    }

    public void setOrder(Ride order, int step) {
        if (hasOrder()) {
            throw new RuntimeException("Vehicle already has an order.");
        }

        this.order = order;
        updateStatus(step);
    }

    public void drive(int step) {
        updateStatus(step);
        if (hasOrder()) {
            if (!hasPassenger) {
                driveTo(order.startX, order.startY);
            } else {
                driveTo(order.endX, order.endY);
            }
            updateStatus(step);
        }
    }

    private void driveTo(int toX, int toY) {
        if (x != toX) {
            x = toX > x ? x + 1 : x - 1;
        } else if (y != toY) {
            y = toY > y ? y + 1 : y - 1;
        } else {
            if (!hasPassenger) {
                // waiting for passenger
            } else {
                throw new RuntimeException("Vehicle is already at target.");
            }
        }
    }

    private void updateStatus(int step) {
        if (!hasPassenger) {
            if (x == order.startX && y == order.startY) {
                if (step >= order.earliestStart) {
                    // Pick up a passenger.
                    hasPassenger = true;
                }
            }
        } else {
            if (x == order.endX && y == order.endY) {
                if (step > order.latestFinish) throw new RuntimeException("Arrival is too late.");
                // Passenger is at destination.
                ridesPerformed.add(order);
                order = null;
                hasPassenger = false;
            }
        }
    }

    @Override
    public String toString() {
        return String.valueOf(ridesPerformed.size() + " "
                + ridesPerformed.stream().map(r -> String.valueOf(r.rideNumber)).collect(Collectors.joining(" ")));
    }
}
