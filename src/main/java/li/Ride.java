package li;

import com.google.common.base.Objects;

public class Ride {

    public final int rideNumber;
    // a – the row of the start intersection (0 ≤ a < R)
    public final int startX;
    // b – the column of the start intersection (0 ≤ b < C)
    public final int startY;
    // x – the row of the finish intersection (0 ≤ x < R)
    public final int endX;
    // x – the row of the finish intersection (0 ≤ x < R)
    public final int endY;
    // s – the earliest start(0 ≤ s < T)
    public final int earliestStart;
    // f – the latest finish (0 ≤ f ≤ T) , (f ≥ s + |x − a| + |y − b|)
    public final int latestFinish;
    // route length
    public final int routeLength;

    public Ride(int rideNumber, int startX, int startY, int endX, int endY, int earliestStart, int latestFinish, int routeLength) {
        this.rideNumber = rideNumber;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.earliestStart = earliestStart;
        this.latestFinish = latestFinish;
        this.routeLength = routeLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ride ride = (Ride) o;
        return rideNumber == ride.rideNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rideNumber);
    }
}
