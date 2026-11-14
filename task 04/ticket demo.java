/**
 * Task 4 - Classes, objects and constructors.
 * ParkingTicket has: a default constructor, a parameterised constructor, an overloaded
 * constructor, a copy constructor, and constructor chaining with this(...).
 * The alternative approaches (setters, static factory) are measured in main().
 *
 * Compile: javac TicketDemo.java
 * Run    : java TicketDemo
 */
class ParkingTicket {
    private final int ticketNo;
    private final String regNo;
    private final int floor;          // -1 = not yet assigned
    private final int slot;           // -1 = not yet assigned
    private final int entryMinute;    // minutes since midnight, 0..1439
    private final boolean duplicate;  // true for a re-issued copy

    // 1. Default constructor: delegates to the 2-argument one
    ParkingTicket() {
        this(0, "UNKNOWN");
    }

    // 2. Overloaded constructor (ticket without a slot yet): delegates to the 5-argument one
    ParkingTicket(int ticketNo, String regNo) {
        this(ticketNo, regNo, -1, -1, 0);
    }

    // 3. Parameterised constructor: delegates to the master constructor
    ParkingTicket(int ticketNo, String regNo, int floor, int slot, int entryMinute) {
        this(ticketNo, regNo, floor, slot, entryMinute, false);
    }

    // 4. Copy constructor: used to re-issue a duplicate ticket (same data, flagged as duplicate)
    ParkingTicket(ParkingTicket other) {
        this(other.ticketNo, other.regNo, other.floor, other.slot, other.entryMinute, true);
    }

    // Master constructor: the ONLY place where validation and field assignment are written
    private ParkingTicket(int ticketNo, String regNo, int floor, int slot, int entryMinute, boolean duplicate) {
        if (ticketNo < 0) throw new IllegalArgumentException("ticketNo must be >= 0");
        if (regNo == null || regNo.isEmpty()) throw new IllegalArgumentException("regNo required");
        if (entryMinute < 0 || entryMinute > 1439) throw new IllegalArgumentException("entryMinute must be 0..1439");
        this.ticketNo = ticketNo;
        this.regNo = regNo;
        this.floor = floor;
        this.slot = slot;
        this.entryMinute = entryMinute;
        this.duplicate = duplicate;
    }

    // Alternative: static factory method
    static ParkingTicket of(int ticketNo, String regNo, int floor, int slot, int entryMinute) {
        return new ParkingTicket(ticketNo, regNo, floor, slot, entryMinute);
    }

    @Override public String toString() {
        return "Ticket#" + ticketNo + "[" + regNo + ", floor=" + floor + ", slot=" + slot
                + ", entry=" + entryMinute + (duplicate ? ", DUPLICATE" : "") + "]";
    }
}

/** Alternative: default constructor + setters. The object exists in an invalid state until every setter is called. */
class MutableTicket {
    private int ticketNo;
    private String regNo;
    private int floor, slot, entryMinute;
    MutableTicket() { }
    void setTicketNo(int v) { ticketNo = v; }
    void setRegNo(String v) { regNo = v; }
    void setFloor(int v) { floor = v; }
    void setSlot(int v) { slot = v; }
    void setEntryMinute(int v) { entryMinute = v; }
}

public class TicketDemo {
    public static void main(String[] args) {
        System.out.println("--- Constructors ---");
        ParkingTicket t0 = new ParkingTicket();
        ParkingTicket t1 = new ParkingTicket(1001, "TN01AB1111");
        ParkingTicket t2 = new ParkingTicket(1002, "TN02CD2222", 0, 5, 545);
        ParkingTicket t3 = new ParkingTicket(t2);             // copy constructor -> duplicate ticket
        System.out.println(t0);
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);

        System.out.println("\n--- References, stack and heap ---");
        ParkingTicket alias = t2;                              // copies the REFERENCE, same heap object
        System.out.println("alias == t2 (same object)     : " + (alias == t2));
        System.out.println("t3 == t2 (different object)    : " + (t3 == t2));
        System.out.println("identityHashCode t2/alias/t3   : " + System.identityHashCode(t2) + " / "
                + System.identityHashCode(alias) + " / " + System.identityHashCode(t3));

        System.out.println("\n--- Validation by the master constructor ---");
        try {
            new ParkingTicket(1003, "TN03EF3333", 0, 1, 2000);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        System.out.println("\n--- Alternative approaches: time to create n tickets ---");
        int[] sizes = {100_000, 1_000_000};
        long sink = 0;
        for (int w = 0; w < 3; w++) { // warm-up
            for (int i = 0; i < 200_000; i++) {
                sink += new ParkingTicket(i, "TN01AB1111", 0, i % 50, 540).hashCode();
                MutableTicket m = new MutableTicket();
                m.setTicketNo(i); m.setRegNo("TN01AB1111"); m.setFloor(0); m.setSlot(i % 50); m.setEntryMinute(540);
                sink += m.hashCode();
                sink += ParkingTicket.of(i, "TN01AB1111", 0, i % 50, 540).hashCode();
            }
        }
        System.out.printf("%-10s %-22s %-22s %-22s%n", "n", "constructor (ms)", "default+setters (ms)", "static factory (ms)");
        for (int n : sizes) {
            long t = System.nanoTime();
            for (int i = 0; i < n; i++) sink += new ParkingTicket(i, "TN01AB1111", 0, i % 50, 540).hashCode();
            double a = (System.nanoTime() - t) / 1e6;

            t = System.nanoTime();
            for (int i = 0; i < n; i++) {
                MutableTicket m = new MutableTicket();
                m.setTicketNo(i); m.setRegNo("TN01AB1111"); m.setFloor(0); m.setSlot(i % 50); m.setEntryMinute(540);
                sink += m.hashCode();
            }
            double b = (System.nanoTime() - t) / 1e6;

            t = System.nanoTime();
            for (int i = 0; i < n; i++) sink += ParkingTicket.of(i, "TN01AB1111", 0, i % 50, 540).hashCode();
            double c = (System.nanoTime() - t) / 1e6;

            System.out.printf("%-10d %-22.2f %-22.2f %-22.2f%n", n, a, b, c);
        }
        System.out.println("(sink=" + sink + ")");
    }
}

