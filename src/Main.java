import java.io.*;
import java.util.*;

// ================= RESERVATION =================
class Reservation implements Serializable {
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
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// ================= INVENTORY =================
class RoomInventory implements Serializable {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + " -> " + inventory.get(key));
        }
    }
}

// ================= BOOKING HISTORY =================
class BookingHistory implements Serializable {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getHistory() {
        return history;
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            r.display();
        }
    }
}

// ================= PERSISTENCE SERVICE =================
class PersistenceService {

    private static final String FILE_NAME = "hotel_data.ser";

    // Save data to file
    public static void save(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("\nData saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load data from file
    public static Object[] load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("\nData loaded successfully.");
            return new Object[]{inventory, history};
        } catch (Exception e) {
            System.out.println("\nNo valid saved data found. Starting fresh.");
            return null;
        }
    }
}

// ================= MAIN CLASS =================
public class Main {

    public static void main(String[] args) {

        RoomInventory inventory;
        BookingHistory history;

        // Attempt to load previous state
        Object[] data = PersistenceService.load();

        if (data != null) {
            inventory = (RoomInventory) data[0];
            history = (BookingHistory) data[1];
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();

            // Simulate some bookings
            history.addReservation(new Reservation("SI-101", "Alice", "Single Room"));
            history.addReservation(new Reservation("DB-201", "Bob", "Double Room"));
        }

        // Display current state
        inventory.display();
        history.display();

        // Save state before exit
        PersistenceService.save(inventory, history);
    }
}