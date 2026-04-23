import java.util.*;

// ================= RESERVATION =================
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// ================= BOOKING HISTORY =================
class BookingHistory {

    // List preserves insertion order
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Get all bookings (read-only usage)
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// ================= REPORT SERVICE =================
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display full booking history
    public void displayAllBookings() {
        System.out.println("===== Booking History =====");

        List<Reservation> list = history.getAllReservations();

        if (list.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : list) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummaryReport() {
        System.out.println("\n===== Booking Summary Report =====");

        Map<String, Integer> countByRoom = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            String type = r.getRoomType();
            countByRoom.put(type, countByRoom.getOrDefault(type, 0) + 1);
        }

        for (String roomType : countByRoom.keySet()) {
            System.out.println(roomType + " Bookings: " + countByRoom.get(roomType));
        }
    }
}

// ================= MAIN =================
public class Main {

    public static void main(String[] args) {

        // Initialize history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from UC6)
        history.addReservation(new Reservation("SI-101", "Alice", "Single Room"));
        history.addReservation(new Reservation("SI-102", "Bob", "Single Room"));
        history.addReservation(new Reservation("DB-201", "Charlie", "Double Room"));
        history.addReservation(new Reservation("SU-301", "David", "Suite Room"));

        // Generate reports
        BookingReportService reportService = new BookingReportService(history);

        reportService.displayAllBookings();
        reportService.generateSummaryReport();
    }
}