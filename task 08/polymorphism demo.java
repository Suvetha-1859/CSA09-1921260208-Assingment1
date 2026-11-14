import java.time.Duration;
import java.time.LocalTime;

/**
 * Task 8 - compile-time polymorphism (overloading), runtime polymorphism (dynamic dispatch)
 * and the instanceof alternative. Uses the Vehicle hierarchy completed in FeeDemo.java.
 *
 * Compile: javac FeeDemo.java PolymorphismDemo.java
 * Run    : java PolymorphismDemo
 */
class FeeCalculator {
    // Overloaded methods: the COMPILER selects one from the argument types
    static double fee(Vehicle v, int minutes) {
        return v.calculateFee(minutes);
    }
    static double fee(Vehicle v, LocalTime in, LocalTime out) {
        int minutes = (int) Duration.between(in, out).toMinutes();
        if (minutes < 0) minutes += 1440;            // parked past midnight
        return v.calculateFee(minutes);
    }
    // Alternative to dynamic dispatch: instanceof + cast chain (must be edited for every new type)
    static double feeByInstanceof(Vehicle v, int minutes) {
        int hours = (minutes + 59) / 60;
        if (v instanceof ElectricCar) return hours * 30;
        else if (v instanceof Car) return hours * 30;
        else if (v instanceof TwoWheeler) return hours * 10;
        throw new IllegalArgumentException("Unknown vehicle");
    }
}

public class PolymorphismDemo {
    public static void main(String[] args) {
        Vehicle car = new Car("TN02B2");

        System.out.println("--- Compile-time polymorphism (overloading) ---");
        System.out.println("fee(car, 130)                          -> " + FeeCalculator.fee(car, 130)
                + "   [compiler picks fee(Vehicle,int)]");
        System.out.println("fee(car, 09:00, 11:10)                 -> "
                + FeeCalculator.fee(car, LocalTime.of(9, 0), LocalTime.of(11, 10))
                + "   [compiler picks fee(Vehicle,LocalTime,LocalTime)]");

        System.out.println("\n--- Upcasting: one Vehicle[] holds three different classes ---");
        Vehicle[] parked = { new TwoWheeler("TN01A1"), new Car("TN02B2"), new ElectricCar("TN03C3") };

        System.out.println("\n--- Dynamic method dispatch trace for parked[2].calculateFee(130) ---");
        Vehicle v = parked[2];
        System.out.println("declared (reference) type : Vehicle");
        System.out.println("actual (object) type      : " + v.getClass().getSimpleName());
        System.out.println("calculateFee is defined in: Vehicle (inherited), but it calls ratePerHour()");
        System.out.println("ratePerHour() chosen at runtime from the ACTUAL type: ElectricCar -> Car.ratePerHour() = "
                + v.ratePerHour());
        System.out.println("result                    : " + v.calculateFee(130));

        System.out.println("\n--- Same call, three behaviours ---");
        for (Vehicle p : parked) {
            System.out.println(p.getClass().getSimpleName() + " -> " + p.calculateFee(130)
                    + "   (instanceof version: " + FeeCalculator.feeByInstanceof(p, 130) + ")");
        }
    }
}

