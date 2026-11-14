 * Task 1 - Alternative Concept: vehicles as objects vs. plain Strings in parallel arrays.
 * Measures the time to find a vehicle by registration number (worst case: last element)
 * and prints an estimate of the space used per vehicle.
 *
 * Compile: javac ObjectVsParallelArrays.java
 * Run    : java ObjectVsParallelArrays
 */
public class ObjectVsParallelArrays {

    /** Design A: one object per vehicle. */
    static class VehicleRec {
        final String regNo;
        final String type;
        final String entryTime;
        VehicleRec(String regNo, String type, String entryTime) {
            this.regNo = regNo;
            this.type = type;
            this.entryTime = entryTime;
        }
    }

    static String reg(int i) {
        return String.format("TN01%c%04d", (char) ('A' + i / 10000), i % 10000);
    }

    // Design A search: linear scan over objects
    static int findObject(VehicleRec[] a, String regNo) {
        for (int i = 0; i < a.length; i++) {
            if (a[i].regNo.equals(regNo)) return i;
        }
        return -1;
    }

    // Design B search: linear scan over the regNo[] parallel array
    static int findParallel(String[] regNo, String key) {
        for (int i = 0; i < regNo.length; i++) {
            if (regNo[i].equals(key)) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] sizes = {1_000, 10_000, 100_000};
        long sink = 0; // prevents the JIT from removing the searches

        System.out.printf("%-10s %-22s %-22s%n", "n", "objects (ns/search)", "parallel arrays (ns/search)");
        for (int n : sizes) {
            VehicleRec[] objs = new VehicleRec[n];
            String[] regNo = new String[n];
            String[] type = new String[n];
            String[] entry = new String[n];
            for (int i = 0; i < n; i++) {
                objs[i] = new VehicleRec(reg(i), "CAR", "09:00");
                regNo[i] = reg(i);
                type[i] = "CAR";
                entry[i] = "09:00";
            }
            String key = reg(n - 1); // worst case: last element

            int reps = Math.max(20, 2_000_000 / n);
            for (int w = 0; w < reps; w++) { sink += findObject(objs, key) + findParallel(regNo, key); } // warm-up

            long t0 = System.nanoTime();
            for (int r = 0; r < reps; r++) sink += findObject(objs, key);
            long tObj = (System.nanoTime() - t0) / reps;

            t0 = System.nanoTime();
            for (int r = 0; r < reps; r++) sink += findParallel(regNo, key);
            long tPar = (System.nanoTime() - t0) / reps;

            System.out.printf("%-10d %-22d %-22d%n", n, tObj, tPar);
        }

        // Space per vehicle (HotSpot, 64-bit, compressed oops): reference = 4 bytes, object header = 12 bytes
        int objectBytes = 12 + 3 * 4;      // header + 3 references = 24 bytes (already 8-byte aligned)
        int arrayRefBytes = 3 * 4;         // one reference slot in each of the three arrays
        System.out.println();
        System.out.println("Space per vehicle (excluding the String contents, which are the same in both designs):");
        System.out.println("  Object design        : " + objectBytes + " bytes (object) + 4 bytes (slot in VehicleRec[]) = " + (objectBytes + 4) + " bytes");
        System.out.println("  Parallel arrays      : " + arrayRefBytes + " bytes (three array slots)");
        System.out.println("(sink=" + sink + ")");
    }
}

