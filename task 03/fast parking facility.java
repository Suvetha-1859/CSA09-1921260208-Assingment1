import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Task 3 - Alternative Concept: keep a count of free slots per floor and a stack of free
 * slot numbers per floor, so that allocate() never scans occupied slots.
 */
public class FastParkingFacility {
    private final String[][] slots;
    private final int[] freeCount;              // free slots on each floor
    private final Deque<Integer>[] freeStack;   // free slot numbers on each floor (top = lowest number)

    @SuppressWarnings("unchecked")
    public FastParkingFacility(int[] slotsPerFloor) {
        int floors = slotsPerFloor.length;
        slots = new String[floors][];
        freeCount = new int[floors];
        freeStack = new Deque[floors];
        for (int f = 0; f < floors; f++) {
            slots[f] = new String[slotsPerFloor[f]];
            freeStack[f] = new ArrayDeque<>();
            for (int s = slotsPerFloor[f] - 1; s >= 0; s--) freeStack[f].push(s); // slot 0 ends on top
            freeCount[f] = slotsPerFloor[f];
        }
    }

    /** O(F): look at one counter per floor, then pop in O(1). */
    public int[] allocate(String vehicleNo) {
        for (int f = 0; f < slots.length; f++) {
            if (freeCount[f] > 0) {
                int s = freeStack[f].pop();
                slots[f][s] = vehicleNo;
                freeCount[f]--;
                return new int[]{f, s};
            }
