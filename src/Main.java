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

// ================= SHARED QUEUE =================
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    // synchronized → only one thread at a time
    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// ================= INVENTORY =================
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    // synchronized critical section
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + " -> " + inventory.get(key));
        }
    }
}

// ================= THREAD PROCESSOR =================
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory, String name) {
        super(name);
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            Reservation r;

            // synchronized retrieval
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) break;

            // critical section for allocation
            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " CONFIRMED booking for " + r.getGuestName());
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED booking for " + r.getGuestName());
            }
        }
    }
}

// ================= MAIN CLASS =================
public class Main {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Double Room"));
        queue.addRequest(new Reservation("Eve", "Double Room")); // should fail

        // Create multiple threads (simulating users)
        BookingProcessor t1 = new BookingProcessor(queue, inventory, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(queue, inventory, "Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final inventory
        inventory.displayInventory();
    }
}