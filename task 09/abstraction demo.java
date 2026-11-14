/**
 * Task 9 - Abstraction: an abstract class for the "what every vehicle is",
 * interfaces for capabilities that only some classes have.
 *
 * Compile: javac AbstractionDemo.java
 * Run    : java AbstractionDemo
 */
interface Chargeable {
    double RATE_PER_KWH = 12;                                   // implicitly public static final

    double chargingFee(int kWh);                                // abstract method

    default double totalBill(double parkingFee, int kWh) {      // default method: shared behaviour, no repetition in implementers
        return parkingFee + chargingFee(kWh);
    }

    static boolean isValidEnergy(int kWh) {                     // static method: utility belonging to the interface
        return kWh >= 0 && kWh <= 200;
    }
}

interface Payable {
    double amountDue();
}

/** Abstract class: has state (regNo), a constructor and partly finished behaviour. Cannot be instantiated. */
abstract class Vehicle {
    private final String regNo;
    protected Vehicle(String regNo) { this.regNo = regNo; }
    public String getRegNo() { return regNo; }
    protected abstract double ratePerHour();                    // WHAT: every vehicle has a rate. HOW: left to subclasses
    public double calculateFee(int minutes) { return ((minutes + 59) / 60) * ratePerHour(); }   // finished behaviour
}

class TwoWheeler extends Vehicle {
    TwoWheeler(String r) { super(r); }
    @Override protected double ratePerHour() { return 10; }
}

class Car extends Vehicle {
    Car(String r) { super(r); }
    @Override protected double ratePerHour() { return 30; }
}

/** One superclass (extends) + TWO capabilities (implements): multiple behaviours through interfaces. */
class ElectricCar extends Car implements Chargeable, Payable {
    private double due;
    ElectricCar(String r) { super(r); }
    @Override public double chargingFee(int kWh) { return kWh * RATE_PER_KWH; }
    void settle(int minutes, int kWh) { due = totalBill(calculateFee(minutes), kWh); }
    @Override public double amountDue() { return due; }
}

/** An unrelated class can also be Payable - that is why Payable is an interface and not an abstract class. */
class VisitorAccount implements Payable {
    @Override public double amountDue() { return 100; }          // one-day pass price
}

public class AbstractionDemo {
    public static void main(String[] args) {
        // Vehicle v = new Vehicle("X");   // compile error: Vehicle is abstract
        ElectricCar ev = new ElectricCar("TN03C3");
        ev.settle(130, 10);
        System.out.println("EV parking fee   : " + ev.calculateFee(130));
        System.out.println("EV charging fee  : " + ev.chargingFee(10));
        System.out.println("EV total bill    : " + ev.amountDue() + "  (default method totalBill)");
        System.out.println("valid energy 50  : " + Chargeable.isValidEnergy(50) + " ; valid energy 500: " + Chargeable.isValidEnergy(500));

        Payable[] payables = { ev, new VisitorAccount() };       // unrelated classes treated uniformly
        double total = 0;
        for (Payable p : payables) total += p.amountDue();
        System.out.println("Total payable    : " + total);
    }
}

