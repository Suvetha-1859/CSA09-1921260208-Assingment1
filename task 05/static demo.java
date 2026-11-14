import java.lang.ref.WeakReference;

/**
 * Task 5 - static members, object passing and the object life cycle.
 *
 * Compile: javac StaticDemo.java
 * Run    : java StaticDemo
 */
class Ticket {
    // static: belongs to the CLASS, shared by every Ticket and every facility
    private static int nextTicketNo;
    private static int totalOccupancy;
    private static final int[] FEE_SLAB_HOURS;

    static {                                   // static block: runs once, when the class is first loaded
        nextTicketNo = 1000;
        totalOccupancy = 0;
        FEE_SLAB_HOURS = new int[]{1, 8, 24};  // first hour, daily cap hour, full day
        System.out.println("[static block] Ticket class loaded, counter starts at " + nextTicketNo);
    }

    private final int ticketNo;                // instance: different for every ticket
    private final String regNo;
    private int exitMinute = -1;               // mutable on purpose, to show object passing

    Ticket(String regNo) {
        this.ticketNo = ++nextTicketNo;        // unique across ALL facilities
        this.regNo = regNo;
        totalOccupancy++;
    }

    static int getTotalOccupancy() { return totalOccupancy; }   // static method: no object needed
    static void vehicleLeft() { totalOccupancy--; }

    int getTicketNo() { return ticketNo; }
    int getExitMinute() { return exitMinute; }
    void setExitMinute(int m) { exitMinute = m; }
    @Override public String toString() { return "Ticket#" + ticketNo + "(" + regNo + ")"; }
}

/** WRONG design: the counter is an instance variable of the facility. */
class FacilityWithInstanceCounter {
    private final String name;
    private int counter = 0;
    FacilityWithInstanceCounter(String name) { this.name = name; }
    String issue() { return name + "-" + (++counter); }          // fix 1: facility prefix keeps IDs unique
    int issueNumberOnly() { return ++counter; }                  // the bug: two facilities both return 1, 2, 3...
}

public class StaticDemo {

    // Object passing: Java passes the REFERENCE by value
    static void closeTicket(Ticket t, int exit) { t.setExitMinute(exit); }        // changes the shared object
    static void tryToReplace(Ticket t) { t = new Ticket("TN99ZZ9999"); }         // only changes the local copy of the reference
    static Ticket issueTicket(String regNo) { return new Ticket(regNo); }        // returning an object = returning a reference

    public static void main(String[] args) {
        System.out.println("--- Static counter (one facility class, shared counter) ---");
        Ticket a = issueTicket("TN01AB1111");
        Ticket b = issueTicket("TN02CD2222");
        System.out.println(a + " and " + b + " , occupancy = " + Ticket.getTotalOccupancy());

        System.out.println("\n--- Instance counter with two facilities (north / south gate) ---");
        FacilityWithInstanceCounter north = new FacilityWithInstanceCounter("N");
        FacilityWithInstanceCounter south = new FacilityWithInstanceCounter("S");
        System.out.println("number only : north=" + north.issueNumberOnly() + " south=" + south.issueNumberOnly()
                + "  <-- both are 1: DUPLICATE ticket numbers");
        System.out.println("with prefix : " + north.issue() + " , " + south.issue() + "  <-- unique");

        System.out.println("\n--- Passing objects to methods ---");
        closeTicket(a, 615);
        System.out.println("after closeTicket(a, 615)  -> exitMinute = " + a.getExitMinute() + " (caller sees the change)");
        Ticket before = a;
        tryToReplace(a);
        System.out.println("after tryToReplace(a)      -> a is still the same object: " + (a == before));

        System.out.println("\n--- Object life cycle ---");
        Ticket.vehicleLeft();
        Ticket temp = new Ticket("TN03EF3333");
        WeakReference<Ticket> watcher = new WeakReference<>(temp);
        System.out.println("before: ticket reachable = " + (watcher.get() != null));
        temp = null;                 // no strong reference remains -> eligible for garbage collection
        System.gc();                 // only a request; the JVM decides
        try { Thread.sleep(100); } catch (InterruptedException e) { }
        System.out.println("after temp=null and System.gc(): collected = " + (watcher.get() == null)
                + " (usually true, but the JVM is not obliged to collect immediately)");
    }
}

