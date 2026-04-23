import java.util.HashMap;
import java.util.Map;

// Inventory Class (Centralized State Management)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor → initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 5);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (controlled update)
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found!");
        }
    }

    // Display entire inventory
    public void displayInventory() {
        System.out.println("===== Current Room Inventory =====");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

// Main Class (Entry Point)
public class Main {

    public static void main(String[] args) {

        // Initialize inventory system
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        System.out.println("\nChecking availability...");
        System.out.println("Single Room Available: " + inventory.getAvailability("Single Room"));

        // Update availability
        System.out.println("\nUpdating availability...");
        inventory.updateAvailability("Single Room", 8);

        // Display updated inventory
        inventory.displayInventory();
    }
}