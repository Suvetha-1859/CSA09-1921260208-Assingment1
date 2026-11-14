/**
 * Task 6 - Inheritance hierarchy.
 *   Vehicle (abstract)                     User (abstract)
 *     |-- TwoWheeler                         |-- Student
 *     |-- Car                                |-- Staff
 *           |-- ElectricCar                  |-- Visitor
 * Vehicle -> Car -> ElectricCar is MULTILEVEL inheritance; TwoWheeler/Car are HIERARCHICAL siblings.
 *
 * Compile: javac InheritanceDemo.java
 * Run    : java InheritanceDemo
 */
abstract class Vehicle {
    private final String regNo;
    protected Vehicle(String regNo) {
        System.out.println("  1. Vehicle constructor  (regNo=" + regNo + ")");
        this.regNo = regNo;
    }
    public String getRegNo() { return regNo; }
    protected abstract double ratePerHour();
    public String describe() { return getClass().getSimpleName() + " " + regNo + " @ Rs " + ratePerHour() + "/h"; }
}

class TwoWheeler extends Vehicle {
    TwoWheeler(String regNo) { super(regNo); System.out.println("  2. TwoWheeler constructor"); }
    @Override protected double ratePerHour() { return 10; }
}

class Car extends Vehicle {
    Car(String regNo) { super(regNo); System.out.println("  2. Car constructor"); }
    @Override protected double ratePerHour() { return 30; }
}

class ElectricCar extends Car {
    private final int batteryKWh;
    ElectricCar(String regNo, int batteryKWh) {
        super(regNo);                                   // MUST be the first statement
        this.batteryKWh = batteryKWh;
        System.out.println("  3. ElectricCar constructor (battery=" + batteryKWh + " kWh)");
    }
    @Override public String describe() { return super.describe() + ", battery " + batteryKWh + " kWh"; } // super.method()
}

// ---------- Users ----------
abstract class User {
    protected final String name;
    User(String name) { this.name = name; }
    abstract boolean mayHoldMonthlyPass();
}
class Student extends User { Student(String n) { super(n); } boolean mayHoldMonthlyPass() { return true; } }
class Staff   extends User { Staff(String n)   { super(n); } boolean mayHoldMonthlyPass() { return true; } }
class Visitor extends User { Visitor(String n) { super(n); } boolean mayHoldMonthlyPass() { return false; } }

// ---------- Alternative: composition (Vehicle HAS-A VehicleType) ----------
class VehicleType {
    final String name;
    final double rate;
    VehicleType(String name, double rate) { this.name = name; this.rate = rate; }
}
class ComposedVehicle {
    private final String regNo;
    private final VehicleType type;           // has-a
    ComposedVehicle(String regNo, VehicleType type) { this.regNo = regNo; this.type = type; }
    double fee(int minutes) { return ((minutes + 59) / 60) * type.rate; }   // same code for every type
    String describe() { return type.name + " " + regNo; }
}

public class InheritanceDemo {
    public static void main(String[] args) {
        System.out.println("Creating new ElectricCar(\"TN03C3\", 40) - constructor execution order:");
        ElectricCar ev = new ElectricCar("TN03C3", 40);
        System.out.println(ev.describe());

        System.out.println("\nCreating new TwoWheeler(\"TN01A1\"):");
        Vehicle v = new TwoWheeler("TN01A1");
        System.out.println(v.describe());

        System.out.println("\nUsers:");
        User[] users = {new Student("Asha"), new Staff("Ravi"), new Visitor("Guest")};
        for (User u : users) System.out.println("  " + u.getClass().getSimpleName() + " " + u.name + " may hold monthly pass: " + u.mayHoldMonthlyPass());

        System.out.println("\nComposition alternative (no subclass needed for a new type):");
        VehicleType ebike = new VehicleType("EBike", 5);          // a NEW type = a NEW OBJECT, no new class
        ComposedVehicle cv = new ComposedVehicle("TN07E7", ebike);
        System.out.println("  " + cv.describe() + " 130 min -> Rs " + cv.fee(130));
    }
}

