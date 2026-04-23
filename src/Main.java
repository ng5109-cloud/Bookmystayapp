import java.util.*;

// ================= RESERVATION (NEW - UC5) =================
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

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// ================= BOOKING QUEUE (FIFO) =================
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request to queue
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void displayQueue() {
        System.out.println("\n===== Booking Request Queue =====");

        if (queue.isEmpty()) {
            System.out.println("No booking requests.");
            return;
        }

        for (Reservation r : queue) {
            r.display();
        }
    }

    // Peek next request (FIFO head)
    public Reservation peekNext() {
        return queue.peek();
    }
}

// ================= MAIN CLASS =================
public class Main {

    public static void main(String[] args) {

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Display queue (FIFO order)
        bookingQueue.displayQueue();

        // Show next request to be processed
        System.out.println("\nNext request to process:");
        Reservation next = bookingQueue.peekNext();

        if (next != null) {
            next.display();
        }
    }
}