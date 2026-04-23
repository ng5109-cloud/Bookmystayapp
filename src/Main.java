import java.util.*;

// ================= RESERVATION =================
class Reservation {
    private String reservationId;
    private String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }
}

// ================= INVENTORY =================
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0);
    }

    public void increaseAvailability(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + " -> " + inventory.get(key));
        }
    }
}

// ================= BOOKING HISTORY =================
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void addReservation(Reservation r) {
        bookings.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return bookings.get(id);
    }

    public void removeReservation(String id) {
        bookings.remove(id);
    }
}

// ================= CANCELLATION SERVICE =================
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    // Stack for rollback tracking
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for ID: " + reservationId);

        // Validate reservation exists
        Reservation r = history.getReservation(reservationId);

        if (r == null) {
            System.out.println("Cancellation FAILED: Reservation not found.");
            return;
        }

        // Push room ID into rollback stack
        rollbackStack.push(reservationId);

        // Restore inventory
        inventory.increaseAvailability(r.getRoomType());

        // Remove from booking history
        history.removeReservation(reservationId);

        System.out.println("Cancellation SUCCESS for " + reservationId);
    }

    // Display rollback stack
    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent First):");
        for (String id : rollbackStack) {
            System.out.println(id);
        }
    }
}

// ================= MAIN CLASS =================
public class Main {

    public static void main(String[] args) {

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("SI-101", "Single Room"));
        history.addReservation(new Reservation("DB-201", "Double Room"));

        // Cancellation service
        CancellationService cancelService = new CancellationService(inventory, history);

        // Perform cancellations
        cancelService.cancelBooking("SI-101"); // valid
        cancelService.cancelBooking("XX-999"); // invalid
        cancelService.cancelBooking("SI-101"); // already cancelled

        // Show rollback stack
        cancelService.showRollbackStack();

        // Show updated inventory
        inventory.displayInventory();
    }
}