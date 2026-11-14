import java.util.HashSet;
import java.util.Set;

/**
 * Task 7 - Overriding, final, and the Object-class methods.
 *
 * Compile: javac OverrideDemo.java
 * Run    : java OverrideDemo
 */
abstract class Vehicle {
    public static final double CAP_HOURS = 8;               // final variable: a constant, never changes
    private final String regNo;                             // final field: identity cannot change after creation

    protected Vehicle(String regNo) { this.regNo = regNo; }

    public final String getRegNo() { return regNo; }        // final method: no subclass may change how identity is read

    protected abstract double ratePerHour();                // each subclass MUST override
    protected double dailyCap() { return CAP_HOURS * ratePerHour(); }   // hook: subclass MAY override

    /** Template method: slab logic written once, specialised through the two hooks above. */
    public double calculateFee(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("minutes must be >= 0");
        int days = minutes / 1440;
        int hours = ((minutes % 1440) + 59) / 60;
        return days * dailyCap() + Math.min(hours * ratePerHour(), dailyCap());
    }

    @Override public String toString() { return getClass().getSimpleName() + "[" + regNo + "]"; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        return regNo.equals(((Vehicle) o).regNo);            // equality = same registration number
    }

    @Override public int hashCode() { return regNo.hashCode(); }   // equal objects MUST have equal hash codes
}

class TwoWheeler extends Vehicle {
    TwoWheeler(String regNo) { super(regNo); }
    @Override protected double ratePerHour() { return 10; }
    @Override protected double dailyCap() { return 50; }          // overrides the hook: flat Rs 50 maximum per day
}

class Car extends Vehicle {
    Car(String regNo) { super(regNo); }
    @Override protected double ratePerHour() { return 30; }
}

final class ElectricCar extends Car {                              // final class: no further specialisation planned
    ElectricCar(String regNo) { super(regNo); }
    @Override public String toString() { return super.toString() + "(EV)"; }
}

/** Alternative: ONE calculateFee with a switch on a type field. */
class SwitchFee {
    static double fee(String type, int minutes) {
        int hours = (minutes + 59) / 60;
        switch (type) {
            case "TWO_WHEELER": return Math.min(hours * 10, 50);
            case "CAR":         return Math.min(hours * 30, 240);
            case "EV":          return Math.min(hours * 30, 240);
            default: throw new IllegalArgumentException("Unknown type " + type);   // every new type edits THIS method
        }
    }
}

public class OverrideDemo {
    public static void main(String[] args) {
        Vehicle[] list = { new TwoWheeler("TN01A1"), new Car("TN02B2"), new ElectricCar("TN03C3") };
        for (Vehicle v : list) {
            System.out.println(v + " : 130 min = Rs " + v.calculateFee(130) + " , 10 hours = Rs " + v.calculateFee(600));
        }

        System.out.println("\nequals / hashCode:");
        Vehicle x = new Car("TN02B2");
        Vehicle y = new Car("TN02B2");
        System.out.println("x == y       : " + (x == y));
        System.out.println("x.equals(y)  : " + x.equals(y));
        System.out.println("same hash    : " + (x.hashCode() == y.hashCode()));
        Set<Vehicle> parked = new HashSet<>();
        parked.add(x);
        boolean added = parked.add(y);
        System.out.println("HashSet.add(second TN02B2) accepted? " + added + " , set size = " + parked.size());

        System.out.println("\nSwitch alternative: TwoWheeler 130 min = Rs " + SwitchFee.fee("TWO_WHEELER", 130));
    }
}

