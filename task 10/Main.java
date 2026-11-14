package parking.app;

import parking.model.ParkingSession;
import parking.model.UnsafeSession;
import parking.service.SessionService;

import java.util.ArrayList;
import java.util.List;

/** parking.app: entry point. Packages: parking.model (data), parking.service (logic), parking.app (UI/main). */
public class Main {
    public static void main(String[] args) {
        System.out.println("--- Validated setters ---");
        ParkingSession s = new ParkingSession("TN01AB1111", 50);
        System.out.println("Fee for 130 minutes: Rs " + SessionService.billFor(s, 130));
        String[] bad = {"negative duration", "invalid slot", "invalid regNo"};
        for (String what : bad) {
            try {
                switch (what) {
                    case "negative duration": s.setMinutes(-5); break;
                    case "invalid slot":      s.setSlot(99); break;
                    default:                  s.setRegNo("hello"); break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Rejected (" + what + "): " + e.getMessage());
            }
        }
        System.out.println("State after rejected updates: minutes=" + s.getMinutes() + ", slot=" + s.getSlot() + ", regNo=" + s.getRegNo());

        System.out.println("\n--- Alternative: public fields ---");
        UnsafeSession u = new UnsafeSession();
        u.minutes = -5;          // nothing stops this
        u.slot = 9999;
        u.regNo = "hello";
        System.out.println("UnsafeSession now holds minutes=" + u.minutes + ", slot=" + u.slot + ", regNo=" + u.regNo + "  <-- invalid state accepted");

        System.out.println("\n--- Wrapper classes and autoboxing ---");
        Integer a = 127, b = 127;
        Integer c = 1000, d = 1000;
        System.out.println("Integer 127  == 127  : " + (a == b) + "   (cached objects, same reference)");
        System.out.println("Integer 1000 == 1000 : " + (c == d) + "  (two different objects!)");
        System.out.println("Integer 1000 equals 1000 : " + c.equals(d) + "   <-- always compare wrappers with equals()");
        List<Integer> fees = new ArrayList<>();
        fees.add(30);                         // autoboxing int -> Integer
        int first = fees.get(0);              // auto-unboxing Integer -> int
        System.out.println("autoboxed and unboxed value: " + first);
        Integer notComputed = null;
        try {
            int x = notComputed;              // unboxing null
            System.out.println(x);
        } catch (NullPointerException e) {
            System.out.println("Unboxing a null Integer throws NullPointerException");
        }
    }
}

