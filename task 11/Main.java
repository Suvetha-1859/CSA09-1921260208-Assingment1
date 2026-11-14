package parking.app;

import parking.model.*;
import parking.service.ParkingService;

import java.time.LocalDateTime;

/** Demo of one working day, printing every ticket and the fee so it can be compared with the hand calculation. */
public class Main {
    public static void main(String[] args) {
        ParkingService service = new ParkingService(new int[]{20, 30, 50});   // 3 floors, different slot counts
        LocalDateTime day = LocalDateTime.of(2026, 11, 16, 0, 0);

        Object[][] plan = {
            // type, regNo, user, pass, in(h), out(h:m), kWh, hand-calculated fee
            {"TWO_WHEELER", "TN01A1", UserType.VISITOR, false, 9, 0, 10, 30, 0, 20.0},
            {"CAR",         "TN02B2", UserType.VISITOR, false, 9, 0, 12, 0, 0, 90.0},
            {"EV",          "TN03C3", UserType.VISITOR, false, 10, 0, 11, 0, 5, 90.0},
            {"CAR",         "TN04D4", UserType.STUDENT, true, 9, 0, 17, 0, 0, 0.0},
            {"CAR",         "TN05E5", UserType.VISITOR, false, 8, 0, 20, 0, 0, 240.0},
        };

        System.out.printf("%-8s %-12s %-8s %-10s %-12s %-12s %-10s%n", "Ticket", "Vehicle", "RegNo", "User", "Program Rs", "Hand Rs", "Match");
        double handTotal = 0;
        for (Object[] p : plan) {
            Vehicle v = VehicleFactory.create((String) p[0], (String) p[1]);
            ParkingTicket t = service.enter(v, (UserType) p[2], (Boolean) p[3], day.withHour((Integer) p[4]).withMinute((Integer) p[5]));
            double fee = service.exit(v.getRegNo(), day.withHour((Integer) p[6]).withMinute((Integer) p[7]), (Integer) p[8]);
            double hand = (Double) p[9];
            handTotal += hand;
            System.out.printf("%-8d %-12s %-8s %-10s %-12.1f %-12.1f %-10s%n", t.getTicketNo(), v.getClass().getSimpleName(), v.getRegNo(), p[2], fee, hand, fee == hand);
        }
        System.out.println("Total collection by program : Rs " + service.getTotalCollection());
        System.out.println("Total collection by hand    : Rs " + handTotal);
        System.out.println("Slots occupied at end of day: " + service.occupied() + " of " + service.capacity());
    }
}

