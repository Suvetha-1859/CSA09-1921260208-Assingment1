/**
 * Task 3 - measures allocate()/release() of the scanning facility (ParkingFacility)
 * against the counter + stack facility (FastParkingFacility) in the worst case:
 * the facility is almost full, so the only free slot is the LAST one.
 *
 * Compile: javac ParkingFacility.java FastParkingFacility.java AllocationBenchmark.java
 * Run    : java AllocationBenchmark
 */
public class AllocationBenchmark {

    static int[] layout(int floors, int slotsPerFloor) {
        int[] a = new int[floors];
        java.util.Arrays.fill(a, slotsPerFloor);
        return a;
    }

    public static void main(String[] args) {
        int[][] configs = {{1, 20}, {3, 50}, {5, 100}, {50, 1000}};
        long sink = 0;
        System.out.printf("%-14s %-8s %-26s %-26s%n", "F x S", "slots", "scan: release+allocate ns", "stack: release+allocate ns");
        for (int[] c : configs) {
            int F = c[0], S = c[1];
            int total = F * S;
            int reps = Math.max(2000, 4_000_000 / total);

            ParkingFacility scan = new ParkingFacility(layout(F, S));
            FastParkingFacility fast = new FastParkingFacility(layout(F, S));
            for (int i = 0; i < total; i++) { scan.allocate("V" + i); fast.allocate("V" + i); } // fill completely

            // warm-up
            for (int w = 0; w < reps; w++) {
                scan.release(F - 1, S - 1); sink += scan.allocate("X")[1];
                fast.release(F - 1, S - 1); sink += fast.allocate("X")[1];
            }

            long t0 = System.nanoTime();
            for (int r = 0; r < reps; r++) { scan.release(F - 1, S - 1); sink += scan.allocate("X")[1]; }
            long tScan = (System.nanoTime() - t0) / reps;

            t0 = System.nanoTime();
            for (int r = 0; r < reps; r++) { fast.release(F - 1, S - 1); sink += fast.allocate("X")[1]; }
            long tFast = (System.nanoTime() - t0) / reps;

            System.out.printf("%-14s %-8d %-26d %-26d%n", F + " x " + S, total, tScan, tFast);
        }
        System.out.println("(sink=" + sink + ")");
    }
}

