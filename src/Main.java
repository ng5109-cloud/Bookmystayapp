import java.util.*;

// ================= RESERVATION =================
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// ================= BOOKING QUEUE =================
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO removal
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ================= INVENTORY =================
class RoomInventory {
    private HashMap<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void reduceAvailability(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nUpdated Inventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + " -> " + inventory.get(key));
        }
    }
}

// ================= BOOKING SERVICE =================
class BookingService {

    private RoomInventory inventory;

    // Track all allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type → assigned room IDs
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processRequests(BookingRequestQueue queue) {

        while (!queue.isEmpty()) {

            Reservation req = queue.getNextRequest();
            String roomType = req.getRoomType();

            System.out.println("\nProcessing request for " + req.getGuestName());

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Ensure uniqueness (extra safety)
                while (allocatedRoomIds.contains(roomId)) {
                    roomId = generateRoomId(roomType);
                }

                // Add to global set
                allocatedRoomIds.add(roomId);

                // Add to type-wise allocation
                roomAllocations.putIfAbsent(roomType, new HashSet<>());
                roomAllocations.get(roomType).add(roomId);

                // Update inventory immediately
                inventory.reduceAvailability(roomType);

                // Confirm booking
                System.out.println("Booking CONFIRMED for " + req.getGuestName());
                System.out.println("Room Type: " + roomType + ", Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED for " + req.getGuestName() + " (No availability)");
            }
        }
    }

    // Room ID generator
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + (int)(Math.random() * 1000);
    }
}

// ================= MAIN =================
public class Main {

    public static void main(String[] args) {

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail (only 2 available)
        queue.addRequest(new Reservation("David", "Double Room"));

        // Process bookings
        BookingService bookingService = new BookingService(inventory);
        bookingService.processRequests(queue);

        // Show updated inventory
        inventory.displayInventory();
    }
}