import java.util.Scanner;

/**
 * Task 2 - Data representation: types, casting risk, jagged vs 1-D arrays, input.
 *
 * Compile: javac DataRepresentation.java
 * Run    : java DataRepresentation                 (uses a built-in sample line)
 *          java DataRepresentation TN01AB1111 CAR 90   (command-line arguments)
 */
public class DataRepresentation {

    public static void main(String[] args) {
        // ---------- 1. Reading input (Scanner on a sample line, or command-line args) ----------
        String regNo;
        String type;
        int minutes;
        if (args.length >= 3) {
            regNo = args[0];
            type = args[1];
            minutes = Integer.parseInt(args[2]);
        } else {
            Scanner sc = new Scanner("TN01AB1111 CAR 90");   // in a real run: new Scanner(System.in)
            regNo = sc.next();
            type = sc.next();
            minutes = sc.nextInt();
            sc.close();
        }
        double ratePerHour = 30.0;       // Rs per hour (double: money with decimals possible)
        System.out.println("Input -> regNo=" + regNo + ", type=" + type + ", minutes=" + minutes);

        // ---------- 2. Casting / integer-division risk ----------
        double wrong1 = (minutes / 60) * ratePerHour;                   // integer division: 90/60 = 1
        double wrong2 = (int) (minutes / 60.0) * ratePerHour;           // cast truncates 1.5 -> 1
        double right1 = ((minutes + 59) / 60) * ratePerHour;            // ceiling using int arithmetic only
        double right2 = Math.ceil(minutes / 60.0) * ratePerHour;        // ceiling using double
        System.out.println();
        System.out.println("Fee for " + minutes + " minutes at Rs " + ratePerHour + "/hour:");
        System.out.println("  (minutes / 60) * rate            = " + wrong1 + "   <-- WRONG (integer division)");
        System.out.println("  (int)(minutes / 60.0) * rate     = " + wrong2 + "   <-- WRONG (cast truncates)");
        System.out.println("  ((minutes + 59) / 60) * rate     = " + right1 + "   <-- correct");
        System.out.println("  Math.ceil(minutes / 60.0) * rate = " + right2 + "   <-- correct");

        // int overflow: epoch milliseconds do not fit in an int, so entry timestamps use long
        long epochMillis = 1_763_100_000_000L;
        int overflowed = (int) epochMillis;
        System.out.println();
        System.out.println("long epochMillis = " + epochMillis + " ; (int) cast gives " + overflowed + "  <-- overflow, so use long");

        // ---------- 3. Representing floors and slots ----------
        int[] slotsPerFloor = {20, 30, 50, 40, 60};      // different number of slots on each floor
        String[][] jagged = new String[slotsPerFloor.length][];
        for (int f = 0; f < slotsPerFloor.length; f++) jagged[f] = new String[slotsPerFloor[f]];

        // 1-D alternative: index = offset[floor] + slot  (floor * slotsPerFloor only works if all floors are equal)
        int[] offset = new int[slotsPerFloor.length];
        int total = 0;
        for (int f = 0; f < slotsPerFloor.length; f++) { offset[f] = total; total += slotsPerFloor[f]; }
        String[] flat = new String[total];

        int floor = 2, slot = 10;
        int correctIndex = offset[floor] + slot;                  // 50 + 10 = 60
        int naiveIndex = floor * slotsPerFloor[0] + slot;         // 2*20 + 10 = 50  (wrong for jagged floors)
        System.out.println();
        System.out.println("Jagged array: floors=" + jagged.length + ", slots on floor 2=" + jagged[2].length);
        System.out.println("1-D array size = " + flat.length + " ; floor 2 slot 10 -> correct index " + correctIndex
                + " , naive floor*S+slot gives " + naiveIndex + " (collides with floor 1 slot 20)");
    }
}

